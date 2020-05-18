package com.utn.UTN.Phone.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "iduser")
    @JsonBackReference
    private User user;

    @Column(name = "totalcalls")
    private Integer totalcalls;

    @Column(name = "total")
    private Float total;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "paymentdate")
    private Date paymentDate;

    @Column(name = "expiration")
    private Date expiration;

    @Column(name = "state")
    private Boolean state;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;


}
