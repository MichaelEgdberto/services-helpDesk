package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosDao extends JpaRepository<Usuarios, Long> {

    Usuarios findByEmail(String email);

}
