package com.stormracing.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserAccountExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -575063485155861704L;

	public UserAccountExistsException(String email) {
		
		super("A user with email '"+email+"' exists in the system.");
		
	}

}
