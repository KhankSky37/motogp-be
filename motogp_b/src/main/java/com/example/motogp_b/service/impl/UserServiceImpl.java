package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ForgotPasswordRequestDto;
import com.example.motogp_b.dto.PasswordDTO;
import com.example.motogp_b.dto.ResetPasswordRequestDto;
import com.example.motogp_b.dto.UserDto;
import com.example.motogp_b.entity.User;
import com.example.motogp_b.repository.UserRepository;
import com.example.motogp_b.service.EmailService;
import com.example.motogp_b.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    EmailService emailService;
    @Override
    public UserDto login(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDto.getEmail()));


        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public String register(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }
        if (userDto.getPassword() == null || userDto.getConfirmPassword() == null || userDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password and confirm password cannot be empty");
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        return "User registered successfully with ID: " + savedUser.getId();
    }

    @Override
    public List<UserDto> findAll(UserDto userDto) {
        return userRepository.findAllUsers(userDto).stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto findById(String id) {
        return modelMapper.map(userRepository.findById(id), UserDto.class);
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + userDto.getEmail());
        }
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto update(String id, UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(id);
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public void updatePassword(String id, PasswordDTO passwordDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password");
        }

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        if (passwordDTO.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String processForgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        User user = userRepository.findByEmail(forgotPasswordRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + forgotPasswordRequestDto.getEmail()));
        // Trong production, bạn có thể muốn trả về thông báo chung để tránh user enumeration

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(3)); // OTP hết hạn sau 10 phút
        userRepository.save(user); // Lưu OTP và thời gian hết hạn vào DB

        // Gửi email (bạn đã xác nhận phần này hoạt động)
        emailService.sendOtpEmail(user.getEmail(), "Your Password Reset OTP", otp);

        return "OTP has been sent to your email address. Please check your inbox.";
    }

    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        User user = userRepository.findByOtp(resetPasswordRequestDto.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP."));

        // Kiểm tra OTP hết hạn
        if (user.getOtpExpiryDate() == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            // Xóa OTP cũ nếu hết hạn để tránh nhầm lẫn
            user.setOtp(null);
            user.setOtpExpiryDate(null);
            userRepository.save(user);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        // Xác thực mật khẩu mới
        if (resetPasswordRequestDto.getNewPassword() == null || resetPasswordRequestDto.getNewPassword().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty.");
        }
        if (!resetPasswordRequestDto.getNewPassword().equals(resetPasswordRequestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        // Xóa OTP sau khi đã sử dụng thành công
        user.setOtp(null);
        user.setOtpExpiryDate(null);
        userRepository.save(user);

        return "Password has been reset successfully.";
    }

    private String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000); // Đảm bảo 6 chữ số
        return String.valueOf(otpNumber);
    }
}