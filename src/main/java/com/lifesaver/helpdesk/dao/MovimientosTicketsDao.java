package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.MovimientosTicket;
import com.lifesaver.helpdesk.model.Tickets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientosTicketsDao extends JpaRepository<MovimientosTicket, Long> {

    List<MovimientosTicket> findByTicketOrderByFechaCreadoDesc(Tickets tickets);

    Page<MovimientosTicket> findByTicketOrderByFechaCreadoDesc(Tickets tickets, Pageable pageable);

}
