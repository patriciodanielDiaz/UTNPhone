package com.utn.UTN.Phone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorDto {
    @JsonProperty
    int code;
    @JsonProperty
    String description;
}
