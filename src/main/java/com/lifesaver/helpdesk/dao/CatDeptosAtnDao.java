package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.CatDeptosAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatDeptosAtnDao extends JpaRepository<CatDeptosAtencion,Long> {

    List<CatDeptosAtencion> findByActivoTrue();

    List<CatDeptosAtencion> findByOrderByIdAsc();

    CatDeptosAtencion findByDepartamentoNombreIgnoreCase(String deptoNombre);

    CatDeptosAtencion findByCodigoIgnoreCase(String codigo);

}
