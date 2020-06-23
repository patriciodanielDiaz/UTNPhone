package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.dto.UserDto;
import com.utn.UTN.Phone.exceptions.LineNotExistsException;
import com.utn.UTN.Phone.exceptions.LineTypeNotExistsException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Line;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.LineRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LineServiceTest {

    LineService lineService;
    @Mock
    LineRepository lineRepository;
    Line line;
    User user ;
    LineType lineType;

    @Before
    public void setUp() {
        initMocks(this);
        lineService= new LineService(lineRepository);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);
        lineType=new LineType(1,"celular",null,null);
        line=new Line(1,"123456",lineType,user,null,null,null);

    }

    @Test
    public void testGetAllOk() throws  RecordNotExistsException {

        List<Line> lines=new ArrayList<>();
        lines.add(line);
        when(lineRepository.findAll()).thenReturn(lines);
        List<Line> lReturn = lineService.getAll();
        assertEquals(lines,lReturn);
        verify(lineRepository, times(1)).findAll();
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetAllRecordNotExistsException() throws RecordNotExistsException {
        when(lineRepository.findAll()).thenReturn(null);
        lineService.getAll();
    }

    @Test
    public void testGetLineByNumberOk() throws  LineNotExistsException {

        when(lineRepository.findByLinenumber("123456")).thenReturn(line);
        Line lReturn = lineService.getLineByNumber("123456");
        assertEquals(line,lReturn);
        verify(lineRepository, times(1)).findByLinenumber("123456");
    }
    @Test(expected = LineNotExistsException.class)
    public void testGetLineByNumberRecordNotExistsException() throws LineNotExistsException {
        when(lineRepository.findByLinenumber("123456")).thenReturn(null);
        lineService.getLineByNumber("123456");
    }

    @Test
    public void testGetLinesByUserDNIOk() throws  RecordNotExistsException {

        List<Line> lines=new ArrayList<>();
        lines.add(line);
        when(lineRepository.getLinesByUserDNI("123456")).thenReturn(lines);
        List<Line> lReturn = lineService.getLinesByUserDNI("123456");
        assertEquals(lines,lReturn);
        verify(lineRepository, times(1)).getLinesByUserDNI("123456");
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetLinesByUserDNIRecordNotExistsException() throws RecordNotExistsException {
        when(lineRepository.getLinesByUserDNI("123456")).thenReturn(null);
        lineService.getLinesByUserDNI("123456");
    }

    @Test
    public void testGetLinesByUserOk() throws  RecordNotExistsException {

        List<Line> lines=new ArrayList<>();
        lines.add(line);
        when(lineRepository.findAllByUser(user)).thenReturn(lines);
        List<Line> lReturn = lineService.getLinesByUser(user);
        assertEquals(lines,lReturn);
        verify(lineRepository, times(1)).findAllByUser(user);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetLinesByUserRecordNotExistsException() throws RecordNotExistsException {
        when(lineRepository.findAllByUser(user)).thenReturn(null);
        lineService.getLinesByUser(user);
    }

    @Test
    public void testCreateLineOk(){
        //UserDto userDto = new UserDto("mariano", "123456", "bbbb", "cccc","12345","Mar del Plata");
        when(lineRepository.createLine(10,2)).thenReturn(10);
        Integer id = lineService.createLine(10,2);
        assertEquals(id,new Integer(10));
        verify(lineRepository, times(1)).createLine(10,2);
    }
    @Test
    public void testCreateLineNull(){
        when(lineRepository.createLine(10,2)).thenReturn(null);
        Integer id = lineService.createLine(10,2);
        assertEquals(null,id);
    }

    @Test
    public void testDisabledLineOk(){
        when(lineRepository.disabledLine(10)).thenReturn(10);
        Integer id = lineService.disabledLine(10);
        assertEquals(id,new Integer(10));
        verify(lineRepository, times(1)).disabledLine(10);
    }
    @Test
    public void testDisabledLineNull(){
        when(lineRepository.disabledLine(10)).thenReturn(null);
        Integer id = lineService.disabledLine(10);
        assertEquals(null,id);
    }
}
