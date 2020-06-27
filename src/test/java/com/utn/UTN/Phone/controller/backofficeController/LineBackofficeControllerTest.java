package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.config.RestUtil;
import com.utn.UTN.Phone.dto.LineDto;
import com.utn.UTN.Phone.exceptions.*;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.service.LineService;
import com.utn.UTN.Phone.service.LineTypeService;
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
import java.util.ArrayList;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RestUtil.class)
public class LineBackofficeControllerTest {
    @Mock
    RestUtil restUtil;
    @Mock
    LineService lineService;
    @Mock
    UserService userService;
    @Mock
    LineTypeService lineTypeService;
    @Mock
    SessionManager sessionManager;

    LineType lineType;
    User user;
    Line lineOrigin;

    LineBackofficeController lineBackofficeController;
    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(RestUtil.class);
        lineBackofficeController=new LineBackofficeController(lineService,lineTypeService,userService,sessionManager);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        lineType =new LineType(1,"celular",null,null) ;
    }

    @Test
    public void testGetLineByUserDNIOk() throws RecordNotExistsException{

        List<Line> lines=new ArrayList<>();
        lines.add(lineOrigin);
        when(lineService.getLinesByUserDNI("123456")).thenReturn(lines);

        ResponseEntity re = lineBackofficeController.getLine("123456","123456");

        assertEquals(ResponseEntity.ok(lines), re);
        verify(lineService, times(1)).getLinesByUserDNI("123456");

    }

    @Test(expected = RecordNotExistsException.class)
    public void testGetLineByUserDNIRecordNotExistsException() throws RecordNotExistsException{

        when(lineService.getLinesByUserDNI("123456")).thenThrow(new RecordNotExistsException());

        lineBackofficeController.getLine("123456","123456");

        verify(lineService, times(1)).getLinesByUserDNI("123456");

    }

    @Test
    public void testGetLineOk() throws RecordNotExistsException{

        List<Line> lines=new ArrayList<>();
        lines.add(lineOrigin);
        when(lineService.getAll()).thenReturn(lines);

        ResponseEntity re = lineBackofficeController.getLine("123456",null);

        assertEquals(ResponseEntity.ok(lines), re);
        verify(lineService, times(1)).getAll();

    }

    @Test(expected = RecordNotExistsException.class)
    public void testGetLineRecordNotExistsException() throws RecordNotExistsException{

        when(lineService.getAll()).thenThrow(new RecordNotExistsException());

        lineBackofficeController.getLine("123456",null);

        verify(lineService, times(1)).getAll();

    }

    @Test
    public void testCreateLineOk() throws LineTypeNotExistsException, UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineTypeService.getByType("celular")).thenReturn(lineType);
        when(lineService.createLine(1,1)).thenReturn(1);
        Mockito.when(RestUtil.getLocation(1)).thenReturn(URI.create("miUri.com"));

        ResponseEntity re = lineBackofficeController.createLine("123456","123456","celular");
        List<String> headers = re.getHeaders().get("location");
        Assert.assertEquals(headers.get(0), "miUri.com");

        verify(userService, times(1)).findByDni("123456");
        verify(lineTypeService, times(1)).getByType("celular");
        verify(lineService, times(1)).createLine(1,1);

    }

    @Test(expected = UserNotExistException.class)
    public void testCreateLineUserNotExistException() throws LineTypeNotExistsException, UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(null);

        lineBackofficeController.createLine("123456","123456","celular");

        verify(userService, times(1)).findByDni("123456");

    }

    @Test(expected = LineTypeNotExistsException.class)
    public void testCreateLineLineTypeNotExistsException() throws LineTypeNotExistsException, UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineTypeService.getByType("celular")).thenThrow(new LineTypeNotExistsException());

        lineBackofficeController.createLine("123456","123456","celular");

        verify(userService, times(1)).findByDni("123456");
        verify(lineTypeService, times(1)).getByType("celular");

    }

    @Test(expected = LineNotExistsException.class)
    public void testCreateLineNotExistsException() throws LineTypeNotExistsException, UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineTypeService.getByType("celular")).thenReturn(lineType);
        when(lineService.createLine(1,1)).thenThrow(new LineNotExistsException());
        Mockito.when(RestUtil.getLocation(1)).thenReturn(URI.create("miUri.com"));

        lineBackofficeController.createLine("123456","123456","celular");

        verify(userService, times(1)).findByDni("123456");
        verify(lineTypeService, times(1)).getByType("celular");
        verify(lineService, times(1)).createLine(1,1);

    }

    @Test
    public void testDisabledLineOk() throws UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenReturn(lineOrigin);
        when(lineService.disabledLine(1)).thenReturn(1);

        ResponseEntity re = lineBackofficeController.disabledLine("123456","123456","123456");

        Assert.assertEquals(ResponseEntity.ok().build(), re);

        verify(userService, times(1)).findByDni("123456");
        verify(lineService, times(1)).getLineByNumber("123456");
        verify(lineService, times(1)).disabledLine(1);

    }

    @Test(expected = UserNotExistException.class)
    public void testDisabledLineUserNotExistException() throws UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(null);

        lineBackofficeController.disabledLine("123456","123456","123456");

        verify(userService, times(1)).findByDni("123456");

    }

    @Test(expected = LineNotExistsException.class)
    public void testDisabledLineLineNotExistsException() throws UserNotExistException, LineNotExistsException {

        when(userService.findByDni("123456")).thenReturn(user);
        when(lineService.getLineByNumber("123456")).thenThrow(new LineNotExistsException());

        lineBackofficeController.disabledLine("123456","123456","123456");

        verify(userService, times(1)).findByDni("123456");

    }
}
