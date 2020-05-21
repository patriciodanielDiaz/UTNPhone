package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rates")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idrate")
    private Integer id;

    @NotNull
    private String type;

    @NotNull
    @Column(name = "pricexmin")
    private Float pricexMin;

    @NotNull
    @Column(name = "costprice")
    private Float costPrice;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "origincity")
    @JsonBackReference(value="rateOriginCity")
    private City originCity;

    public String getOriginCityName(){
        return originCity.getCity();
    }

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "destinationcity")
    @JsonBackReference(value="rateDestinationCity")
    private City destinationCity;

    public String getDestinationCityName(){
        return destinationCity.getCity();
    }

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;
}
