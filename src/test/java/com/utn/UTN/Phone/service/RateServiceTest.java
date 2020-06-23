package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.exceptions.UserNotExistException;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Rate;
import com.utn.UTN.Phone.model.User;
import com.utn.UTN.Phone.repository.RateRepository;
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

public class RateServiceTest {
    RateService rateService;
    @Mock
    RateRepository rateRepository;

    @Before
    public void setUp() {
        initMocks(this);
        rateService=new RateService(rateRepository);
    }

    @Test
    public void testGetAllOk() throws RecordNotExistsException {

        List<Rate> rates= new ArrayList<>();
        rates.add(new Rate(1,"local",12f,12f,(new City()),(new City()),null,null));
        when(rateRepository.findAll()).thenReturn(rates);
        List<Rate> rReturn = rateService.getAll();
        assertEquals(rates.size(),rReturn.size());
        verify(rateRepository, times(1)).findAll();
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetAllRecordNotExistsException() throws RecordNotExistsException{
        when(rateRepository.findAll()).thenReturn(null);
        rateService.getAll();
    }

    @Test
    public void testGetByCitOk() throws RecordNotExistsException {

        List<Rate> rates= new ArrayList<>();
        rates.add(new Rate(1,"local",12f,12f,(new City()),(new City()),null,null));
        City city=new City(1,null,"Mar del plata",223,null,null);
        when(rateRepository.findByOriginCity(city)).thenReturn(rates);
        List<Rate> rReturn = rateService.getByCity(city);
        assertEquals(rates.size(),rReturn.size());
        verify(rateRepository, times(1)).findByOriginCity(city);
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetByCitRecordNotExistsException() throws RecordNotExistsException{
        City city=new City(1,null,"Mar del plata",223,null,null);
        when(rateRepository.findByOriginCity(city)).thenReturn(null);
        rateService.getByCity(city);
    }

}
