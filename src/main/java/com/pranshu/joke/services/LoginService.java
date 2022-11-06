package com.pranshu.joke.services;

import com.pranshu.joke.exceptions.UnauthorizedUserException;
import com.pranshu.joke.models.dtos.AccessTokenDTO;
import com.pranshu.joke.models.entities.AccessToken;
import com.pranshu.joke.repositories.AccessTokenRepository;
import com.pranshu.joke.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    @Value("${token.length}")
    private int tokenLength;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public AccessTokenDTO validateLogin(String authorization) {
        if(authorization == null || !authorization.startsWith("Bearer ") || authorization.length() != tokenLength) {
            log.error("Invalid authorization header");
            throw new UnauthorizedUserException();
        }

        AccessTokenDTO accessTokenDTO = AccessTokenDTO.builder().accessToken(Utility.getNewAccessToken()).build();

        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(accessTokenDTO.getAccessToken());
        accessTokenRepository.save(accessToken);

        return accessTokenDTO;
    }
}
