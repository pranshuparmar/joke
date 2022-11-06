package com.pranshu.joke.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public final class Utility {

    public static String getNewAccessToken() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static HttpEntity getExternalRequestEntity() {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypeList);
        return new HttpEntity<>(headers);
    }
}
