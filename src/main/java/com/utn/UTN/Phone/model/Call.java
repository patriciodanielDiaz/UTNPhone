package com.utn.UTN.Phone.model;

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

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "origincall")
    @JsonBackReference
    private Line originCall;

    public String getOriginNumber(){
        return originCall.getLinenumber();
    }

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "destinationcall")
    @JsonBackReference
    private Line destinationCall;

    public String getDestinationNumber(){
        return destinationCall.getLinenumber();
    }

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "origincity")
    @JsonBackReference
    private City originCity;

    public String getOriginCityName(){
        return originCity.getCity();
    }

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "destinationcity")
    @JsonBackReference
    private City destinationCity;

    public String getDestinationCityName(){
        return destinationCity.getCity();
    }

    private Time durationtime;

    private Float price;

    @Column(name = "costprice")
    private Float costPrice;

    private Double total;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "idinvoice")
    @JsonBackReference
    private Invoice idInvoice;

    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "idrate")
    @JsonBackReference
    private Rate rate;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
