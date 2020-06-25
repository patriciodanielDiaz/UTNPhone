package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.config.RestUtil;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/backoffice/user")
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
            throws DuplicateUserName, DuplicateDNI {

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
        return ResponseEntity.created(RestUtil.getLocation(user.getId())).build();
    }
    @PutMapping("/{dni}")
    public ResponseEntity UpdateUser(@RequestHeader("Authorization") String sessionToken,
                                     @RequestBody @Valid UserDto userDto,
                                     @PathVariable("dni") String dni) {

        ResponseEntity response;
        User user;
        user = userService.findByDni(dni);
        if(user!=null){
            Integer idUser=userService.updateCommonUser(userDto, user.getId());
            return ResponseEntity.created(RestUtil.getLocation(idUser)).build();}
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
    @DeleteMapping("/{dni}")
    public ResponseEntity removeUser(@RequestHeader("Authorization") String sessionToken,
                                     @PathVariable("dni") String dni)
                                    throws UserNotExistException {

        User user = userService.findByDni(dni);

        if ((user != null)) {
            userService.removeUser(user.getId());
        } else {
            throw new UserNotExistException();
        }
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/")
    private ResponseEntity <List<User>> getAllUser(@RequestHeader("Authorization") String sessionToken) throws UserNotExistException {

        List<User> list= userService.getAll();
        return (list!= null) ? ResponseEntity.ok(list) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }*/
}
