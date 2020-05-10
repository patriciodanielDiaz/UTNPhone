package com.utn.UTN.Phone.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="cities")
public class City
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
    private Integer id;
    @Column(name="city")
    private String city;
    @Column(name="idprovince")
    private Integer idprovince;
    @Column(name = "prefix")
    private String prefix;



    @JoinColumn(name = "idprovince", unique = true,insertable=false, updatable=false)

    @OneToOne(cascade = CascadeType.ALL)

    private Province provincename;
}
