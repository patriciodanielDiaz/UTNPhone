package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.config.RestUtil;
import com.utn.UTN.Phone.controller.clientController.CallController;
import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.dto.CityDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.*;
import com.utn.UTN.Phone.service.CallService;
import com.utn.UTN.Phone.service.CityService;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.UserService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RestUtil.class)
public class CallBackofficeControllerTest {
    @Mock
    RestUtil restUtil;
    @Mock
    CallService callService;
    @Mock
    LineService lineService;
    @Mock
    UserService userService;
    @Mock
    SessionManager sessionManager;

    CallBackofficeController callBackofficeController;
    Line lineOrigin;
    Line lineDest;
    City city1;
    City city2;
    Call call;
    User user;
    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(RestUtil.class);

        callBackofficeController = new CallBackofficeController(lineService,userService,callService,sessionManager);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        lineDest=new Line(2,"12345",lineType,user,null,null,null);
        city1=new City(1,null,"Mar del plata",223,null,null);
        city2=new City(2,null,"Mar del plata",223,null,null);
        call= new Call(10,lineOrigin,lineDest,city1,city2,null,null,null,null,null,null,null,null);

    }

    @Test
    public void testGetCallByUserDateOk() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException{

        List<Call> calls=new ArrayList<>();
        calls.add(call);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-07-07");
        when(userService.findByDni("123456")).thenReturn(user);
        when(callService.getCallsByDate(lineOrigin,fromDate,toDate)).thenReturn(calls);

        ResponseEntity re = callBackofficeController.getCallByUser("123456","123456","123456","20-01-01","20-07-07");
        assertEquals(ResponseEntity.ok(calls), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
        verify(callService, times(1)).getCallsByDate(lineOrigin,fromDate,toDate);

    }
    @Test(expected = UserNotExistException.class)
    public void testGetCallByUserDateUserNotExistException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(userService.findByDni("123456")).thenReturn(null);
        callBackofficeController.getCallByUser("123456","123456","123456","20-01-01","20-07-07");
        verify(userService, times(1)).findByDni("123456");

    }
    @Test(expected = LineNotExistsException.class)
    public void testGetCallByUserDateLineNotExistsException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenThrow(new LineNotExistsException());
        callBackofficeController.getCallByUser("123456","123456","123456","20-01-01","20-07-07");
        verify(userService, times(1)).findByDni("123456");
        verify(lineService, times(1)).getLineByNumber("123456");

    }
    @Test
    public void testGetCallByUserOk() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException{

        List<Call> calls=new ArrayList<>();
        calls.add(call);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        when(userService.findByDni("123456")).thenReturn(user);
        when(callService.getCallsByLine(lineOrigin)).thenReturn(calls);

        ResponseEntity re = callBackofficeController.getCallByUser("123456","123456","123456",null,null);
        assertEquals(ResponseEntity.ok(calls), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(userService, times(1)).findByDni("123456");
        verify(callService, times(1)).getCallsByLine(lineOrigin);

    }

    @Test
    public void testAddCallOk() throws LineNotExistsException,RecordNotExistsException, UserNotExistException{

        CallDto callDto=new CallDto("123456","654321",null,null);

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        when(lineService.getLineByNumber("654321")).thenReturn( lineOrigin);
        when(callService.addCall(lineOrigin,lineOrigin,null,null)).thenReturn(call);
        Mockito.when(RestUtil.getLocation(10)).thenReturn(URI.create("miUri.com"));

        ResponseEntity re = callBackofficeController.addCall("123456",callDto);
        List<String> headers = re.getHeaders().get("location");
        Assert.assertEquals(headers.get(0), "miUri.com");

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).addCall(lineOrigin,lineOrigin,null,null);

    }

    @Test(expected = LineNotExistsException.class)
    public void testAddCallLineNotExistsException() throws LineNotExistsException{

        CallDto callDto=new CallDto("123456","654321",null,null);

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenThrow(new LineNotExistsException());
        when(lineService.getLineByNumber("654321")).thenThrow(new LineNotExistsException());

        callBackofficeController.addCall("123456",callDto);

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }
}
