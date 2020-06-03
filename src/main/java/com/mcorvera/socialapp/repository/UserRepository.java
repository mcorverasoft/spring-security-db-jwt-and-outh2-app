package com.mcorvera.socialapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mcorvera.socialapp.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findById(Long id);
	Optional<User> findByEmailOrUsername(String email, String username);
	Boolean existsByEmail (String email);
	Boolean existsByUsername (String username);
	Boolean existsByPhone (String phone);
	List<User> findAll();
	Boolean existsByProviderId(String providerId);
	Optional<User> findByProviderId(String providerId);
}
