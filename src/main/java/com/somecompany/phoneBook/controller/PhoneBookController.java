package com.somecompany.phoneBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.phoneBook.exception.InvalidPhoneBookNameException;
import com.somecompany.phoneBook.model.CreateEntryReqParam;
import com.somecompany.phoneBook.model.PhoneBook;
import com.somecompany.phoneBook.model.UpdateEntryReqParam;
import com.somecompany.phoneBook.service.PhoneBookService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the phone book application.<br/>
 * It handles requests and interacts with the PhoneBookService.
 * 
 * @author patrick
 *
 */
@RestController
@RequestMapping("/api/phonebook")
@Slf4j
public class PhoneBookController {

	@Autowired
	private PhoneBookService phoneBookService;

	@Value("${phoneBook.name.base}")
	private String phoneBookNameBase;

	@PostMapping(path = "")
	@ApiOperation(value = "Create a new phone book entry or update an existing phone book entry")
	public ResponseEntity<Object> createEntry(@RequestBody CreateEntryReqParam createEntryReqParam) {
		String inputPhoneBookName = createEntryReqParam.getPhoneBookName();
		String inputCustName = createEntryReqParam.getCustName();
		String inputCustNum = createEntryReqParam.getCustNum();

		ResponseEntity<Object> responseEntity = doCreateOrUpdateEntry(inputPhoneBookName, inputCustName, inputCustNum);

		return responseEntity;
	}

	@GetMapping(path = "")
	@ApiOperation(value = "Read all entries from all phone books")
	public ResponseEntity<Object> readAllEntriesFromAllPhoneBooks() {
		List<PhoneBook> phoneBookList = phoneBookService.readAllEntriesFromAllPhoneBooks();

		return ResponseEntity.ok(phoneBookList);
	}

	@GetMapping(path = "/{phoneBookName}")
	@ApiOperation(value = "Read all entries from single phone book")
	public ResponseEntity<Object> readAllEntriesFromSinglePhoneBook(@PathVariable String phoneBookName) {

		String editedInputPhoneBookName = phoneBookNameBase.concat(phoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);

		PhoneBook phoneBook;

		try {
			phoneBook = phoneBookService.readAllEntriesFromSinglePhoneBook(editedInputPhoneBookName);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			// The input phone book name is invalid

			return ResponseEntity.badRequest().body(invalidPhoneBookNameException.getMessage());
		}

		return ResponseEntity.ok(phoneBook);
	}

	@GetMapping(path = "/unique")
	@ApiOperation(value = "Read unique entries from all phone books")
	public ResponseEntity<Object> readUniqueEntriesFromAllPhoneBooks() {
		PhoneBook phonebook = phoneBookService.readUniqueEntriesFromAllPhoneBooks();

		return ResponseEntity.ok(phonebook);
	}

	@PutMapping(path = "/{phoneBookName}/{custName}")
	@ApiOperation(value = "Update an existing phone book entry")
	public ResponseEntity<Object> updateEntry(@PathVariable String phoneBookName, @PathVariable String custName,
			@RequestBody UpdateEntryReqParam updateEntryReqParam) {
		String inputCustNum = updateEntryReqParam.getCustNum();

		ResponseEntity<Object> responseEntity = doCreateOrUpdateEntry(phoneBookName, custName, inputCustNum);

		return responseEntity;
	}

	@DeleteMapping(path = "/{phoneBookName}/{custName}")
	@ApiOperation(value = "Delete an entry from single phone book")
	public ResponseEntity<Object> deleteEntryFromSinglePhoneBook(@PathVariable String phoneBookName,
			@PathVariable String custName) {
		String editedInputPhoneBookName = phoneBookNameBase.concat(phoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);

		boolean isExistingPhoneBookEntry = false;

		try {
			isExistingPhoneBookEntry = phoneBookService.deleteEntryFromSinglePhoneBook(editedInputPhoneBookName,
					custName);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			// The input phone book name is invalid

			return ResponseEntity.badRequest().body(invalidPhoneBookNameException.getMessage());
		}

		if (isExistingPhoneBookEntry) {
			// Existing entry found and deleted

			return new ResponseEntity<Object>("Existing entry for \"" + custName + "\" deleted.",
					HttpStatus.NO_CONTENT);
		}

		// No existing entry found
		return ResponseEntity.badRequest()
				.body("No existing entry for \"" + custName + "\" found, please check and confirm.");

	}

	/**
	 * Common operation for create and update entry operation.
	 * 
	 * @param inputPhoneBookName
	 * @param inputCustName
	 * @param inputCustNum
	 * @return ResponseEntity<Object>
	 */
	private ResponseEntity<Object> doCreateOrUpdateEntry(String inputPhoneBookName, String inputCustName,
			String inputCustNum) {
		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);

		boolean isExistingPrimaryPhoneBookEntry = false;

		try {
			isExistingPrimaryPhoneBookEntry = phoneBookService.createOrUpdateEntry(editedInputPhoneBookName,
					inputCustName, inputCustNum);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			// The input phone book name is invalid

			return ResponseEntity.badRequest().body(invalidPhoneBookNameException.getMessage());
		}

		if (isExistingPrimaryPhoneBookEntry) {
			// Update operation was performed
			return new ResponseEntity<Object>("Existing customer entry is found in " + editedInputPhoneBookName
					+ ", and is updated with the new phone number.", HttpStatus.CREATED);

		}

		// Create operation was performed
		return ResponseEntity.ok("New customer entry added to " + editedInputPhoneBookName + " successfully.");
	}
}
