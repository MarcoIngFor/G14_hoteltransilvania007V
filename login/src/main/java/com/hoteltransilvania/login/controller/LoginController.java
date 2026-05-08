package com.hoteltransilvania.login.controller;

import com.hoteltransilvania.login.DTO.LoginRequest;
import com.hoteltransilvania.login.DTO.LoginResponse;
import com.hoteltransilvania.login.service.LoginService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            LoginResponse response = service.login(request);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}