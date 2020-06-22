package com.utn.UTN.Phone.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "linetypes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LineType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtype")
    private Integer id;

    @NotNull
    private String type;
/*
    @OneToMany(mappedBy = "lineType")
    private List<Line> lines;
*/
    @Column(name = "create_at")
    private String createAt;

    @Column(name = "update_at")
    private String updateAt;
}
