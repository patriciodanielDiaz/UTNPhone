package com.utn.UTN.Phone.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="users")
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
    private Integer id;
    @Column(name ="user")
    private String user;
    @Column(name="password")
    private String password;
    @Column(name="name")
    private String name;
    @Column(name ="lastname")
    private String lastname;
    @Column(name = "dni")
    private String dni;
    @Column(name="idcity")
    private Integer idcity;
    @Column(name = "usertype")
    private String usertype;



    @JoinColumn(name = "idcity", unique = true,insertable=false, updatable=false)

    @OneToOne(cascade = CascadeType.ALL)

    private City city_name;
}
