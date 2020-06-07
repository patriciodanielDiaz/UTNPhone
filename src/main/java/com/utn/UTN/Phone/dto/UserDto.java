package com.utn.UTN.Phone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private String user;

    private String password;

    private String name;

    private String lastname;

    private String dni;

    private String city;
}
