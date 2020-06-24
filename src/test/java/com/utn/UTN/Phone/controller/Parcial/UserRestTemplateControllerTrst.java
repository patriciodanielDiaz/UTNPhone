package com.utn.UTN.Phone.controller.Parcial;

import com.utn.UTN.Phone.controller.UserRestTemplateController;
import com.utn.UTN.Phone.controller.clientController.UserController;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.*;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserRestTemplateControllerTrst {

    @Mock
    CallService callService;
    @Mock
    UserService userService;
    UserRestTemplateController userRestTemplateController;

    @Before
    public void setUp() throws UserNotExistException {
        initMocks(this);
        userRestTemplateController = new UserRestTemplateController(userService, callService);

    }

    @Test
    public void getCallsmallTest () throws RecordNotExistsException {

        User user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);
        LineType lineType=new LineType(1,"celular",null,null);
        Line lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        Line lineDest=new Line(2,"123456",lineType,user,null,null,null);
        City city1=new City(1,null,"Mar del plata",223,null,null);
        City city2=new City(2,null,"Mar del plata",223,null,null);
        Call call= new Call(10,lineOrigin,lineDest,city1,city2,null,null,null,null,null,null,null,null);


        when(callService.getCallSmall()).thenReturn(call);
        when(userService.getUserByNum("123456")).thenReturn(user);
        ResponseEntity re=userRestTemplateController.getCallsmall();


        assertEquals( ResponseEntity.ok(user),re);
        verify(callService, times(1)).getCallSmall();
        verify(userService, times(1)).getUserByNum("123456");

    }
    @Test(expected = RecordNotExistsException.class)
    public void testgetCallsmallRecordNotExistsException() throws RecordNotExistsException{


        when(callService.getCallSmall()).thenThrow(new RecordNotExistsException());
       userRestTemplateController.getCallsmall();


    }
}
