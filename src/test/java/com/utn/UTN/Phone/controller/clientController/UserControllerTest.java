package com.utn.UTN.Phone.controller.clientController;

import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class UserControllerTest {

    UserService userService;
    SessionManager sessionManager;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        sessionManager = mock(SessionManager.class);
    }

    @Test
    public void getProfileOk()throws PermissionDeniedException, UserNotExistException {


    }
}
