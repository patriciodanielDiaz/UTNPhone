package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "provinces")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Province {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @JoinColumn(name = "idcountry")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Country country;

    public String countryName(){
        return country.getCountry();
    }

    @NotNull
    private String province;

    /*@OneToMany(mappedBy = "province")
    private List<City> cities;*/

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
