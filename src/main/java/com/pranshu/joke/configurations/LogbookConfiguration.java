package com.pranshu.joke.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.json.JsonPathBodyFilters;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

import java.util.Collection;
import java.util.HashSet;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook getLogbook() {
        Collection<Sink> sinkCollection = new HashSet<>();

        Sink logFileSink = new DefaultSink(new JsonHttpLogFormatter(), new DefaultHttpLogWriter());
        sinkCollection.add(logFileSink);
        return Logbook.builder()
                .queryFilter(QueryFilters.replaceQuery("accessToken", "<accessToken>"))
                .bodyFilter(JsonPathBodyFilters.jsonPath("$.accessToken").replace("<accessToken>"))
                .sink(new CompositeSink(sinkCollection))
                .build();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(new LogbookClientHttpRequestInterceptor(getLogbook()));
        return restTemplate;
    }
}
