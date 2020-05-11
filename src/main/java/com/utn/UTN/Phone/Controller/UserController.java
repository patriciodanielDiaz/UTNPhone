package com.utn.UTN.Phone.Controller;

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

    /*public User login(String username, String password) throws  ValidationException {
        if ((username != null) && (password != null)) {
                return userService.login(username, password);

        } else {
            throw new ValidationException();
        }
    }*/

    /*public User createUser(User user) throws UserAlreadyExistsException {
        return userService.createUser(user);
    }

    public void removeUser(User user) throws UserNotExistException {
        userService.removeUser(user);
    }*/




    /// metodos de prueba de fncionamiento
    @GetMapping("/")
    public List<User> getPet(){
        return userService.getAll();
    }

    @PostMapping("/")
    public void addUser(@RequestBody @Valid User user){
        userService.addUser(user);
    }
}