package com.utn.UTN.Phone.controller.clientController;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.InvalidLoginException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.exceptions.ValidationException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.PowerMockUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class UserControllerTest {

    UserController userController;
    User user;
    ProfileProyection profileProyection;
    @Mock
    UserService userService;
    @Mock
    SessionManager sessionManager;

    @Before
    public void setUp() throws UserNotExistException {
        initMocks(this);
        userController = new UserController(userService, sessionManager);
        user = new User(1, "patricio", "123456", "bbbb", "cccc", "12345678", empleado, null, null, null, null);

    }

    @Test
    public void getProfileOk() throws PermissionDeniedException, UserNotExistException {

            profileProyection= new ProfileProyection() {
                @Override public String getUser() {return "mariano";}
                @Override public String getPassword() {return "123456";}
                @Override public String getName() {return "Mariano";}
                @Override public String getLastname() { return "Zanier"; }
                @Override public String getDni() { return "3333333"; }
                @Override public String getCity() { return"Mar del Plata"; }};

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(userService.getProfile(1)).thenReturn(profileProyection);
        ResponseEntity re =userController.getProfile("123456");

        assertEquals( ResponseEntity.ok(profileProyection),re);
        verify(userService, times(1)).getProfile(1);
        verify(sessionManager, times(1)).getCurrentUser("123456");
    }
    @Test
    public void getProfileNoContentOk() throws PermissionDeniedException, UserNotExistException {

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(userService.getProfile(1)).thenReturn(null);
        ResponseEntity re =userController.getProfile("123456");

        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(),re);
        verify(userService, times(1)).getProfile(1);
        verify(sessionManager, times(1)).getCurrentUser("123456");
    }

    @Test(expected = PermissionDeniedException.class)
    public void getProfilePermissionDeniedException() throws PermissionDeniedException, UserNotExistException {
        when(sessionManager.getCurrentUser("123456")).thenReturn(null);
        userController.getProfile("123456");
    }

    @Test(expected = UserNotExistException.class)
    public void getProfileUserNotExistException() throws PermissionDeniedException, UserNotExistException {

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(userService.getProfile(1)).thenThrow(new UserNotExistException());
        userController.getProfile("123456");
    }

    @Test
    public void getUpdateUserOk()throws PermissionDeniedException {
        UserDto userDto = new UserDto("patricio", "123456", "patricio", "diaz", "123456", "Mar del Plata");
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(userService.updateCommonUser(userDto,1)).thenReturn(1);
        ResponseEntity re= userController.updateUser("123456",userDto);

    }

    @Test
    public void getRemoveUserOk ()throws PermissionDeniedException, InvalidLoginException {


    }
}
//-------------------------------------------------------------------------------------
      /*MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] data = "mimamamemima".getBytes();
        m.update(data,0,data.length);
        BigInteger i = new BigInteger(1,m.digest());
        System.out.println(String.format("%1$032X", i))

   @RunWith(PowerMockRunner.class)

 @PrepareForTest(RestUtils.class)
    public class MessageWebControllerTest {
        @Mock
        SessionManager sessionManager;
        @Mock
        MessageController messageController;
        @Mock
        RestUtils restUtils;
        MessageWebController messageWebController;
        @Test
        public void testNewMessageOk() throws UserNotexistException {
            initMocks(this);
            PowerMockito.mockStatic(RestUtils.class);
            Message m = new Message(null, TestFixture.getUser(), TestFixture.getUser(), "subject", "body", LocalDateTime.now());
            Message newMessage = TestFixture.getMessage();
            when(sessionManager.getCurrentUser("1")).thenReturn(TestFixture.getUser());
            when(messageController.newMessage(m)).thenReturn(newMessage);
            when(RestUtils.getMessageLocation(newMessage)).thenReturn(URI.create("miUri.com"));
            messageWebController = new MessageWebController(messageController, sessionManager);
            ResponseEntity<Message> returnedMessage = messageWebController.newMessage("1", m);
            List<String> headers = returnedMessage.getHeaders().get("location");
            Assert.assertEquals(headers.get(0), "miUri.com");
        }
        private URI getLocation(Message message) {
            return ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(message.getMessageId())
                    .toUri();
        }
    }
    //----------------clase ----------------------------
    public class RestUtils {
    public static  URI getMessageLocation(Message message) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(message.getMessageId())
                .toUri();
    }
}
*/

