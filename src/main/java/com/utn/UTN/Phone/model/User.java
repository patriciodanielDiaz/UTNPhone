package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    private String user;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String lastname;

    @NotNull
    private String dni;

    //------------enum of type------------------------------------
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Provide status(empleado/cliente)")
    @Column(name = "usertype")
    private Type userType;

    public enum Type{
        empleado,cliente;
    }
    //----------------------------------------------------------

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idcity")
    @JsonBackReference(value="userCity")
    private City city;

    public Integer getCityId(){
        return city.getId();
    }

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Line> lines;

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;


}
