package com.lifesaver.helpdesk.manager;


import com.lifesaver.helpdesk.model.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketManager {

    void guardaTikcet(Tickets tickets);

    void guardaRevision(Tickets tickets, MovimientosTicket movTikets);

    List<Tickets>  traeTicketsPorEstado(Long status);

    Page<Tickets> traeTicketsPorEstado(Operadores operadores, Long status, Integer pagina);

    Page<Tickets> traeTicketsPorEstado(Operadores operadores, List<CatStatus> status, Integer pagina);

    Page<Tickets> traeTicketsEstatus(Long status, Integer pagina);

    Page<Tickets> traeTicketsEstatus(Operadores operadores, Long status, Integer pagina);

    List<MovimientosTicket> movimientosTicket(Long idt);

    Page<MovimientosTicket> movimientosTicket(Long idt, Integer pagina);

    void guardaMovimientos(MovimientosTicket mvt, Tickets tickets);

    ContadoresTickets contadores( List<Object[]> lsContadores );

}
