package com.lifesaver.helpdesk.model;

import lombok.Data;

import javax.persistence.*;

//@Data
//@Entity
//@Table(name = "cat_problemas")
public class CatProblemas {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "problema_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="depto_atn_id")
    private CatDeptosAtencion deptoAtn;

    private String problemaText;




}
