package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.*;
import com.utn.UTN.Phone.repository.CallRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.utn.UTN.Phone.model.User.Type.empleado;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CallServiceTest {
    CallService callService;
    @Mock
    CallRepository callRepository;
    User user;
    Line lineOrigin;
    Line lineDest;
    City city1;
    City city2;
    Call call;
    @Before
    public void setUp() {
        initMocks(this);
        callService= new CallService(callRepository);
        user = new User(1,"mariano", "123456", "bbbb", "cccc","12345",empleado,null,null,null,null);        LineType lineType=new LineType(1,"celular",null,null);
        lineOrigin=new Line(1,"123456",lineType,user,null,null,null);
        lineDest=new Line(2,"123456",lineType,user,null,null,null);
        city1=new City(1,null,"Mar del plata",223,null,null);
        city2=new City(2,null,"Mar del plata",223,null,null);
        call= new Call(10,lineOrigin,lineDest,city1,city2,null,null,null,null,null,null,null,null);

    }

    @Test
    public void testGetCallsByLineOk() throws RecordNotExistsException, ParseException {

        List<Call> calls=new ArrayList<>();
        calls.add(call);

        when(callRepository.findAllByOriginCall(lineOrigin)).thenReturn(calls);
        List<Call> cReturn = callService.getCallsByLine(lineOrigin);
        assertEquals(calls,cReturn);
        verify(callRepository, times(1)).findAllByOriginCall(lineOrigin);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetCallsByLineRecordNotExistsException() throws RecordNotExistsException, ParseException {

        when(callRepository.findAllByOriginCall(lineOrigin)).thenReturn(null);
        callService.getCallsByLine(lineOrigin);
    }

    @Test
    public void testGetCallsByDateOk() throws RecordNotExistsException, ParseException {

        List<Call> calls=new ArrayList<>();
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-01");
        calls.add(call);

        when(callRepository.getCallsByDate(1,fromDate,toDate)).thenReturn(calls);
        List<Call> cReturn = callService.getCallsByDate(lineOrigin,fromDate,toDate);
        assertEquals(calls,cReturn);
        verify(callRepository, times(1)).getCallsByDate(1,fromDate,toDate);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetCallsByDateRecordNotExistsException() throws RecordNotExistsException, ParseException {
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-07-01");
        when(callRepository.getCallsByDate(1,fromDate,toDate)).thenReturn(null);
        callService.getCallsByDate(lineOrigin,fromDate,toDate);
    }

    @Test
    public void testAddCallOk(){
        Call call2=new Call();
        when(callRepository.save(call2)).thenReturn(call2);
        Call cReturn=callService.addCall(null,null,null,null);
        assertEquals(call2,cReturn);
        verify(callRepository, times(1)).save(call2);
    }

    @Test
    public void testAddCallNull(){
        when(callRepository.save(call)).thenReturn(null);
        Call cReturn=callService.addCall(lineOrigin,lineDest,null,null);
        assertEquals(null,cReturn);
    }

}
