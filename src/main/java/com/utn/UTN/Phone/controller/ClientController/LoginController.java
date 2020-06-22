package com.utn.UTN.Phone.controller.ClientController;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    /*@ApiOperation(value= "user login")
    @ApiResponses(value={
            @ApiResponse(code =200, message = "successful login"),
            @ApiResponse(code =400, message = "incomplete data"),
            @ApiResponse(code =400, message = "User not exists")
    })*/
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) throws ValidationException, UserNotExistException {


        if(loginDto.getPassword()==null||loginDto.getUsername()==null){
            throw new ValidationException();
        }
        User u = userService.login(loginDto.getUsername(), loginDto.getPassword());
        String token = sessionManager.createSession(u);

        return ResponseEntity.ok().headers(createHeaders(token)).build();
    }

    /*@ApiOperation(value= "user login")
    @ApiResponses(value={
            @ApiResponse(code =200, message = "successful login")
    })*/
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        sessionManager.removeSession(token);
        return ResponseEntity.ok().build();
    }

    public HttpHeaders createHeaders(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }
}
