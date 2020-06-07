package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "cities")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @JoinColumn(name = "idprovince")
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private Province province;

    public String provinceName(){
        return province.getProvince();
    }

    @NotNull
    private String city;

    @NotNull
    private int prefix;

    /*@OneToMany(mappedBy = "city")
    private List<User> users;*/

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
