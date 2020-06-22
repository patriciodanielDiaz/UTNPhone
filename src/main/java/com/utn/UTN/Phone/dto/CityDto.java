package com.utn.UTN.Phone.dto;

import com.sun.xml.internal.ws.developer.Serialization;
import com.utn.UTN.Phone.model.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Serialization
public class CityDto {
    private Integer topPosition;
    private String cityName;

    public static List<CityDto> transferToCityDto(List<City> cities){

        List<CityDto> citiesDto=new ArrayList<>();
        Integer top=1;
        for (City c:cities) {
            CityDto cityDto =new CityDto();
            cityDto.setTopPosition(top);
            cityDto.setCityName(c.getCity());
            citiesDto.add(cityDto);
            top=top+1;
        }
        return citiesDto;
    }
}


