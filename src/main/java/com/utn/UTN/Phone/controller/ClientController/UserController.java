package com.utn.UTN.Phone.controller.ClientController;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/user")
public class UserController {
    UserService userService;
    SessionManager sessionManager;

    @Autowired
    public UserController(UserService userService, SessionManager sessionManager) {
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/")
    private ResponseEntity<ProfileProyection> getProfile(@RequestHeader("Authorization") String sessionToken) throws PermissionDeniedException, UserNotExistException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            throw new PermissionDeniedException();
        }
        ProfileProyection profileProyection= userService.getProfile(currentUser.getId());
        return (profileProyection!= null) ? ResponseEntity.ok(profileProyection) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/")
    public ResponseEntity updateUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid UserDto userDto)
                                     throws PermissionDeniedException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            throw new PermissionDeniedException();
        }

        Integer idUser=userService.updateCommonUser(userDto, currentUser.getId());
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(idUser).toUri();
        return ResponseEntity.created(location).build();
    }

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
    /*
    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody @Valid UserDto addUser)
            throws URISyntaxException, DuplicateUserName, DuplicateDNI, SQLException {

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
        Integer iduser=userService.addCommonUser(addUser);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(iduser).toUri();
        return ResponseEntity.created(location).build();
    }*/

}