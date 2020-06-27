package com.utn.UTN.Phone.controller.clientController;

import com.utn.UTN.Phone.dto.CallDto;
import com.utn.UTN.Phone.dto.CityDto;
import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.PermissionDeniedException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.session.SessionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
@PrepareForTest(LineDto.class)
public class LineControllerTest {
    @Mock
    LineService lineService;
    @Mock
    SessionManager sessionManager;
    @Mock
    LineDto lineDto;
    LineController lineController;
    User user;
    Line lineOrigin;
    @Before
    public void setUp(){
        initMocks(this);
        PowerMockito.mockStatic(LineDto.class);
        lineController=new LineController(lineService,sessionManager);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);

    }

    @Test
    public void testGetUserLinesOk() throws LineNotExistsException, ParseException, RecordNotExistsException, UserNotExistException, PermissionDeniedException {

        List<Line> lines=new ArrayList<>();
        lines.add(lineOrigin);
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLinesByUser(user)).thenReturn(lines);
        when(LineDto.transferToLineDto(lines)).thenReturn(new ArrayList<LineDto>());

        ResponseEntity re = lineController.getUserLines("123456");
        assertEquals(ResponseEntity.ok(new ArrayList<LineDto>()), re);

        verify(lineService, times(1)).getLinesByUser(user);
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }
    @Test
    public void testGetUserLines() throws RecordNotExistsException, UserNotExistException{

        List<Line> lines=new ArrayList<>();
        //lines.add(lineOrigin);
        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLinesByUser(user)).thenReturn(lines);
        when(LineDto.transferToLineDto(lines)).thenReturn(new ArrayList<LineDto>());

        ResponseEntity re = lineController.getUserLines("123456");
        assertEquals(ResponseEntity.status(HttpStatus.NO_CONTENT).build(), re);

        verify(lineService, times(1)).getLinesByUser(user);
        verify(sessionManager, times(1)).getCurrentUser("123456");

    }

    @Test(expected = UserNotExistException.class)
    public void testGetUserLinesUserNotExistException() throws  UserNotExistException, RecordNotExistsException {
        when(sessionManager.getCurrentUser("123456")).thenReturn(null);
        lineController.getUserLines("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
    }

    @Test(expected = RecordNotExistsException.class)
    public void testGetUserLinesDateLineNotExistsException() throws UserNotExistException, RecordNotExistsException {

        when(sessionManager.getCurrentUser("123456")).thenReturn(user);
        when(lineService.getLinesByUser(user)).thenThrow(new RecordNotExistsException());
        lineController.getUserLines("123456");
        verify(sessionManager, times(1)).getCurrentUser("123456");
        verify(lineService, times(1)).getLinesByUser(user);

    }

}
