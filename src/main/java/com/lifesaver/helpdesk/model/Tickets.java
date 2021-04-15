package com.lifesaver.helpdesk.model;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Data
@Entity
@Table(name = "tickets")
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Operadores usuario;

    @ManyToOne
    @JoinColumn(name="depto_atn_id")
    private CatDeptosAtencion deptoAtn;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ticket_contacto", joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "cat_contacto_id"))
    private Set<CatContacto> contacto;

//    @Column(name = "telefono_contacto")
//    private String telefonoContacto;

    @Column(name = "descripcion_problema", columnDefinition = "TEXT")
    private String descripcionProblema;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CatStatus status;

    @Column(name = "recibe_equipo")
    private Boolean recibeEquipo=false;

    @Column(name="fecha_creado", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date fechaCreado;

    @Column(name="fecha_modificado", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date fechaModificado;

    @Column(name = "fecha_finalizado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinalizado;






}
