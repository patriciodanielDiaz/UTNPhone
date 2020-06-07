package com.utn.UTN.Phone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.sql.Time;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallDto {

    private String originNumber;

    private String destinationNumber;

    private Time duration;

    private Timestamp  dateTime;



    /*○ Número de origen
    *○ Número de destino
    *○ Duración de la llamada
    *○ Fecha y hora de la llamada
    * */
}
