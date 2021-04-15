package com.lifesaver.helpdesk.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cat_contacto")
public class CatContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_contacto_id")
    private Long id;

    private String contacto;



}
