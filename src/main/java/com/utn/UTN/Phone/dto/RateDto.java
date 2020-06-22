package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.Rate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RateDto {
     Integer id;
     String origin;
     String destination;
     Float price;
     Float cost;

    public static List<RateDto> transferToRatesDto(List<Rate> rates){

        List<RateDto> ratesDtos= new ArrayList<>();
        RateDto tranferDto=new RateDto();
        for (Rate ra:rates) {

            tranferDto.setId(ra.getId());
            tranferDto.setOrigin(ra.getOriginCityName());
            tranferDto.setDestination(ra.getDestinationCityName());
            tranferDto.setPrice(ra.getPricexMin());
            tranferDto.setCost(ra.getCostPrice());

            ratesDtos.add(tranferDto);
        }
        return ratesDtos;
    }
}
