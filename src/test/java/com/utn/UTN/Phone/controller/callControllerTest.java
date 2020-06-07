package com.utn.UTN.Phone.controller;

import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class callControllerTest {
    CallService callService;
    LineService lineService;
    UserService userService;
    SessionManager sessionManager;
    CallController callController;
/*
    @Before
    public void setUp() {
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);

        callController = new CallController(callService,lineService,sessionManager,userService);
    }

    @Test
    public void getCallsmallTest(){

        /*User loggedUser = new User(1,"patricio", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null,null,null);
        when(userService.login("user", "pwd")).thenReturn(loggedUser);

        ResponseEntity responseEntity = callController.getCallsmall();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }*/
}
