package com.lifesaver.helpdesk.managerImpl;

import com.lifesaver.helpdesk.dao.CatStatusDao;
import com.lifesaver.helpdesk.dao.MovimientosTicketsDao;
import com.lifesaver.helpdesk.dao.TicketsDao;
import com.lifesaver.helpdesk.dao.UsuariosDao;
import com.lifesaver.helpdesk.manager.TicketManager;
import com.lifesaver.helpdesk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("TicketManager")
public class TicketManagerImpl implements TicketManager {

    @Autowired
    TicketsDao ticketsDao;

    @Autowired
    MovimientosTicketsDao mvTDao;

    @Autowired
    CatStatusDao catStatusDao;

    @Autowired
    UsuariosDao usuariosDao;

    @Override
    public void guardaTikcet(Tickets tickets) {

        tickets.setStatus(catStatusDao.findByIndice(0));

        ticketsDao.save(tickets);
    }

    @Override
    public void guardaRevision(Tickets tickets, MovimientosTicket movTikets) {



    }

    @Override
    public List<Tickets> traeTicketsPorEstado(Long status) {
        return ticketsDao.findByStatusOrderByFechaCreadoAsc(catStatusDao.getOne(status));
    }

    @Override
    public Page<Tickets> traeTicketsPorEstado(Operadores operadores, Long status, Integer pagina) {

        return ticketsDao.findByUsuarioAndStatusOrderByFechaCreadoAsc(operadores, catStatusDao.getOne(status), PageRequest.of(pagina,10,Sort.by("fechaCreado").ascending()));
    }

    @Override
    public Page<Tickets> traeTicketsEstatus(Long status, Integer pagina) {
        return ticketsDao.findByStatusOrderByFechaCreadoAsc(catStatusDao.getOne(status),PageRequest.of(pagina,10,Sort.by("fechaCreado").ascending()));
    }

    @Override
    public List<MovimientosTicket> movimientosTicket(Long idt) {
        return mvTDao.findByTicketOrderByFechaCreadoDesc(ticketsDao.getOne(idt));
    }

    @Override
    public Page<Tickets> traeTicketsEstatus(Operadores operadores, Long status, Integer pagina) {



        if(operadores.getDeptos()!=null && operadores.getDeptos().isEmpty()){
            return ticketsDao.findByStatusOrderByFechaCreadoAsc(catStatusDao.getOne(status), PageRequest.of(pagina,10,Sort.by("fechaCreado").ascending()));
        }else{
            return ticketsDao.findByDeptoAtnInAndStatusOrderByFechaCreadoAsc(operadores.getDeptos(),catStatusDao.getOne(status),PageRequest.of(pagina,10,Sort.by("fechaCreado").ascending()));
        }

    }

    @Override
    public Page<MovimientosTicket> movimientosTicket(Long idt, Integer pagina) {
        return mvTDao.findByTicketOrderByFechaCreadoDesc(ticketsDao.getOne(idt),PageRequest.of(pagina,5,Sort.by("fechaCreado").descending()));
    }

    @Override
    public ContadoresTickets contadores( List<Object[]> lsContadores) {

        Integer inicial = 0;
        Integer intermedio = 0;
        Integer finalizado = 0;

        for(Object[] obj : lsContadores){
            Integer indice = new Integer(obj[1].toString());
            if(indice==0){
                inicial += new Integer(obj[2].toString());
            }
            if(indice>0 && indice<10){
                intermedio+= new Integer(obj[2].toString());
            }
            if(indice==10){
                finalizado += new Integer(obj[2].toString());
            }


        }

        ContadoresTickets cnt = new ContadoresTickets();
        cnt.setEnProceso(intermedio);
        cnt.setFinalizados(finalizado);
        cnt.setNuevos(inicial);

        return cnt;
    }

    @Override
    public Page<Tickets> traeTicketsPorEstado(Operadores operadores, List<CatStatus> status, Integer pagina) {

        return ticketsDao.findByUsuarioAndStatusIn(operadores,status,PageRequest.of(pagina,10,Sort.by("fechaModificado").descending()));
    }

    @Override
    public void guardaMovimientos(MovimientosTicket mvt, Tickets tickets) {

        tickets.setFechaModificado(new Date());
        ticketsDao.save(tickets);
        mvTDao.save(mvt);

    }
}
