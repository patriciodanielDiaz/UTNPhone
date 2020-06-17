package com.utn.UTN.Phone.dto;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Serialization
public class CityDto {
    private Integer topPosition;
    private String cityName;
}


