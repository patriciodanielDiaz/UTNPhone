package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.LoginRequestDto;
import com.utn.UTN.Phone.Model.User;
import com.utn.UTN.Phone.Service.UserService;
import com.utn.UTN.Phone.exceptions.UserAlreadyExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /// metodos de prueba login

    @ResponseBody @RequestMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) throws  ValidationException {

       User user = userService.login(loginRequestDto.getUsername(),loginRequestDto.getPassword());

        return user.getName();
    }

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