package com.utn.UTN.Phone.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="Customers")
public class Customer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name ="lastname")
    private String lastname;
    @Column(name = "dni")
    private String dni;
    @Column(name="idcity")
    private Integer idcity;
    @Column(name = "iduser")
    private Integer iduser;



    //@JoinColumn()
}
