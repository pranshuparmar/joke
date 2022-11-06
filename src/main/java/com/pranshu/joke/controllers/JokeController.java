package com.pranshu.joke.controllers;

import com.pranshu.joke.models.dtos.AnalyzeResponseDTO;
import com.pranshu.joke.models.dtos.SearchResponseDTO;
import com.pranshu.joke.models.dtos.TokenDTO;
import com.pranshu.joke.services.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    @Autowired
    private JokeService jokeService;

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchJoke(@RequestParam String accessToken, @RequestParam String query, @RequestParam(required = false) Integer page) {
        return new ResponseEntity<>(jokeService.searchJoke(accessToken, query, page), HttpStatus.OK);
    }

    @PostMapping("/analyze")
    public ResponseEntity<TokenDTO> analyzeQuery(@RequestParam String accessToken, @RequestParam String query) {
        return new ResponseEntity<>(jokeService.analyzeQuery(query), HttpStatus.OK);
    }

    @GetMapping("/analyze")
    public ResponseEntity<AnalyzeResponseDTO> getAnalysisResult(@RequestParam String accessToken, @RequestParam String token) {
        return new ResponseEntity<>(jokeService.getAnalysisResult(token), HttpStatus.OK);
    }
}
