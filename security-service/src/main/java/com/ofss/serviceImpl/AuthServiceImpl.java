package com.ofss.serviceImpl;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofss.dto.AdminCreateUserRequest;
import com.ofss.dto.AuthResponse;
import com.ofss.dto.LoginRequest;
import com.ofss.dto.RefreshTokenRequest;
import com.ofss.dto.RegisterRequest;
import com.ofss.dto.UserResponse;
import com.ofss.entity.AppUser;
import com.ofss.entity.RefreshToken;
import com.ofss.enums.Role;
import com.ofss.exception.BadRequestException;
import com.ofss.exception.UnauthorizedException;
import com.ofss.repository.AppUserRepository;
import com.ofss.security.JwtService;
import com.ofss.service.AuthService;
import com.ofss.service.RefreshTokenService;
import com.ofss.exception.ResourceNotFoundException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    //private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {

        validateUniqueUsernameAndEmail(
                request.username(),
                request.email()
        );

        AppUser user = new AppUser();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        AppUser savedUser = appUserRepository.save(user);

        return toAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        AppUser user = appUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException(
                        "Invalid username or password"
                ));

        if (!user.isEnabled()) {
            throw new UnauthorizedException(
                    "User account is disabled"
            );
        }

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new UnauthorizedException(
                    "Invalid username or password"
            );
        }

        String token = jwtService.generateJwtToken(user);

        return toAuthResponse(user);
    }

    @Override
    public UserResponse createUserByAdmin(
            AdminCreateUserRequest request
    ) {

        validateUniqueUsernameAndEmail(
                request.username(),
                request.email()
        );

        AppUser user = new AppUser();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setEnabled(true);

        AppUser savedUser = appUserRepository.save(user);

        return toUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return appUserRepository.findAll()
                .stream()
                .map(this::toUserResponse)
                .toList();
    }

    private void validateUniqueUsernameAndEmail(
            String username,
            String email
    ) {

        if (appUserRepository.existsByUsername(username)) {
            throw new BadRequestException(
                    "Username already exists"
            );
        }

        if (appUserRepository.existsByEmail(email)) {
            throw new BadRequestException(
                    "Email already exists"
            );
        }
    }

    private AuthResponse toAuthResponse(AppUser user) {

        String accessToken = jwtService.generateJwtToken(user);

        String refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                accessToken,
                refreshToken
        );
    }

    private UserResponse toUserResponse(AppUser user) {

        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }

    @Override
    public AuthResponse refreshAccessToken(
            RefreshTokenRequest request
    ) {

        RefreshToken oldRefreshToken =
                refreshTokenService.getValidRefreshToken(
                        request.refreshToken()
                );

        AppUser user = oldRefreshToken.getUser();

        String newAccessToken =
                jwtService.generateJwtToken(user);

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(
                        oldRefreshToken
                );

        return new AuthResponse(
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                newAccessToken,
                newRefreshToken
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User",
                        userId.toString()
                ));

        return toUserResponse(user);
    }
}