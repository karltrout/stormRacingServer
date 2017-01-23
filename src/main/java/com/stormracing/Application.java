package com.stormracing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.stormracing.entities.UserAccountRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserAccountRepository accountRepository) {
		return (evt) -> accountRepository.count();//(new UserAccount("Karl", "Trout", "karl.trout@gmail.com"));
	}
}
