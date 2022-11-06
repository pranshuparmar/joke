package com.pranshu.joke.models.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccessTokenDTO {

    private String accessToken;
}
