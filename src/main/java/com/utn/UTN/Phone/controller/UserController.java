package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.session.SessionManager;
import netscape.security.ForbiddenTargetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    //-----------------------------------------------------------------------------------------------------
    @GetMapping("/")
    private ResponseEntity<ProfileProyection> getProfile(@RequestHeader("Authorization") String sessionToken) throws PermissionDeniedException, UserNotExistException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            throw new PermissionDeniedException();
        }
        ProfileProyection profileProyection= userService.getProfile(currentUser.getId());
        return (profileProyection!= null) ? ResponseEntity.ok(profileProyection) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //----------------------------------------------------------------------------------------------------------------------
    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody @Valid UserDto addUser) throws URISyntaxException, DuplicateUserName, DuplicateDNI, SQLException {

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
        userService.addCommonUser(addUser); //agregar location
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    //--------------------------------------------------------------------------------------------------
    @PutMapping("/")
    public ResponseEntity updateUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid UserDto userDto)
                                     throws PermissionDeniedException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            throw new PermissionDeniedException();
        }

        userService.updateCommonUser(userDto, currentUser.getId());
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(currentUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    // ----------------------------------------------------------------------------------------------------------
    @DeleteMapping("/")
    public ResponseEntity removeUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid LoginDto userDto)
                                     throws PermissionDeniedException, InvalidLoginException {


        User user = sessionManager.getCurrentUser(sessionToken);
        if (user==null) throw new PermissionDeniedException();
        else if (userDto.getPassword().equals(user.getPassword()) && userDto.getUsername().equals(user.getUser())) {
            userService.removeUser(user.getId());
        } else {
            throw new InvalidLoginException();
        }
        return ResponseEntity.ok().build();
    }

}