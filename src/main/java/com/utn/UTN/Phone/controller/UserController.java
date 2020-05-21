package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    /*

    public User getUserById(Integer userId) {
        return userService.getUser(userId);
    }


    public User createUser(User user) throws UserAlreadyExistsException {
        return userService.createUser(user);
    }

    public void removeUser(User user) throws UserNotExistException {
        userService.removeUser(user);
    }

    public void updateUser(User user) throws UserNotExistException {
        userService.updateUser(user);
    }
*/
    /// metodos de prueba de funcionamiento
    @GetMapping("/")
    public List<User> getUser(){
        return userService.getAll();
    }

    @PostMapping("/")
    public void addUser(@RequestBody @Valid User user){
        userService.addUser(user);
    }
}