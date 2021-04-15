package com.lifesaver.helpdesk.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cat_status")
public class CatStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "indice", unique = true)
    private Integer indice;

    @Column(name = "publico")
    private Boolean publico=false;

    @Column(name = "activo")
    private Boolean activo=true;


}
