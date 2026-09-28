package com.ofss.serviceImpl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofss.entity.AppUser;
import com.ofss.entity.RefreshToken;
import com.ofss.exception.UnauthorizedException;
import com.ofss.repository.RefreshTokenRepository;
import com.ofss.service.RefreshTokenService;


@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh-token.expiration-months}")
    private int refreshTokenExpirationMonths;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String createRefreshToken(AppUser user) {

        String rawRefreshToken = generateSecureToken();

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setTokenHash(hashToken(rawRefreshToken));
        refreshToken.setCreatedAt(LocalDateTime.now(IST));
        refreshToken.setExpiresAt(
                LocalDateTime.now(IST)
                        .plusMonths(refreshTokenExpirationMonths)
        );
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return rawRefreshToken;
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken getValidRefreshToken(String rawRefreshToken) {

        RefreshToken refreshToken = refreshTokenRepository
                .findWithUserByTokenHash(hashToken(rawRefreshToken))
                .orElseThrow(() -> new UnauthorizedException(
                        "Refresh token is invalid"
                ));

        boolean expired = !LocalDateTime.now(IST)
                .isBefore(refreshToken.getExpiresAt());

        if (refreshToken.isRevoked() || expired) {
            throw new UnauthorizedException(
                    "Refresh token is expired or revoked"
            );
        }

        if (!refreshToken.getUser().isEnabled()) {
            throw new UnauthorizedException(
                    "User account is disabled"
            );
        }

        return refreshToken;
    }

    @Override
    public String rotateRefreshToken(RefreshToken oldRefreshToken) {

        oldRefreshToken.setRevoked(true);

        refreshTokenRepository.save(oldRefreshToken);

        return createRefreshToken(oldRefreshToken.getUser());
    }

    private String generateSecureToken() {

        byte[] randomBytes = new byte[32];

        new SecureRandom().nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }

    private String hashToken(String rawToken) {

        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = messageDigest.digest(
                    rawToken.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is not available",
                    exception
            );
        }
    }
}
