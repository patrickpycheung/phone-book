package com.somecompany.phoneBook.exception;

import org.springframework.stereotype.Component;

/**
 * Exception when customer name is invalid.<br/>
 * 
 * @author patrick
 *
 */
@Component
public class InvalidCustNameException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String custNameLimit = "70";

	public InvalidCustNameException() {
		super("Invalid customer name! Customer names should be composed of alphabets (case sensitive) and digits only, with maximum length of "
				+ custNameLimit + " characters.");
	}
}
