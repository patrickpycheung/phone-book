package com.somecompany.phoneBook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.phoneBook.exception.InvalidPhoneBookNameException;
import com.somecompany.phoneBook.model.PhoneBook;
import com.somecompany.phoneBook.service.PhoneBookService;

import io.swagger.annotations.ApiOperation;

/**
 * Controller for the phone book application.<br/>
 * It handles requests and interacts with the PhoneBookService.
 * 
 * @author patrick
 *
 */
@RestController
@RequestMapping("/api/phonebook")
public class PhoneBookController {

	@Autowired
	private PhoneBookService phoneBookService;

	@GetMapping(path = "/{phoneBookName}")
	@ApiOperation(value = "Read all entries from single phone book")
	public ResponseEntity<Object> readAllEntriesFromSinglePhoneBook(@PathVariable String phoneBookName) {
		PhoneBook phoneBook;

		try {
			phoneBook = phoneBookService.readAllEntriesFromSinglePhoneBook(phoneBookName);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			return ResponseEntity.badRequest().body(invalidPhoneBookNameException.getMessage());
		}
		return ResponseEntity.ok(phoneBook);
	}
}
