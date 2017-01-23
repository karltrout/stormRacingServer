package com.stormracing.entities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	Optional<UserAccount> findByEmail(String email);
	Optional<UserAccount> findById(Long id);
}
