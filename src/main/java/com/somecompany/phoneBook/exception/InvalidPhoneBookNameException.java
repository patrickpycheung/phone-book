package com.somecompany.phoneBook.exception;

import org.springframework.stereotype.Component;

/**
 * Exception when phone book name is invalid.<br/>
 * Thrown when the specified phone book could not be found.
 * 
 * @author patrick
 *
 */
@Component
public class InvalidPhoneBookNameException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPhoneBookNameException() {
		super("Invalid phoneBook name! Provide \"A\" for phoneBookA, \"B\" for phoneBookB.");
	}
}
