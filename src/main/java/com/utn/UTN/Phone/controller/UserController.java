package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.session.SessionManager;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;
    SessionManager sessionManager;

    @Autowired
    public UserController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    //--------------------------------------------------------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity createUser(@RequestBody @Valid User addUser) throws URISyntaxException, DuplicateUserName, DuplicateDNI {

        ResponseEntity response;
        User user;
        user = userService.findByDni(addUser.getDni());

        if (user != null) {
            throw new DuplicateDNI();
        } else {
            user = userService.findByUser(addUser.getUser());
            if (user != null) {
                throw new DuplicateUserName();
            }
        }
        userService.addUser(addUser);
        response = ResponseEntity.created(new URI("/login")).build();
        return response;
    }

    //--------------------------------------------------------------------------------------------------
    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid User userUpdate)
                                     throws PermissionDeniedException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            throw new PermissionDeniedException();
        }

        userService.updateUser(userUpdate, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    // ------------------no funciona cry cry -------------------------------------------------------------
    @PostMapping("/remove")
    public ResponseEntity removeUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid LoginDto userDto)
                                     throws PermissionDeniedException, InvalidLoginException {


        User currentUser = sessionManager.getCurrentUser(sessionToken); //ERROR ,me lo trae lazy, no me deja entrar a los metodos
        System.out.println(currentUser);

        if (currentUser == null) {
            throw new PermissionDeniedException();
        } else if (userDto.getUsername() == currentUser.getUser() && userDto.getPassword() == currentUser.getPassword()) {
            userService.removeUser(currentUser.getId());
        } else {
            throw new InvalidLoginException();
        }
        return ResponseEntity.ok().build();
    }

}