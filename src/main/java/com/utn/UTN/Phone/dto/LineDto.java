package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.Call;
import com.utn.UTN.Phone.model.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LineDto {
    String number;
    String lineType;
    String available;

    public static List<LineDto> transferToLineDto(List<Line> lines){

        List<LineDto> linesDto=new ArrayList<>();

        for (Line l:lines) {
            LineDto lineDto =new LineDto();

            lineDto.setNumber(l.getLinenumber());
            lineDto.setLineType(l.getLineType().getType());
            lineDto.setAvailable((l.getIsAvailable()==true)? "Disponible" :"Fuera de servicio");
            linesDto.add(lineDto);
        }
        return linesDto;
    }
}
