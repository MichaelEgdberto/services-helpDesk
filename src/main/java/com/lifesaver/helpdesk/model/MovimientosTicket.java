package com.lifesaver.helpdesk.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "movimientos_tiket")
public class MovimientosTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_ticket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="ticket_id")
    private Tickets ticket;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @ManyToOne
    @JoinColumn(name = "operador_id")
    private Operadores operador;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CatStatus status;

    @Column(name="fecha_creado", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date fechaCreado;



}
