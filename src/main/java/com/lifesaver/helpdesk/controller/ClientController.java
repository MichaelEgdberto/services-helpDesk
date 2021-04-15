package com.lifesaver.helpdesk.controller;

import com.lifesaver.helpdesk.dao.*;
import com.lifesaver.helpdesk.manager.OperadorManager;
import com.lifesaver.helpdesk.manager.TicketManager;
import com.lifesaver.helpdesk.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
//@SessionAttributes("usuario")
public class ClientController {

    @Autowired
    UsuariosDao usuariosDao;

    @Autowired
    CatDeptosAtnDao catDeptosAtnDao;

    @Autowired
    TicketManager ticketManager;

    @Autowired
    RoleDao roleDao;

    @Autowired
    OperadorDao operadorDao;


    @Autowired
    OperadorManager operadorManager;

    @Autowired
    CatStatusDao catStatusDao;

    @Autowired
    TicketsDao ticketsDao;

    @Autowired
    CatContactoDao catContactoDao;

//    @ModelAttribute("usuario")
//    public Usuarios setUpUsuariosForm(){
//        return new Usuarios();
//    }

//
//    @GetMapping("/")
//    public String getUserLogin(Model model){
//
//        model.addAttribute("usuarios",new Usuarios());
//
//        return "user/login_template_client";
//    }
//
//    @PostMapping("/")
//    public String postUserLogin(Model model, @RequestParam("email") String email) {
//
//        Usuarios user = usuariosDao.findByEmail(email);
//
//        if (user != null) {
//            model.addAttribute("usuario",user);
//            return "redirect:/perfil";
//        } else {
//            user = new Usuarios();
//            user.setEmail(email);
//            model.addAttribute("usuarios",user);
//            return "user/registro_template";
//        }
//    }

    private Model perfilModel(Model model,String page, Operadores usuario, Integer pageA,
                              Integer pageB, Integer pageC, Tickets tickets){


        if(tickets==null) {
            tickets = new Tickets();
            tickets.setUsuario(usuario);
            model.addAttribute("tickets", tickets);
        }else {
            tickets.setUsuario(usuario);
            model.addAttribute("tickets", tickets);
        }
        model.addAttribute("lsContacto",catContactoDao.findAll());
        model.addAttribute("page",page);
        model.addAttribute("deptosAtn", catDeptosAtnDao.findByActivoTrue());
        model.addAttribute("usuario", usuario);
        model.addAttribute("lsSolicitudesA", ticketManager.traeTicketsPorEstado(usuario,catStatusDao.findByIndice(0).getId(),pageA));
        model.addAttribute("lsSolicitudesB", ticketManager.traeTicketsPorEstado(usuario,catStatusDao.findByActivoTrueAndIndiceBetweenOrderByIndiceAsc(1,9),pageB));
        model.addAttribute("lsSolicitudesC", ticketManager.traeTicketsPorEstado(usuario,catStatusDao.findByIndice(10).getId(),pageC));
        return model;
    }

    @GetMapping("/client/perfil")
    public String getPerfil(Model model, @ModelAttribute("usuario") Operadores usuario
            , @RequestParam(name = "status",defaultValue = "0") Long status
            , @RequestParam(name = "pageA", defaultValue = "0") Integer pageA
            , @RequestParam(name = "pageB", defaultValue = "0") Integer pageB
            , @RequestParam(name = "pageC", defaultValue = "0") Integer pageC
            , @RequestParam(name = "show", defaultValue = "") String show){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());


            String page = "page";
            if(!show.equals("")){
                page = page + show.toUpperCase();
            }
            model = perfilModel(model, page,op, pageA, pageB, pageC, null);

            return "user/perfil_template";

    }

    @PostMapping("/registro")
    public String postRegistro(Model model, Usuarios usuarios){

        usuariosDao.save(usuarios);

        Usuarios us = usuariosDao.findByEmail(usuarios.getEmail());
        model.addAttribute("usuario",us);
        return "redirect:/perfil";

    }

    @PostMapping("/client/guardaSolicitud")
    public String postSolicitud(Model model, Tickets tickets,BindingResult bindingResult){

        System.out.println(tickets.toString());


        if(tickets.getUsuario()==null) {
            model = perfilModel(model, "",tickets.getUsuario(), 0,0,0, tickets);
            return "redirect:/client/perfil";
        }else if(tickets.getDescripcionProblema().equals("")){
            bindingResult.rejectValue("descripcionProblema", "","Se requiere una descripcion amplia del problema");
            model = perfilModel(model, "",tickets.getUsuario(), 0,0,0, tickets);

            return "user/perfil_template";
        }else if(tickets.getDeptoAtn()==null){
            bindingResult.rejectValue("deptosAtn", "","Seleccione una opcion");
            model = perfilModel(model, "",tickets.getUsuario(), 0,0,0, tickets);

            return "user/perfil_template";
        }else{

            ticketManager.guardaTikcet(tickets);
            model.addAttribute("usuario", tickets.getUsuario());

            return "redirect:/client/perfil";
        }
    }

    @GetMapping("/client/revisa")
    public String getRevisaUsr(
            Model model, @RequestParam(name = "idt",defaultValue = "0") Long idt ,
                               @RequestParam(name = "page", defaultValue = "0") Integer page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());
        if(idt!=0) {
            Tickets tk  = ticketsDao.getOne(idt);


                if(tk.getUsuario().getId().equals(op.getId())) {

                    tk.setFechaModificado(new Date());
                    ticketsDao.save(tk);
                    model.addAttribute("cancelUrl", "/client/perfil");
                    model.addAttribute("lsMov", ticketManager.movimientosTicket(idt, page));
                    model.addAttribute("lsStatus", catStatusDao.findByActivoTrueAndPublicoTrueOrderByIndiceAsc());
                    model.addAttribute("tk", tk);
                    model.addAttribute("usuario", op);
                    return "/manager/revisar_template";
                }else {
                    return "redirect:/client/perfil";
                }

        }else{
            System.out.println("se va por otro lado");
            return "redirect:/client/perfil";
        }
    }


    @PostMapping("/client/revisa")
    public String postRevisaUsuario(Model model, @RequestParam(name = "status") Long status,
                                    @RequestParam(name = "observacion") String observacion, Long idt){

        Tickets tk = ticketsDao.getOne(idt);

        tk.setStatus(catStatusDao.getOne(status));
        tk.setFechaModificado(new Date());
        MovimientosTicket mv = new MovimientosTicket();
        mv.setObservacion(observacion);
        mv.setStatus(catStatusDao.getOne(status));

        mv.setTicket(tk);

        ticketManager.guardaMovimientos(mv,tk);


        return "redirect:/perfil";
    }

    @GetMapping("/operador")
    public String getOperador(Model model){

        model.addAttribute("operadores",new Operadores());
        model.addAttribute("lsRoles", roleDao.findAll());

        return "user/creausuario";
    }

    @PostMapping("/operador")
    public String postOperador(Operadores operadores){

        operadorManager.guardaOperador(operadores);

        return "redirect:/operador";
    }


    @GetMapping("/login")
    public String getLogin(){
        return "user/login_template";
    }

}
