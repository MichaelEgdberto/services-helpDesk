package com.lifesaver.helpdesk.dao;

import com.lifesaver.helpdesk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleDao extends JpaRepository<Role, Long> {

    Role findByRole(String role);

}
