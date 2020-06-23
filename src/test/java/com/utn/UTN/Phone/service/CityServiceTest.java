package com.utn.UTN.Phone.service;

import com.utn.UTN.Phone.exceptions.CityNotExistsException;
import com.utn.UTN.Phone.exceptions.RecordNotExistsException;
import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.repository.CityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CityServiceTest {
    CityService cityService;
    City city;
    @Mock
    CityRepository cityRepository;
    @Before
    public void setUp() {
        initMocks(this);
        cityService= new CityService(cityRepository);
        city=new City(1,null,"Mar del plata",223,null,null);
    }

    @Test
    public void testGetTopDestinationOk() throws RecordNotExistsException, ParseException {

        List<City> cities=new ArrayList<>();
        cities.add(city);

        when(cityRepository.getTopDestination("123456")).thenReturn(cities);
        List<City> cReturn = cityService.getTopDestination("123456");
        assertEquals(cities.size(),cReturn.size());
        assertEquals(cities,cReturn);
        verify(cityRepository, times(1)).getTopDestination("123456");
    }
    @Test(expected = RecordNotExistsException.class)
    public void testGetTopDestinationRecordNotExistsException() throws RecordNotExistsException, ParseException {

        when(cityRepository.getTopDestination("123456")).thenReturn(null);
        cityService.getTopDestination("123456");
    }

    @Test
    public void testGetCityByNameOk() throws CityNotExistsException {

        when(cityRepository.findByCity("Mar Del Plata")).thenReturn(city);
        City cReturn = cityService.getCityByName("Mar Del Plata");
        assertEquals(city.getId(),cReturn.getId());
        verify(cityRepository, times(1)).findByCity("Mar Del Plata");
    }
    @Test(expected = CityNotExistsException.class)
    public void testGetCityByNameRecordNotExistsException() throws CityNotExistsException {

        when(cityRepository.findByCity("Mar Del Plata")).thenReturn(null);
        cityService.getCityByName("Mar Del Plata");
    }

}
