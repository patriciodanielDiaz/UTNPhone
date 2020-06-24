package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.Call;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
