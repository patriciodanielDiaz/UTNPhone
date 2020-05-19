package com.utn.UTN.Phone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lines_users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idline")
    private Integer id;

    @NotNull
    private String linenumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idtype")
    @JsonBackReference(value="lineLineType")
    private LineType lineType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    @JsonBackReference(value="lineUser")
    private User user;

    //no me sirve mostrar esto por ahora
    /*@OneToMany(mappedBy = "originCity" ,fetch = FetchType.LAZY)
    private List<Rate> rateOrigin;

    @OneToMany(mappedBy = "destinationCity", fetch = FetchType.LAZY)
    private List<Rate> rateDestination;*/

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;

}
