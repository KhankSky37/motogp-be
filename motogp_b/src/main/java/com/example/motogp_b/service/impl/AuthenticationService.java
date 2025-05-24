package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.AuthenticationDto;
import com.example.motogp_b.dto.InvalidatedTokenDto;
import com.example.motogp_b.entity.InvalidatedToken;
import com.example.motogp_b.entity.User;
import com.example.motogp_b.repository.InvalidatedTokenRepository;
import com.example.motogp_b.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    private String signerKey;

    public boolean introspect(String token){
        boolean result = true;
        try {
            verifyToken(token);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public String authenticate(AuthenticationDto authenticationRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            log.error("Authentication failed");
            throw new RuntimeException("Authentication failed");
        }

        log.info("Authenticating user");
        return generateToken(user);
    }

    public String refreshToken(String token) throws ParseException, JOSEException {
        var signedJWT = verifyToken(token);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return generateToken(user);
    }
    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("motogp_b")
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .expirationTime(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout(InvalidatedTokenDto invalidatedTokenDto) throws JOSEException, ParseException {
        var signToken = verifyToken(invalidatedTokenDto.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException {
        try {
            JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if(!signedJWT.verify(verifier) || expiryTime.before(new Date())) {
                throw new RuntimeException("Unauthorized");
            }

            if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new RuntimeException("Unauthorized");
            }

            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
