package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.Call;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallDto {

    private String originNumber;

    private String destinationNumber;

    private Time duration;

    private Timestamp dateTime;

    public static List<CallDto> transferToCallDto(List<Call> calls){

        List<CallDto> callsDto=new ArrayList<>();

        for (Call c:calls) {
            CallDto callDto =new CallDto();

            callDto.setOriginNumber(c.getOriginNumber());
            callDto.setDestinationNumber(c.getDestinationNumber());
            callDto.setDuration(c.getDurationtime());
            callDto.setDateTime(c.getCreateAt());

            callsDto.add(callDto);
        }
        return callsDto;
    }


    /*post:localhost:8080/backoffice/call/entry/
       {
       "originNumber": "5893239",
        "destinationNumber": "4841271",
        "duration":"00:01:30",
        "dateTime":"2020-06-04T16:23:04-03:00"
       }
    */
}
