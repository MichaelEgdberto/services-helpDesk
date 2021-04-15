package com.lifesaver.helpdesk.managerImpl;

import com.lifesaver.helpdesk.dao.OperadorDao;
import com.lifesaver.helpdesk.dao.RoleDao;
import com.lifesaver.helpdesk.manager.OperadorManager;
import com.lifesaver.helpdesk.model.Operadores;
import com.lifesaver.helpdesk.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service("OperadorManager")
public class OperadorManagerImpl implements OperadorManager {

    @Autowired
    OperadorDao operadorDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public Operadores traeOperadorPorUsuario(String usuario) {
        return operadorDao.findByUsuario(usuario);
    }

    @Override
    public void guardaOperador(Operadores operadores) {

        Operadores opTmp = null;

        System.out.println(operadores.toString());

        if(operadores.getId()!=null)
            opTmp=operadorDao.getOne(operadores.getId());

        if(opTmp!=null && !operadores.getPassword().equals(opTmp.getPassword()))
            operadores.setPassword(bCryptPasswordEncoder.encode(operadores.getPassword()));

        if(opTmp==null)
            operadores.setPassword(bCryptPasswordEncoder.encode(operadores.getPassword()));


        operadorDao.save(operadores);
    }
}
