package com.somecompany.phoneBook.exception;

import org.springframework.stereotype.Component;

/**
 * Exception when customer phone number is invalid.<br/>
 * 
 * @author patrick
 *
 */
@Component
public class InvalidCustNumException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String custNumLimit = "30";

	public InvalidCustNumException() {
		super("Invalid customer phone number! Customer phone numbers should be composed of digits only, with maximum length of "
				+ custNumLimit + " characters.");
	}
}
