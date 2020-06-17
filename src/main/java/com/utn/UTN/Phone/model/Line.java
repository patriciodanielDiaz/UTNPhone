package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "lines_users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@NamedStoredProcedureQuery(
        name = "sp_create_line",
        procedureName = "sp_create_line",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "pIdUser"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "pTypeLine"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "pidLine")
        }
)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idline")
    private Integer id;

    @NotNull
    private String linenumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idtype")
    @JsonBackReference(value="lineLineType")
    private LineType lineType;

    public String getType(){
        return lineType.getType();
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iduser")
    @JsonBackReference(value="lineUser")
    private User user;

    public Integer getUserId(){
        return user.getId();
    }

    //no me sirve mostrar esto por ahora
    /*@OneToMany(mappedBy = "originCity" ,fetch = FetchType.LAZY)
    private List<Rate> rateOrigin;

    @OneToMany(mappedBy = "destinationCity", fetch = FetchType.LAZY)
    private List<Rate> rateDestination;
    */
    @OneToMany(mappedBy = "line")
    private List<Invoice> invoices;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
