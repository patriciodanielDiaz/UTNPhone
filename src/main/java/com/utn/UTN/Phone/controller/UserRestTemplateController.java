package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/parcial")
public class UserRestTemplateController {

    CallService callService;
    UserService userService;

    @Autowired
    public UserRestTemplateController(UserService userService, CallService callService) {
        this.callService = callService;
        this.userService = userService;
    }

    //----------------------------------parcial German-------------------------------------------
    //usuario con la llamada mas corta
    @GetMapping("/")
    public ResponseEntity<User> getCallsmall() throws RecordNotExistsException, UserNotExistException {


        Call call=callService.getCallSmall();
        User user =userService.getUserByNum(call.getOriginCall().getLinenumber());

        return (user!= null) ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
