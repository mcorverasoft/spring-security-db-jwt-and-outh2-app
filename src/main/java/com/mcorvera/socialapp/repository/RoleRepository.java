package com.mcorvera.socialapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mcorvera.socialapp.model.Role;
import com.mcorvera.socialapp.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	public List<Role> findAll();
	public Role findByName(RoleName rolename );

}
