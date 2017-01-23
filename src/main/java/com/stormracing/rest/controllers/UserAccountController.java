package com.stormracing.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.stormracing.entities.UserAccount;
import com.stormracing.entities.UserAccountRepository;
import com.stormracing.exceptions.InvalidUserAccountException;
import com.stormracing.exceptions.UserAccountExistsException;
import com.stormracing.exceptions.UserNotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserAccountController {

	private final UserAccountRepository userAcctRepo;
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	public UserAccountController(UserAccountRepository userAcctRepo) {
		
		this.userAcctRepo = userAcctRepo;	
		
	}
	

	//@RequestMapping("/{userId}", method = RequestMethod.GET)
	@GetMapping("/{userId}")
	UserAccount getUser(@PathVariable Long userId) {

		logger.warn("validating User Account for ID: "+userId);
		return this.validateUser(userId);
		
	}
	
	@GetMapping("/exists")
	@ResponseBody UserAccount getUserByEmail(
			@RequestParam(value = "email") String email){
		
		logger.warn("Looking up User Account for: "+email);
		
		return this.userAcctRepo.findByEmail(email).orElseThrow(
				(() -> new  UserNotFoundException(email)));
		
	}
	
	@PostMapping
	@ResponseBody UserAccount createUser( @RequestBody UserAccount user ) {
				
		logger.info("Creating user...");
		
		try{
			
			if (user.getEmail()== null || !user.getEmail().matches("^.+@.+\\..+$")){
				throw new InvalidUserAccountException(user.getEmail());
			}
			
			UserAccount existingUser = getUserByEmail(user.email);
			throw new UserAccountExistsException(existingUser.getEmail());
			
			}
		
		catch (UserNotFoundException notfound){
			
			logger.info("User is not in database, Continue creating user account.");
		}
		
		return this.userAcctRepo.save(user);
		
	}
	
	private UserAccount validateUser(Long id) {
		
		return this.userAcctRepo.findById(id).orElseThrow(
				(() -> new UserNotFoundException(String.valueOf(id))));

	}

}
