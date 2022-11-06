package com.pranshu.joke.repositories;

import com.pranshu.joke.models.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    AccessToken findByAccessToken(@NonNull String accessToken);
}