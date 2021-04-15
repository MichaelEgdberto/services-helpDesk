package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.CatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatStatusDao extends JpaRepository<CatStatus, Long> {

    List<CatStatus> findByActivoTrueOrderByIndiceAsc();

    List<CatStatus> findByActivoTrueAndPublicoTrueOrderByIndiceAsc();

    List<CatStatus> findByOrderByIndiceAsc();

    List<CatStatus> findByActivoTrueAndIndiceBetweenOrderByIndiceAsc(Integer a, Integer b);

    CatStatus findByIndice(Integer indice);



}
