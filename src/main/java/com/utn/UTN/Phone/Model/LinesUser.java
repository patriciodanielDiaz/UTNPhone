package com.utn.UTN.Phone.Model;

import com.sun.javafx.beans.IDProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "lines_users")
public class LinesUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idline")
    private Integer idline;
    @Column(name = "linenumber")
    private String linenumber;
    @Column( name = "idtype")
    private Integer idtype;
    @Column(name = "iduser")
    private Integer iduser;


    @JoinColumn(name="idtype", insertable=false, updatable=false)

    @ManyToOne
    private LineType lineType;

}
