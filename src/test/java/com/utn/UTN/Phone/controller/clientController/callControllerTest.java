package com.utn.UTN.Phone.controller.clientController;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CallDto.class,CityDto.class})

public class callControllerTest {
    @Mock
    CallService callService;
    @Mock
    LineService lineService;
    @Mock
    UserService userService;
    @Mock
    CityService cityService;
    @Mock
    SessionManager sessionManager;
    @Mock
    CallDto callDto;
    @Mock
    CityDto cityDto;

    CallController callController;
    Line lineOrigin;
    Line lineDest;
    City city1;
    City city2;
    Call call;
    User user;
    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(CallDto.class);
        PowerMockito.mockStatic(CityDto.class);
        callController = new CallController(callService,lineService,cityService,sessionManager,userService);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        lineDest=new Line(2,"12345",lineType,user,null,null,null);
        city1=new City(1,null,"Mar del plata",223,null,null);
        city2=new City(2,null,"Mar del plata",223,null,null);
        call= new Call(10,lineOrigin,lineDest,city1,city2,null,null,null,null,null,null,null,null);

    }

    @Test
    public void testGetCallByUserDate() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException, PermissionDeniedException {

        List<Call> calls=new ArrayList<>();
        calls.add(call);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-07-07");
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(callService.getCallsByDate(lineOrigin,fromDate,toDate)).thenReturn(calls);
        when(CallDto.transferToCallDto(calls)).thenReturn(new ArrayList<CallDto>());

        ResponseEntity re = callController.getCallByUser("123456","123456","20-01-01","20-07-07");
        assertEquals(ResponseEntity.ok(new ArrayList<CallDto>()), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).getCallsByDate(lineOrigin,fromDate,toDate);

    }

    @Test(expected = UserNotExistException.class)
    public void testGetCallByUserDateUserNotExistException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(sessionManager.getCurrentUser("123456")).thenReturn(null);
        callController.getCallByUser("123456","123456","20-01-01","20-07-07");
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }
    @Test(expected = PermissionDeniedException.class)
    public void testGetCallByUserDatePermissionDeniedException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        User user2 = new User(2,"Patricio", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);

        Mockito.when(sessionManager.getCurrentUser("123456")).thenReturn(user2);
        Mockito.when(lineService.getLineByNumber("123456")).thenReturn( lineDest);

        callController.getCallByUser("123456","123456","20-01-01","20-07-07");

        verify(lineService, times(1)).getLineByNumber(lineDest.getLinenumber());
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }
    @Test(expected = LineNotExistsException.class)
    public void testGetCallByUserDateLineNotExistsException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        Mockito.when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        Mockito.when(lineService.getLineByNumber("123456")).thenThrow(new LineNotExistsException());
        callController.getCallByUser("123456","123456","20-01-01","20-07-07");

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetCallByUserDateRecordNotExistsException() throws PermissionDeniedException, UserNotExistException, ParseException, LineNotExistsException, RecordNotExistsException {

        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("20-07-07");
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(callService.getCallsByDate(lineOrigin,fromDate,toDate)).thenThrow(new RecordNotExistsException());

        callController.getCallByUser("123456","123456","20-01-01","20-07-07");

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).getCallsByDate(lineOrigin,fromDate,toDate);

    }

        @Test
    public void TestGetCallByUser() throws LineNotExistsException, PermissionDeniedException, ParseException, RecordNotExistsException, UserNotExistException {

        List<Call> calls=new ArrayList<>();
        calls.add(call);
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        when(callService.getCallsByLine(lineOrigin)).thenReturn(calls);
        when(CallDto.transferToCallDto(calls)).thenReturn(new ArrayList<CallDto>());
        ResponseEntity re = callController.getCallByUser("123456", "123456",null,null);

        assertEquals(ResponseEntity.ok(new ArrayList<CallDto>()), re);
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).getCallsByLine(lineOrigin);

        }

    @Test(expected = RecordNotExistsException.class)
    public void testGetCallByUserRecordNotExistsException() throws LineNotExistsException, PermissionDeniedException, ParseException, RecordNotExistsException, UserNotExistException {

        List<Call> calls=new ArrayList<>();
        calls.add(call);
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        when(callService.getCallsByLine(lineOrigin)).thenThrow(new RecordNotExistsException());

        callController.getCallByUser("123456", "123456",null,null);

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).getCallsByLine(lineOrigin);

    }

        public void testGetDestinationTop() throws LineNotExistsException, PermissionDeniedException, RecordNotExistsException, UserNotExistException {

        List<City> cities=new ArrayList<>();cities.add(city1);
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn( lineOrigin);
        when(cityService.getTopDestination("123456")).thenReturn(cities);
        when(CityDto.transferToCityDto(cities)).thenReturn(new ArrayList<CityDto>());

        ResponseEntity re=callController.getDestinationsTop("123456", "123456");
        assertEquals(ResponseEntity.ok(new ArrayList<CityDto>()), re);

        verify(lineService, times(1)).getLineByNumber("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(callService, times(1)).getCallsByLine(lineOrigin);

    }


}
