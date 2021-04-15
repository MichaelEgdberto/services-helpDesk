package com.lifesaver.helpdesk.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "operadores")
public class Operadores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operador_id")
    private Long id;

    @Column(name = "usuario", unique = true)
    private String usuario;

    @Column(name = "password")
    private String password;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "correo")
    private String correo;

    @Column(name= "telefono")
    private String telefono;

    @Column(name= "organizacion")
    private String organizacion;

    @Column(name = "activo")
    private Boolean activo=true;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "oper_role", joinColumns = @JoinColumn(name = "operador_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Role> role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "oper_depto", joinColumns = @JoinColumn(name = "operador_id"), inverseJoinColumns = @JoinColumn(name = "depto_id"))
    private Set<CatDeptosAtencion> deptos;

    @Column(name="fecha_creado", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date fechaCreado;

    @Column(name="fecha_modificado", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date fechaModificado;

}
