package com.ofss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ofss.entity.RefreshToken;



@Repository
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    @Query("""
            SELECT refreshToken
            FROM RefreshToken refreshToken
            JOIN FETCH refreshToken.user
            WHERE refreshToken.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findWithUserByTokenHash(
            @Param("tokenHash") String tokenHash
    );
}
