package com.lifesaver.helpdesk.controller;

import com.lifesaver.helpdesk.dao.*;
import com.lifesaver.helpdesk.manager.OperadorManager;
import com.lifesaver.helpdesk.manager.TicketManager;
import com.lifesaver.helpdesk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ManagerController {

    @Autowired
    TicketsDao ticketsDao;

    @Autowired
    OperadorDao operadorDao;

    @Autowired
    CatStatusDao catStatusDao;

    @Autowired
    MovimientosTicketsDao movTkDao;

    @Autowired
    TicketManager ticketManager;

    @Autowired
    CatDeptosAtnDao catDeptosAtnDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    OperadorManager operadorManager;

    @GetMapping({"/manager/dashboard","/"})
    public String getDashboard(Model model, @RequestParam(name = "status", defaultValue = "0") Long status, @RequestParam(name = "page", defaultValue = "0") Integer page){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());


        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");
        Boolean isClient = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("CLIENTE");
        Boolean isOperador = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("OPERADOR");

        if(isAdmin  ||  isOperador) {
            model.addAttribute("isAdmin", isAdmin);

            if (status == 0)
                status = catStatusDao.findByIndice(0).getId();


            model.addAttribute("contadores", ticketManager.contadores(ticketsDao.contadorSinFechaSinUsuario()));
            model.addAttribute("contadoresHoy", ticketManager.contadores(ticketsDao.contadorHoySinUsuario()));
            model.addAttribute("lsStatus", catStatusDao.findByActivoTrueOrderByIndiceAsc());
            model.addAttribute("op", op);
            model.addAttribute("status", status);
            model.addAttribute("tickets", ticketManager.traeTicketsEstatus(op, status, page));
            model.addAttribute("pDashboard", true);

            return "manager/dashboard_template";
        }else if(isClient){
            return "redirect:/client/perfil";
        }else{
            return "manager/dashboard_template";
        }
    }

    @GetMapping("/manager/revisa")
    public String getRevisa(Model model, @RequestParam(name = "idt", defaultValue = "0") Long idt, @RequestParam(name = "page", defaultValue = "0") Integer page){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        if(idt!=0) {
            Tickets tk  = ticketsDao.getOne(idt);
            System.out.println(tk.toString());
            tk.setFechaModificado(new Date());
            ticketsDao.save(tk);
            model.addAttribute("op",op);
            model.addAttribute("cancelUrl","/manager/dashboard");
            model.addAttribute("lsMov", ticketManager.movimientosTicket(idt,page));
            model.addAttribute("lsStatus", catStatusDao.findByActivoTrueOrderByIndiceAsc());
            model.addAttribute("tk", tk);
            return "/manager/revisar_template";
        }else{
            System.out.println("se va por otro lado");
            return "redirect:dashboard";
        }
    }

    @PostMapping("/manager/revisa")
    public String postRevisa(Model model, @RequestParam(name = "status") Long status, @RequestParam(name = "observacion") String observacion, Long idt){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Operadores op = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        Tickets tk = ticketsDao.getOne(idt);

        tk.setStatus(catStatusDao.getOne(status));

        MovimientosTicket mv = new MovimientosTicket();
        mv.setObservacion(observacion);
        mv.setStatus(catStatusDao.getOne(status));
        mv.setOperador(op);
        mv.setTicket(tk);

        System.out.println(mv);

        ticketManager.guardaMovimientos(mv,tk);


        return "redirect:/manager/dashboard";
    }

    @GetMapping("/admin/catStatus")
    public String getCatStatus(Model model, @RequestParam(name = "ids", defaultValue = "0") Long ids){


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);


        CatStatus catStatus = new CatStatus();
        Boolean disableButton = false;
        if(ids!=0){
            catStatus = catStatusDao.getOne(ids);

            if(catStatus.getIndice()==0 || catStatus.getIndice()==10){
                disableButton = true;
            }

        }
        model.addAttribute("op",op);
        model.addAttribute("disableButton", disableButton);
        model.addAttribute("catStatus", catStatus);
        model.addAttribute("lsStatus",catStatusDao.findByOrderByIndiceAsc());
        model.addAttribute("pStatus", true);
        return "manager/cat_status_template";
    }

    private Model modelCatStatus(Model model, CatStatus catStatus){
        model.addAttribute("catStatus", catStatus);
        model.addAttribute("lsStatus", catStatusDao.findByOrderByIndiceAsc());
        model.addAttribute("pStatus", true);
        return model;
    }

    @PostMapping("/admin/catStatus")
    public String postCatStatus(Model model, CatStatus catStatus, BindingResult bindingResult){

        CatStatus cs = catStatusDao.findByIndice(catStatus.getIndice());

        System.out.println(cs!=null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());
        model.addAttribute("op",op);

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        if(catStatus.getIndice()<0 || catStatus.getIndice()>10) {
            bindingResult.rejectValue("indice", "error.indice", "EL indice Solo puede estar entre 0 y 10.");

            model = modelCatStatus(model,catStatus);
            return "/manager/cat_status_template";

        }else {

            if (catStatus.getId() != null) {
                System.out.println("catStatus.getId != null " + catStatus.toString());
                if (cs != null && cs.getId() != catStatus.getId()) {
                    System.out.println("cs!=null && cs.getId()!=catStatus.getId() " + cs.toString());
                    bindingResult.rejectValue("indice", "error.indice", "EL indice ya existe intente con otro.");
                    model = modelCatStatus(model,catStatus);
                    return "/manager/cat_status_template";
                } else {
                    System.out.println("entra a salvar ");
                    catStatusDao.save(catStatus);
                    return "redirect:/admin/catStatus";
                }
            } else {

                if (cs != null) {

                    bindingResult.rejectValue("indice", "error.indice", "EL indice ya existe intente con otro.");
                    model = modelCatStatus(model,catStatus);

                    return "/manager/cat_status";
                } else {
                    System.out.println("enttra a salvar ");
                    catStatusDao.save(catStatus);

                    return "redirect:/admin/catStatus";
                }
            }
        }

    }

    @GetMapping("/admin/deptos")
    public String getDepartamentos(Model model, @RequestParam(name = "idd", defaultValue = "0") Long idd){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        CatDeptosAtencion cdp = new CatDeptosAtencion();

        if(idd!=0){
            cdp = catDeptosAtnDao.getOne(idd);
        }
        model.addAttribute("op",op);
        model.addAttribute("lsDeptos", catDeptosAtnDao.findByOrderByIdAsc());
        model.addAttribute("catDeptosAtencion", cdp);
        model.addAttribute("pDeptos", true);

        return "/manager/departamentos_template";

    }


    private Model modelDeptos(Model model, CatDeptosAtencion dp, CatDeptosAtencion catDeptosAtencion,BindingResult bindingResult){
        if (dp.getDepartamentoNombre().equals(catDeptosAtencion.getDepartamentoNombre().toUpperCase())) {
            bindingResult.rejectValue("departamentoNombre", "", "EL nombre del departamento ya existe intente con otro.");
        } else if (dp.getCodigo().equals(catDeptosAtencion.getCodigo().toUpperCase())) {
            bindingResult.rejectValue("codigo", "", "EL codigo ya existe intente con otro.");
        }
        model.addAttribute("lsDeptos", catDeptosAtnDao.findByOrderByIdAsc());
        model.addAttribute("catDeptosAtencion", catDeptosAtencion);
        model.addAttribute("pDeptos", true);
        return model;
    }

    @PostMapping("/admin/deptos")
    public String postDepartamentos(Model model, CatDeptosAtencion catDeptosAtencion, BindingResult bindingResult){

        CatDeptosAtencion dp = catDeptosAtnDao.findByDepartamentoNombreIgnoreCase(catDeptosAtencion.getDepartamentoNombre());


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores op = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        if(dp==null)
            dp = catDeptosAtnDao.findByCodigoIgnoreCase(catDeptosAtencion.getCodigo());
            model = modelDeptos(model,dp,catDeptosAtencion,bindingResult);

        model.addAttribute("op",op);
        if(catDeptosAtencion.getId()!=null) {
            if (dp != null && dp.getId()!=catDeptosAtencion.getId()) {


                return "/manager/departamentos_template";
            } else {

                catDeptosAtencion.setCodigo(catDeptosAtencion.getCodigo().toUpperCase());
                catDeptosAtencion.setDepartamentoNombre(catDeptosAtencion.getDepartamentoNombre().toUpperCase());

                catDeptosAtnDao.save(catDeptosAtencion);
                return "redirect:/admin/deptos";
            }
        }else{
            if (dp != null) {
                System.out.println(dp.getDepartamentoNombre().equals(catDeptosAtencion.getDepartamentoNombre().toUpperCase()));
                System.out.println(dp.getCodigo().equals(catDeptosAtencion.getCodigo().toUpperCase()));

                model = modelDeptos(model,dp,catDeptosAtencion,bindingResult);

                return "/manager/departamentos_template";

            } else {

                catDeptosAtencion.setCodigo(catDeptosAtencion.getCodigo().toUpperCase());
                catDeptosAtencion.setDepartamentoNombre(catDeptosAtencion.getDepartamentoNombre().toUpperCase());

                catDeptosAtnDao.save(catDeptosAtencion);
                return "redirect:/admin/deptos";
            }

        }

    }


    private Model getCreaOperadoresClientes(Model model, Long ido){

        Operadores op = new Operadores();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores ops = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        if(ido!=0)
            op = operadorDao.getOne(ido);
        model.addAttribute("op",ops);
        model.addAttribute("lsRoles", roleDao.findAll());
        model.addAttribute("operadores", op);

        model.addAttribute("pOperadores", true);
        model.addAttribute("lsDeptos", catDeptosAtnDao.findByActivoTrue());

        return model;

    }


    @GetMapping("/admin/operadores")
    public String getOperadores(Model model, @RequestParam(name = "idd", defaultValue = "0") Long ido){

        model = getCreaOperadoresClientes(model,ido);
        model.addAttribute("lsOperadores",operadorDao.lsOperadoresNoRol("CLIENTE"));

        return "/manager/creausuario_template";
    }

    @GetMapping("/admin/clientes")
    public String getClientes(Model model, @RequestParam(name = "idd", defaultValue = "0") Long ido){

        model = getCreaOperadoresClientes(model,ido);
        model.addAttribute("lsOperadores",operadorDao.lsOperadoressRol("CLIENTE"));

        return "/manager/creacliente_template";
    }


    private Model modelOperadores(Model model, Operadores op, BindingResult bindingResult, Operadores operadores){
        if(op.getUsuario().equals(operadores.getUsuario()))
            bindingResult.rejectValue("usuario", "","EL nombre usuario ya existe intente con otro.");
        if(op.getCorreo().equals(operadores.getCorreo()))
            bindingResult.rejectValue("correo", "","EL correo ya esta registrado intente con otro.");
        model.addAttribute("lsRoles", roleDao.findAll());
        model.addAttribute("operadores", operadores);

        model.addAttribute("pOperadores", true);
        model.addAttribute("lsDeptos", catDeptosAtnDao.findByActivoTrue());
        return model;
    }

    private String modelPostOperadores(Model model, Operadores operadores, BindingResult bindingResult, String template, String redirectController){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Operadores ops = operadorDao.findByUsuario(auth.getName());

        Boolean isAdmin = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        Operadores op = operadorDao.findByUsuario(operadores.getUsuario());

        if(op==null)
            op = operadorDao.findByCorreo(operadores.getCorreo());

        model.addAttribute("op",ops);
        if(operadores.getId()!=null){
            if(op!=null && operadores.getId()!=op.getId()){

                model = modelOperadores(model,op,bindingResult,operadores);

                if(redirectController.equals("clientes"))
                    model.addAttribute("lsOperadores",operadorDao.lsOperadoressRol("CLIENTE"));
                else
                    model.addAttribute("lsOperadores",operadorDao.lsOperadoresNoRol("CLIENTE"));

                System.out.println("YA EXISTE USUARIO O CORREO " + template);

                return "/manager/" + template;

            }else{
                operadores.setCorreo(operadores.getCorreo().toLowerCase());
                operadores.setNombre(operadores.getNombre().toUpperCase());
                operadores.setApellidos(operadores.getApellidos().toUpperCase());

                operadorManager.guardaOperador(operadores);

                return "redirect:" + redirectController;

            }
        }else{
            if(op!=null) {
                model = modelOperadores(model, op, bindingResult, operadores);

                if(redirectController.equals("clientes"))
                    model.addAttribute("lsOperadores",operadorDao.lsOperadoressRol("CLIENTE"));
                else
                    model.addAttribute("lsOperadores",operadorDao.lsOperadoresNoRol("CLIENTE"));

                System.out.println("YA EXISTE USUARIO O CORREO 2" + template);

                return "/manager/" +template;

            }else{
                operadores.setCorreo(operadores.getCorreo().toLowerCase());
                operadores.setNombre(operadores.getNombre().toUpperCase());
                operadores.setApellidos(operadores.getApellidos().toUpperCase());

                operadorManager.guardaOperador(operadores);

                return "redirect:/admin/"+redirectController;

            }
        }
    }

    @PostMapping("/admin/operadores")
    public String postOperadores(Model model, Operadores operadores,  BindingResult bindingResult){

            return modelPostOperadores(model,operadores,bindingResult, "creausuario_template", "/admin/operadores");
    }

    @PostMapping("/admin/clientes")
    public String postClientes(Model model, Operadores operadores,  BindingResult bindingResult){

        return modelPostOperadores(model,operadores,bindingResult, "creacliente_template","clientes");
    }

}
