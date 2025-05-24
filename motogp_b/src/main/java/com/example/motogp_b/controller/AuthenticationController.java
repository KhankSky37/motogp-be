package com.example.motogp_b.controller;

import com.example.motogp_b.dto.AuthenticationDto;
import com.example.motogp_b.dto.InvalidatedTokenDto;
import com.example.motogp_b.service.impl.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationDto authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/introspect")
    public ResponseEntity<Boolean> introspect(@RequestBody String token) {
        return ResponseEntity.ok(authenticationService.introspect(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody InvalidatedTokenDto token) throws JOSEException, ParseException {
        authenticationService.logout(token);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestParam String token) throws ParseException, JOSEException {
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }
}
