package com.lifesaver.helpdesk.manager;

import com.lifesaver.helpdesk.model.Operadores;

public interface OperadorManager {

    Operadores traeOperadorPorUsuario(String usuario);

    void guardaOperador(Operadores operadores);


}
