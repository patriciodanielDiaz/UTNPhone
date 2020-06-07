package com.utn.UTN.Phone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceDto {
    Integer code;
    String number;
    String name;
    String lastName;
    String dni;
    Float total;
    String state;
    Date expirationDate;
}
