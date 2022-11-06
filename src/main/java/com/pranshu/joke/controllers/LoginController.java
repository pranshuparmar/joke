package com.pranshu.joke.controllers;

import com.pranshu.joke.models.dtos.AccessTokenDTO;
import com.pranshu.joke.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<AccessTokenDTO> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return new ResponseEntity<>(loginService.validateLogin(authorization), HttpStatus.OK);
    }
}
