package com.utn.UTN.Phone.controller.backoffice;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.DuplicateDNI;
import com.utn.UTN.Phone.exceptions.DuplicateUserName;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/backoffice/users")
public class UserBackofficeController {

    UserService userService;
    SessionManager sessionManager;

    @Autowired
    public UserBackofficeController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }


    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody @Valid User userAdd)
            throws URISyntaxException, DuplicateUserName, DuplicateDNI {

        User user=userService.findByDni(userAdd.getDni());

        if (user != null) {
            throw new DuplicateDNI();
        } else {
            user = userService.findByUser(userAdd.getUser());

            if (user != null) {
                throw new DuplicateUserName();
            }
        }
        user= userService.addUser(userAdd);
        System.out.println(user.getId());
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @PutMapping("/{dni}")
    public ResponseEntity UpdateUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid User userAdd,
                                     @PathVariable("dni") String dni)
            throws URISyntaxException{

        ResponseEntity response;
        User user;
        user = userService.findByDni(dni);
        if(user!=null){
            userService.updateUser(userAdd, user.getId());
            URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.created(location).build();}
        else{
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{dni}")
    private ResponseEntity <User> getUser(@RequestHeader("Authorization") String sessionToken,
                                                 @PathVariable("dni") String dni) {
        User user = userService.findByDni(dni);
        return (user!= null) ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/")
    private ResponseEntity <List<User>> getAllUser(@RequestHeader("Authorization") String sessionToken) throws UserNotExistException {

        List<User> list= userService.getAll();
        return (list!= null) ? ResponseEntity.ok(list) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
