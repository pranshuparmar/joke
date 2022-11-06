package com.pranshu.joke.utils;

import com.pranshu.joke.models.entities.AccessToken;
import com.pranshu.joke.repositories.AccessTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class AccessTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getParameter("accessToken");
        boolean isValidToken = true;
        if(accessToken == null || accessToken.isEmpty()) {
            isValidToken = false;
        } else {
            AccessToken accessTokenEntity = accessTokenRepository.findByAccessToken(accessToken);
            if(accessTokenEntity == null || ChronoUnit.MINUTES.between(accessTokenEntity.getCreatedAt(), LocalDateTime.now()) > 15) {
                isValidToken = false;
            }
        }

        if(isValidToken) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/login");
    }
}
