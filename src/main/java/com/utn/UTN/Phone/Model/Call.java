package com.utn.UTN.Phone.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "calls")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcall")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "origincall")
    @JsonBackReference
    private Line originCall;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "destinationcall")
    @JsonBackReference
    private Line destinationCall;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "origincity")
    @JsonBackReference
    private Line originCity;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "destinationcity")
    @JsonBackReference
    private Line destinationCity;

    private Time durationtime;

    private float price;

    @Column(name = "costprice")
    private float costPrice;

    private Double total;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "idinvoice")
    @JsonBackReference
    private Invoice idInvoice;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "idrate")
    @JsonBackReference
    private Rate rate;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
