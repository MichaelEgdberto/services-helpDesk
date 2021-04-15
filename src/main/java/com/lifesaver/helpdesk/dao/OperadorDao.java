package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.Operadores;
import com.lifesaver.helpdesk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperadorDao extends JpaRepository<Operadores,Long> {

    Operadores findByUsuario(String usuario);


    Operadores findByCorreo(String correo);

    @Query(value = "SELECT * FROM operadores where operador_id in (select  operador_id from oper_role where rol_id in(select rol_id from roles where role=?1))", nativeQuery = true)
    List<Operadores> lsOperadoressRol(String rol);

    @Query(value = "SELECT * FROM operadores where operador_id in (select  operador_id from oper_role where rol_id in(select rol_id from roles where role<>?1))", nativeQuery = true)
    List<Operadores> lsOperadoresNoRol(String rol);



}
