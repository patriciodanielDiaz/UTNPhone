package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class LoginController {

    UserService userService;
    SessionManager sessionManager;

    @Autowired
    public LoginController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) throws ValidationException, UserNotExistException {

        ResponseEntity response;

        if(loginDto.getPassword()==null||loginDto.getUsername()==null){
            throw new ValidationException();
        }
        User u = userService.login(loginDto.getUsername(), loginDto.getPassword());
        String token = sessionManager.createSession(u);
        response = ResponseEntity.ok().headers(createHeaders(token)).build();

        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        sessionManager.removeSession(token);
        return ResponseEntity.ok().build();
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }
}