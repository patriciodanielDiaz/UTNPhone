package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.LineTypeNotExistsException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.LineType;
import com.utn.UTN.Phone.model.Rate;
import com.utn.UTN.Phone.repository.LineTypeRepository;
import com.utn.UTN.Phone.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LineTypeServiceTest {
    LineTypeService lineTypeService;
    @Mock
    LineTypeRepository lineTypeRepository;

    @Before
    public void setUp() {
        initMocks(this);
        lineTypeService= new LineTypeService(lineTypeRepository);
    }

    @Test
    public void testGetByTypeOk() throws LineTypeNotExistsException {

        LineType lineType=new LineType(1,"celular",null,null);
        when(lineTypeRepository.findByType("celular")).thenReturn(lineType);
        LineType lReturn = lineTypeService.getByType("celular");

        assertEquals(lineType,lReturn);
        verify(lineTypeRepository, times(1)).findByType("celular");
    }
    @Test(expected = LineTypeNotExistsException.class)
    public void testGetByTypeRecordNotExistsException() throws LineTypeNotExistsException{
        when(lineTypeRepository.findByType("celular")).thenReturn(null);
        lineTypeService.getByType("celular");
    }
}
