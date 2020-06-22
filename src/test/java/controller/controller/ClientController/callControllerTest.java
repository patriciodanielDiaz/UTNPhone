package controller.controller.ClientController;

import com.utn.UTN.Phone.controller.ClientController.CallController;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
