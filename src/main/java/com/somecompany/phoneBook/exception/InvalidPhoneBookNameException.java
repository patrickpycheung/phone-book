package com.somecompany.phoneBook.exception;

/**
 * Exception when phone book name is invalid.<br/>
 * Thrown when the specified phone book could not be found.
 * 
 * @author patrick
 *
 */
public class InvalidPhoneBookNameException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPhoneBookNameException() {
		super("Invalid phoneBook name!");
	}
}
