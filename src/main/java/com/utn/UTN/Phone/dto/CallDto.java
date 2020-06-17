package com.utn.UTN.Phone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallDto {

    private String originNumber;

    private String destinationNumber;

    private Time duration;

    private Timestamp dateTime;



    /*post:localhost:8080/backoffice/call/entry/
       {
       "originNumber": "5893239",
        "destinationNumber": "4841271",
        "duration":"00:01:30",
        "dateTime":"2020-06-04T16:23:04-03:00"
       }
    */
}
