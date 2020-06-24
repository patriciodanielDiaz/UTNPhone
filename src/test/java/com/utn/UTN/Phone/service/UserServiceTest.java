package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.proyection.ProfileProyection;
import com.utn.UTN.Phone.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserService.class)
public class UserServiceTest {
    @Mock
    UserService userService;
    @Mock
    UserRepository userRepository;
    User user;
    UserDto userDto;

    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(UserService.class);
        userService= new UserService(userRepository);
        user= new User(1,"mariano", "123456", "bbbb", "cccc","12345678",empleado,null,null,null,null);
        userDto= new UserDto("mariano", "123456", "bbbb", "cccc","12345678","Mar del Plata");

    }

    @Test
    public void testLoginOk() throws UserNotExistException {


        when(UserService.cryptWithMD5("pwd")).thenReturn("pwd");
        when(userRepository.login("user", "pwd")).thenReturn(user);
        User userReturn = userService.login("user", "pwd");

        assertEquals(user.getId(),userReturn.getId());
        assertEquals(user.getDni(),userReturn.getDni());
        verify(userRepository, times(1)).login("user", "pwd");
    }
    @Test(expected = UserNotExistException.class)
    public void testLoginUserNotExistException() throws UserNotExistException{
        when(userRepository.login("user","pwd")).thenReturn(null);
        userService.login("user", "pwd");
    }
    @Test
    public void testFindByDniOk(){
        when(userRepository.findByDni("12345678")).thenReturn(user);
        User userReturn = userService.findByDni("12345678");
        assertEquals(user.getId(),userReturn.getId());
        assertEquals(user.getDni(),userReturn.getDni());
        verify(userRepository, times(1)).findByDni("12345678");
    }
    @Test
    public void testFindByDniNull(){
        when(userRepository.findByDni("12345678")).thenReturn(null);
        User u = userService.findByDni("12345678");
        assertEquals(null,u);
    }

    @Test
    public void testFindByUserOk(){
        when(userRepository.findByUser("mariano")).thenReturn(user);
        User userReturn = userService.findByUser("mariano");
        assertEquals(user.getId(),userReturn.getId());
        assertEquals(user.getDni(),userReturn.getDni());
        verify(userRepository, times(1)).findByUser("mariano");
    }
    @Test
    public void testFindByUserNull(){
        when(userRepository.findByUser("mariano")).thenReturn(null);
        User u = userService.findByUser("mariano");
        assertEquals(null,u);
    }

    @Test
    public void testAddUserOk(){
        when(userRepository.save(user)).thenReturn(user);
        User userReturn = userService.addUser(user);
        assertEquals(user.getId(),userReturn.getId());
        assertEquals(user.getDni(),userReturn.getDni());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void testAddUserNull(){
        when(UserService.cryptWithMD5("123456")).thenReturn("123456");
        when(userRepository.save(user)).thenReturn(null);
        User u = userService.addUser(user);
        assertEquals(null,u);
    }
    @Test
    public void testUpdateCommonUserOk(){
        when(UserService.cryptWithMD5("123456")).thenReturn("123456");
        when(userRepository.updateCommonUser(userDto.getUser(), userDto.getPassword(), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity(),10)).thenReturn(10);
        Integer id = userService.updateCommonUser(userDto,10);
        assertEquals(id,new Integer(10));
        verify(userRepository, times(1)).updateCommonUser(userDto.getUser(), userDto.getPassword(), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity(),10);
    }
    @Test
    public void testUpdateCommonUserNull(){
        when(UserService.cryptWithMD5("123456")).thenReturn("123456");
        when(userRepository.updateCommonUser(userDto.getUser(), userDto.getPassword(), userDto.getName(), userDto.getLastname(), userDto.getDni(), userDto.getCity(),10)).thenReturn(null);
        Integer id = userService.updateCommonUser(userDto,10);
        assertEquals(null,id);
    }
    @Test
    public void testRemoveUser(){

        doNothing().when(userRepository).removeById(any());
        userService.removeUser(10);
        verify(userRepository, times(1)).removeById(10);
    }

    @Test
    public void testGetProfileOk() throws UserNotExistException {
        ProfileProyection profileProyection=new ProfileProyection() {
            @Override public String getUser() {return "mariano";}
            @Override public String getPassword() {return "123456";}
            @Override public String getName() {return "Mariano";}
            @Override public String getLastname() { return "Zanier"; }
            @Override public String getDni() { return "3333333"; }
            @Override public String getCity() { return"Mar del Plata"; }};
        when(userRepository.getProfile(101)).thenReturn(profileProyection);
        ProfileProyection pro = userService.getProfile(101);
        assertEquals(profileProyection,pro);
        verify(userRepository, times(1)).getProfile(101);
    }
    @Test(expected = UserNotExistException.class)
    public void testGetProfileUserNotExistException() throws UserNotExistException{
        when(userRepository.getProfile(101)).thenReturn(null);
        userService.getProfile(101);
    }

}
