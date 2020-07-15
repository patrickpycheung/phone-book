package com.somecompany.phoneBook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.phoneBook.model.PhoneBook;

import lombok.extern.slf4j.Slf4j;

/**
 * Validation service for validating the phone book name, customer name and customer phone number.
 * 
 * @author patrick
 *
 */
@Service
@Slf4j
public class PhoneBookValidationService {

	@Autowired
	@Qualifier("phoneBookA")
	private PhoneBook phoneBookA;

	@Autowired
	@Qualifier("phoneBookB")
	private PhoneBook phoneBookB;

	@Value("${operation.regex}")
	private String operationRegex;

	@Value("${phoneBook.name.regex}")
	private String phoneBookNameRegex;

	@Value("${cust.name.regex}")
	private String custNameRegex;

	@Value("${cust.num.regex}")
	private String custNumRegex;

	@Value("${cust.name.limit}")
	private String custNameLimit;

	@Value("${cust.num.limit}")
	private String custNumLimit;

	/**
	 * Validate the user input for operation.
	 * 
	 * @return isValidInputOperation
	 */
	public boolean isValidInputOperation(String inputOperation) {
		if (!inputOperation.matches(operationRegex)) {
			System.out.println("You may have provided an invalid input, please try again.");
			log.error("Invalid operation");
			log.error("inputOperation: " + inputOperation);

			return false;
		}

		return true;
	}

	/**
	 * Validate the user input for phone book name.
	 * 
	 * @return isValidInputPhoneBookName
	 */
	public boolean isValidInputPhoneBookName(String inputPhoneBookName) {
		if (!inputPhoneBookName.matches(phoneBookNameRegex)) {
			System.out.println("");
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");
			log.error("Invalid phoneBook selection");
			log.error("inputPhoneBookName: " + inputPhoneBookName);

			return false;
		}

		return true;
	}

	/**
	 * Validate the user input for customer name.
	 * 
	 * @return isValidInputCustName
	 */
	public boolean isValidInputCustName(String inputCustName) {
		if (!inputCustName.matches(custNameRegex)) {
			System.out.println("");
			System.out.println("Invalid customer name!");
			System.out.println(
					"Invalid customer name! Customer names should be composed of alphabets (case sensitive) and digits only, with maximum length of "
							+ custNameLimit + " characters.");
			System.out.println("");
			log.error("Invalid customer name");
			log.error("inputCustName: " + inputCustName);

			return false;
		}

		return true;
	}

	/**
	 * Validate the user input for customer phone number.
	 * 
	 * @return isValidInputCustNum
	 */
	public boolean isValidInputCustNum(String inputCustNum) {
		if (!inputCustNum.matches(custNumRegex)) {
			System.out.println("");
			System.out.println("Invalid customer phone number!");
			System.out.println(
					"Invalid customer phone number! Customer phone numbers should be composed of digits only, with maximum length of "
							+ custNumLimit + " characters.");
			System.out.println("");
			log.error("Invalid customer phone number");
			log.error("inputCustNum: " + inputCustNum);

			return false;
		}

		return true;
	}
}
