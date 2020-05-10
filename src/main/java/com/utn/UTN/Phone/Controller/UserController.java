package com.utn.UTN.Phone.Controller;

import com.utn.UTN.Phone.Model.User;
import com.utn.UTN.Phone.Service.UserService;
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

    @GetMapping("/")
    public List<User> getPet(){
        return userService.getAll();
    }

    @PostMapping("/")
    public void addUser(@RequestBody @Valid User user){
        userService.addUser(user);
    }
}