package com.popularshop.service;

import com.popularshop.dto.*;
import com.popularshop.entity.RefreshToken;
import com.popularshop.entity.User;
import com.popularshop.exception.BadRequestException;
import com.popularshop.repository.RefreshTokenRepository;
import com.popularshop.repository.UserRepository;
import com.popularshop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("此 Email 已被註冊");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(User.Role.USER)
                .active(true)
                .build();
        userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = createRefreshToken(user);

        return AuthResponse.of(accessToken, refreshToken, UserResponse.fromEntity(user));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("使用者不存在"));

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = createRefreshToken(user);

        return AuthResponse.of(accessToken, refreshToken, UserResponse.fromEntity(user));
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadRequestException("無效的 Refresh Token"));

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new BadRequestException("Refresh Token 已過期，請重新登入");
        }

        User user = storedToken.getUser();
        refreshTokenRepository.delete(storedToken);

        String accessToken = tokenProvider.generateAccessToken(user.getEmail());
        String newRefreshToken = createRefreshToken(user);

        return AuthResponse.of(accessToken, newRefreshToken, UserResponse.fromEntity(user));
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private String createRefreshToken(User user) {
        String token = tokenProvider.generateRefreshToken(user.getEmail());
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpiration() / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return token;
    }
}
