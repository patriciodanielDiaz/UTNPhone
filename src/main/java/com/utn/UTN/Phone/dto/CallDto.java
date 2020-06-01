package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.Line;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallDto {

    private String originNumber;

    private String destinationNumber;

    private Time durationtime;

    /*○ Número de origen
    **○ Número de destino
    **○ Duración de la llamada
    * */
}
