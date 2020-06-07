package com.utn.UTN.Phone.repository;

import com.utn.UTN.Phone.model.Invoice;
import com.utn.UTN.Phone.proyection.InvoiceProyection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {


    @Query(value = "SELECT i.*" +
            "FROM invoices i \n" +
            "inner join calls c on i.idinvoice=c.idinvoice\n" +
            "inner join lines_users li on li.idline=c.origincall\n" +
            "where li.linenumber=?1 and i.create_at between ?2 and ?3 " +
            "ORDER BY i.state ASC",nativeQuery = true)
    List<Invoice> getInvoicesByDate(String lineNumber, Date fromDate, Date toDate);

    //creo que me va repetir facturas cuando tenga varias calls asociadas, faltaria un group by
    @Query(value = "SELECT i.*" +
            "FROM invoices i \n" +
            "inner join calls c on i.idinvoice=c.idinvoice\n" +
            "inner join lines_users li on li.idline=c.origincall\n" +
            "where li.linenumber=?1" +
            "ORDER BY i.state ASC", nativeQuery = true)
    List<Invoice> getInvoicesByNumber(String lineNumber);
}
