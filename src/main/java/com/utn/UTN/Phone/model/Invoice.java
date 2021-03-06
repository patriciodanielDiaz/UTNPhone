package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinvoice")
    private Integer id;

    @NotNull
    @JoinColumn(name = "iduser")
    @ManyToOne(fetch = FetchType.EAGER )
    @JsonBackReference(value="invoiceUser")
    private User user;

    public Integer getUserId(){
        return user.getId();
    }

    @NotNull
    @JoinColumn(name = "idline")
    @ManyToOne(fetch = FetchType.EAGER )
    @JsonBackReference(value="invoiceLine")
    private Line line;

    public Integer getLineId(){
        return line.getId();
    }

    @Column(name = "total_calls")
    private Integer totalCalls;

    @Column(name = "total")
    private Float total;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "date_issued")
    private Date dateIssued;

    @Column(name = "expiration")
    @Temporal(TemporalType.DATE)
    private Date expiration;

    @Column(name = "state")
    private Boolean state;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "date_index")
    private Date dateIndex;

    @Column(name = "update_at")
    private String updateAt;


}
