package com.ofss.service;

import com.ofss.entity.AppUser;
import com.ofss.entity.RefreshToken;

public interface RefreshTokenService {

    String createRefreshToken(AppUser user);

    RefreshToken getValidRefreshToken(String rawRefreshToken);

    String rotateRefreshToken(RefreshToken oldRefreshToken);
}
