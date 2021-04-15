package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TicketsDao extends JpaRepository<Tickets, Long> {

    List<Tickets> findByStatusOrderByFechaCreadoAsc(CatStatus status);

    Page<Tickets>  findByStatusOrderByFechaCreadoAsc(CatStatus status, Pageable pageable);

    Page<Tickets> findByUsuarioAndStatusOrderByFechaCreadoAsc(Operadores usuario, CatStatus status, Pageable pageable);

    List<Tickets> findByUsuarioAndStatusInOrderByFechaCreadoAsc(Operadores usuario, List<CatStatus> status);

    Page<Tickets> findByUsuarioAndStatusInOrderByFechaCreadoAsc(Operadores usuario, List<CatStatus> status, Pageable pageable);

    Page<Tickets> findByUsuarioAndStatusIn(Operadores usuario, List<CatStatus> status, Pageable pageable);

    Page<Tickets> findByDeptoAtnInAndStatusOrderByFechaCreadoAsc(Set<CatDeptosAtencion> deptosAtencion, CatStatus status, Pageable pageable);

    @Query(value = "SELECT  tk.status_id, st.indice, count(tk.ticket_id) " +
            "from tickets tk join cat_status st on st.status_id=tk.status_id " +
            "group by tk.status_id, st.indice", nativeQuery = true)
    List<Object[]> contadorSinFechaSinUsuario();

    @Query(value = "SELECT  tk.status_id, st.indice, count(tk.ticket_id) " +
            "from tickets tk join cat_status st on st.status_id=tk.status_id " +
            "where DATE(tk.fecha_creado) = DATE(current_date) " +
            "group by tk.status_id, st.indice", nativeQuery = true)
    List<Object[]> contadorHoySinUsuario();



}
