package com.mcorvera.springsecuritydb;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.mcorvera.socialapp.model.Role;
import com.mcorvera.socialapp.model.RoleName;
import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.repository.RoleRepository;
import com.mcorvera.socialapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityDbJWTAppAplicationTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	@Autowired
	private RoleRepository roleRepository; 
	

	public SpringSecurityDbJWTAppAplicationTest() {
	
	}


	@Test
	public void createRoleTest() {
		
		Role roleTest=new Role();
		roleTest.setName(RoleName.ROL_USER);
		roleTest.setDescription("ROLE FOR NORMAL USERS");
		Role roleReturn = roleRepository.save(roleTest);
		
		assertTrue(roleReturn.getName().equals(roleTest.getName()));
		
	}

	public void createUserTest() {
		
		User userTest=new User();
		userTest.setName("romeo");
		userTest.setEmail("romeo.corvera@gmail.com");
		userTest.setPassword(bcrypt.encode("romeo"));
		userTest.setUsername("rcorvera");
		List<Role> roles=new ArrayList<>();
		RoleName rolename=RoleName.ROL_ADMIN;
		Role role=roleRepository.findByName(rolename);
		roles.add(role);
		userTest.setRoles(roles);
		User userRetorno= userRepository.save(userTest);
		assertTrue(userTest.getPassword().equalsIgnoreCase(userRetorno.getPassword()));
		
	}
	

	public void findByEmailOrUsernameTest() {
		String email="romeo.corvera@gmail.com";
		String username="rcorvera";
		Optional<User> user= userRepository.findByEmailOrUsername(email, username);
		assertTrue(user.isPresent());
		
	}

}
