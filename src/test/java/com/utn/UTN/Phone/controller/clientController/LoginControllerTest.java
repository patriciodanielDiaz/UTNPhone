package com.utn.UTN.Phone.controller.clientController;
import com.utn.UTN.Phone.controller.clientController.LoginController;
import com.utn.UTN.Phone.dto.LoginDto;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.exceptions.ValidationException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
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
        when(sessionManager.createSession(loggedUser)).thenReturn("123456");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "123456");

        LoginDto loginDto=new LoginDto("user", "pwd");
        ResponseEntity responseEntity = loginController.login(loginDto);

        assertEquals( ResponseEntity.ok().headers(responseHeaders).build(),responseEntity);

        verify(userService, times(1)).login("user", "pwd");
        verify(sessionManager, times(1)).createSession(loggedUser);
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
    @Test
    public void testLogout(){

        ResponseEntity re=loginController.logout("123456");
        sessionManager.removeSession("123456");
        assertEquals( re, ResponseEntity.ok().build());
    }
    @Test
    public void testCreateHeaders(){

        HttpHeaders responseHeaders=loginController.createHeaders("123456");
        HttpHeaders re = new HttpHeaders();
        re.set("Authorization", "123456");

        assertEquals(responseHeaders,re);
    }
}
