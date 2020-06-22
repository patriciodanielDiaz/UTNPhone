package com.utn.UTN.Phone.dto;

import com.utn.UTN.Phone.model.City;
import com.utn.UTN.Phone.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static List<InvoiceDto> transferToInvoicesDto(List<Invoice> invoices){

        List<InvoiceDto> invoicesDtos= new ArrayList<>();
        InvoiceDto tranferDto=new InvoiceDto();
       for (Invoice in:invoices) {

            tranferDto.setCode(in.getId());
            tranferDto.setNumber(in.getLine().getLinenumber());
            tranferDto.setName(in.getUser().getName());
            tranferDto.setLastName(in.getUser().getLastname());
            tranferDto.setDni(in.getUser().getDni());
            tranferDto.setTotal(in.getTotal());
            tranferDto.setState((in.getState()==true)? "Factura Pagada" :"Factura Impago");
            tranferDto.setExpirationDate(in.getExpiration());

            invoicesDtos.add(tranferDto);
        }
       return invoicesDtos;

    }

}
