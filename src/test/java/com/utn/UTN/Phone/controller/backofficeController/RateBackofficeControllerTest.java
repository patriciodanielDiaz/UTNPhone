package com.utn.UTN.Phone.controller.backofficeController;

import com.utn.UTN.Phone.exceptions.CityNotExistsException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Country;
import com.utn.UTN.Phone.model.Province;
import com.utn.UTN.Phone.model.Rate;
import com.utn.UTN.Phone.service.CityService;
import com.utn.UTN.Phone.service.RateService;
import com.utn.UTN.Phone.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RateBackofficeControllerTest {

    @Mock
    RateService rateService;
    @Mock
    CityService cityService;
    RateBackofficeController rateBackofficeController;
    @Before
    public void setUp() {
        initMocks(this);
        rateBackofficeController=new RateBackofficeController(cityService,rateService);
    }

    @Test
    public void TestGetRatesOk() throws RecordNotExistsException {
        List<Rate> rates=new ArrayList(){};
        Rate rate =new Rate(1,"local",12f,12f,null,null,null,null);
        rates.add(rate);
        when(rateService.getAll()).thenReturn(rates);
        ResponseEntity re = rateBackofficeController.getRates("123456");

        assertEquals( ResponseEntity.ok(rates),re);
        verify(rateService, times(1)).getAll();
    }
    @Test(expected = RecordNotExistsException.class)
    public void TestGetRatesRecordNotExistsException() throws RecordNotExistsException {
        when(rateService.getAll()).thenThrow(new RecordNotExistsException());
        rateBackofficeController.getRates("123456");
        verify(rateService, times(1)).getAll();
    }

    @Test
    public void TestGetRatesByCityOk() throws RecordNotExistsException, CityNotExistsException {

        List<Rate> rates=new ArrayList(){};
        Rate rate =new Rate(1,"local",12f,12f,null,null,null,null);
        rates.add(rate);
        City city = new City(1,(new Province(1,(new Country(1,"Argentina","54",null,null)),"Buenos Aires",null,null)),"Mar del Plata",233,null,null);
        when(cityService.getCityByName("Mar del Plata")).thenReturn(city);
        when(rateService.getByCity(city)).thenReturn(rates);
        ResponseEntity re = rateBackofficeController.getRatesByCity("123456","Mar del Plata");

        assertEquals( ResponseEntity.ok(rates),re);
        verify(rateService, times(1)).getByCity(city);
        verify(cityService, times(1)).getCityByName("Mar del Plata");
    }
    @Test(expected = CityNotExistsException.class)
    public void TestGetRatesByCityCityNotExistsException() throws RecordNotExistsException, CityNotExistsException {

        when(cityService.getCityByName("Mar del Plata")).thenThrow(new CityNotExistsException());
        ResponseEntity re = rateBackofficeController.getRatesByCity("123456","Mar del Plata");

        verify(cityService, times(1)).getCityByName("Mar del Plata");
    }
    @Test(expected = RecordNotExistsException.class)
    public void TestGetRatesByCityRecordNotExistsException() throws RecordNotExistsException, CityNotExistsException {

        City city = new City(1,(new Province(1,(new Country(1,"Argentina","54",null,null)),"Buenos Aires",null,null)),"Mar del Plata",233,null,null);
        when(cityService.getCityByName("Mar del Plata")).thenReturn(city);
        when(rateService.getByCity(city)).thenThrow(new RecordNotExistsException());
        rateBackofficeController.getRatesByCity("123456","Mar del Plata");

        verify(rateService, times(1)).getByCity(city);
        verify(cityService, times(1)).getCityByName("Mar del Plata");
    }

}
