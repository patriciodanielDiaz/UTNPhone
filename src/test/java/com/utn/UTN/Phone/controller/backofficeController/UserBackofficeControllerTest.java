package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.config.RestUtil;
import com.utn.UTN.Phone.controller.clientController.UserController;
import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.DuplicateDNI;
import com.utn.UTN.Phone.exceptions.DuplicateUserName;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RestUtil.class)
public class UserBackofficeControllerTest {
    UserBackofficeController userBackofficeController;
    @Mock
    RestUtil restUtil;
    User user;
    @Mock
    UserService userService;
    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(RestUtil.class);
        userBackofficeController = new UserBackofficeController(userService, sessionManager);
        user = new User(1, "patricio", "123456", "bbbb", "cccc", "12345678", empleado, null, null, null, null);
    }
    @Test
    public void TestCreateUserOk() throws DuplicateUserName, DuplicateDNI {

        when(userService.findByDni("12345678")).thenReturn(null);
        when(userService.findByUser("patricio")).thenReturn(null);
        when(userService.addUser(user)).thenReturn(user);
        when(RestUtil.getLocation(1)).thenReturn(URI.create("miUri.com"));

        ResponseEntity re =userBackofficeController.createUser(user);
        List<String> headers = re.getHeaders().get("location");
        Assert.assertEquals(headers.get(0), "miUri.com");

        verify(userService, times(1)).findByDni("12345678");
        verify(userService, times(1)).findByUser("patricio");
        verify(userService, times(1)).addUser(user);

    }

    @Test(expected = DuplicateDNI.class)
    public void TestCreateUserDuplicateDNI() throws DuplicateUserName, DuplicateDNI {

        when(userService.findByDni("12345678")).thenReturn(user);
        userBackofficeController.createUser(user);

        verify(userService, times(1)).findByDni("12345678");
    }

    @Test(expected = DuplicateUserName.class)
    public void TestCreateUserDuplicateUserName() throws DuplicateUserName, DuplicateDNI {

        when(userService.findByDni("12345678")).thenReturn(null);
        when(userService.findByUser("patricio")).thenReturn(user);
        userBackofficeController.createUser(user);

        verify(userService, times(1)).findByDni("12345678");
        verify(userService, times(1)).findByUser("patricio");
    }

    @Test
    public void TestGetUserOk(){

        when(userService.findByDni("12345678")).thenReturn(user);
        ResponseEntity re = userBackofficeController.getUser("12345678");

        Assert.assertEquals(ResponseEntity.ok(user),re);
        verify(userService, times(1)).findByDni("12345678");

    }
    @Test
    public void TestGetUserNoContent(){

        when(userService.findByDni("12345678")).thenReturn(null);
        ResponseEntity re = userBackofficeController.getUser("12345678");

        Assert.assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(),re);
        verify(userService, times(1)).findByDni("12345678");

    }

    @Test
    public void TestUpdateUserOk(){

        UserDto userDto=new UserDto();
        when(userService.findByDni("12345678")).thenReturn(user);
        when(userService.updateCommonUser(userDto,1)).thenReturn(1);
        when(RestUtil.getLocation(1)).thenReturn(URI.create("miUri.com"));

        ResponseEntity re = userBackofficeController.updateUser(userDto,"12345678");
        List<String> headers = re.getHeaders().get("location");
        Assert.assertEquals(headers.get(0), "miUri.com");
        verify(userService, times(1)).findByDni("12345678");

    }
    @Test
    public void TestUpdateUser(){

        UserDto userDto=new UserDto();
        when(userService.findByDni("12345678")).thenReturn(null);

        ResponseEntity re = userBackofficeController.updateUser(userDto,"12345678");

        Assert.assertEquals(ResponseEntity.badRequest().build(),re);
        verify(userService, times(1)).findByDni("12345678");

    }
    @Test
    public void TestRmoveUserOk() throws UserNotExistException {

        when(userService.findByDni("12345678")).thenReturn(user);
        doNothing().when(userService).removeUser(any());
        ResponseEntity re = userBackofficeController.removeUser("12345678");

        Assert.assertEquals(ResponseEntity.ok().build(),re);
        verify(userService, times(1)).findByDni("12345678");

    }
    @Test(expected = UserNotExistException.class)
    public void TestRmoveUserUserNotExistException() throws UserNotExistException {

        when(userService.findByDni("12345678")).thenReturn(null);
        userBackofficeController.removeUser("12345678");
        verify(userService, times(1)).findByDni("12345678");

    }


}
