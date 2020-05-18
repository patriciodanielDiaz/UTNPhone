package com.utn.UTN.Phone.Model;

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
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idcity")
    @JsonBackReference(value="userCity")
    private City city;

    @OneToMany(mappedBy = "user")
    private List<Line> lines;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

    //agregar campo baja logica

}
