package com.utn.UTN.Phone.controller;
import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.exceptions.InvalidLoginException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.exceptions.ValidationException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class LoginControllerTest {

    UserService userService;
    LoginController loginController;
    SessionManager sessionManager;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);
        loginController = new LoginController(userService,sessionManager);
    }

    @Test
    public void testLoginOk() throws ValidationException, UserNotExistException {

        User loggedUser = new User(1,"patricio", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);
        when(userService.login("user", "pwd")).thenReturn(loggedUser);
        LoginDto loginDto=new LoginDto("user", "pwd");
        ResponseEntity responseEntity = loginController.login(loginDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //verifica que entre al service
        verify(userService, times(1)).login("user", "pwd");
    }
    @Test(expected = ValidationException.class)
    public void testLoginValidationException() throws ValidationException, UserNotExistException {
        LoginDto loginDto=new LoginDto(null, null);
        loginController.login(loginDto);
    }
    @Test(expected = UserNotExistException.class)
    public void testLoginUserNotExistException() throws UserNotExistException, ValidationException {
        when(userService.login("user","pwd")).thenThrow(new UserNotExistException());
        LoginDto loginDto=new LoginDto("user", "pwd");
        loginController.login(loginDto);
    }

}
