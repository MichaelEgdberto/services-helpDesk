package com.lifesaver.helpdesk.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cat_deptos_atencion")
public class CatDeptosAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depto_atn_id")
    private Long id;

    @Column(name = "departamento_nombre")
    private String departamentoNombre;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "activo", columnDefinition = "boolean default true")
    private Boolean activo = true;

}
