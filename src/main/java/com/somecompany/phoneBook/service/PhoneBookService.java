package com.somecompany.phoneBook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.somecompany.phoneBook.exception.InvalidPhoneBookNameException;
import com.somecompany.phoneBook.model.PhoneBook;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for interacting with the phone books.
 * 
 * @author patrick
 *
 */
@Service
@Slf4j
public class PhoneBookService {
	@Autowired
	@Qualifier("phoneBookA")
	private PhoneBook phoneBookA;

	@Autowired
	@Qualifier("phoneBookB")
	private PhoneBook phoneBookB;

	@Value("${uniquePhoneBook.name}")
	private String uniquePhoneBookName;

	/**
	 * Create a new phone book entry or update an existing phone book entry.<br/>
	 * <br/>
	 * Operation:<br/>
	 * (1) If user tries to create an entry and no existing entry is found, the new entry will be added to the specified
	 * phone book only.<br/>
	 * (2) If user tries to create a new entry where there is already an existing one, the new number will replace the
	 * old number. <br/>
	 * (3) If an existing entry exist in both phone books, the entry will be updated in both phone books. (4) The
	 * operation returns a flag indicating whether the customer entry is already existing.
	 * 
	 * @param phoneBookName
	 * @param name
	 * @param number
	 * @return isExistingPrimaryPhoneBookEntry
	 * @throws InvalidPhoneBookNameException
	 */
	public boolean createOrUpdateEntry(String phoneBookName, String custName, String custNum)
			throws InvalidPhoneBookNameException {
		log.info("phoneBookName: " + phoneBookName);

		// Indicates whether the customer entry is already existing
		boolean isExistingPrimaryPhoneBookEntry = false;

		if (phoneBookName.equals(phoneBookA.getPhoneBookName())) {
			// Update phoneBookA and edit phoneBookB if necessary
			isExistingPrimaryPhoneBookEntry = executeCreateOrUpdateEntry(phoneBookA, phoneBookB, custName, custNum);
		} else if (phoneBookName.equals(phoneBookB.getPhoneBookName())) {
			// Update phoneBookB and edit phoneBookA if necessary
			isExistingPrimaryPhoneBookEntry = executeCreateOrUpdateEntry(phoneBookB, phoneBookA, custName, custNum);
		} else {
			// The input phone book name is invalid

			throw new InvalidPhoneBookNameException();
		}

		return isExistingPrimaryPhoneBookEntry;
	}

	/**
	 * Execute the phone book update.
	 * 
	 * @param primaryPhoneBook
	 * @param secondaryPhoneBook
	 * @param name
	 * @param number
	 * @return isExistingPrimaryPhoneBookEntry
	 */
	private boolean executeCreateOrUpdateEntry(PhoneBook primaryPhoneBook, PhoneBook secondaryPhoneBook,
			String custName, String custNum) {
		// Update primaryPhoneBook

		// Indicates whether the customer entry is already existing
		boolean isExistingPrimaryPhoneBookEntry = false;

		log.info("primaryPhoneBook name: " + primaryPhoneBook.getPhoneBookName());
		log.info("Original primaryPhoneBook entries: " + primaryPhoneBook.getEntry().entrySet());

		if (primaryPhoneBook.getEntry().containsKey(custName)) {
			// The specified phone book contains the customer entry already
			isExistingPrimaryPhoneBookEntry = true;
		}
		primaryPhoneBook.getEntry().put(custName, custNum);

		log.info("New primaryPhoneBook entries: " + primaryPhoneBook.getEntry().entrySet());

		// Update the same entry in the secondaryPhoneBook, if exist

		log.info("secondaryPhoneBook name: " + secondaryPhoneBook.getPhoneBookName());

		if (secondaryPhoneBook.getEntry().containsKey(custName)) {
			// secondaryPhoneBook also has the customer entry
			log.info("Original secondaryPhoneBook entries: " + secondaryPhoneBook.getEntry().entrySet());
			secondaryPhoneBook.getEntry().put(custName, custNum);
			log.info("New secondaryPhoneBook entries: " + secondaryPhoneBook.getEntry().entrySet());
		}

		return isExistingPrimaryPhoneBookEntry;
	}

	/**
	 * Read all phone book entries from all phone books.<br/>
	 * 
	 * 
	 * @return phoneBookList
	 */
	public List<PhoneBook> readAllEntriesFromAllPhoneBooks() {
		List<PhoneBook> phoneBookList = new ArrayList<>();
		phoneBookList.add(phoneBookA);
		phoneBookList.add(phoneBookB);

		return phoneBookList;
	}

	/**
	 * Read all phone book entries in a single phone book.<br/>
	 * 
	 * 
	 * @return PhoneBook
	 * @throws InvalidPhoneBookNameException
	 */
	public PhoneBook readAllEntriesFromSinglePhoneBook(String phoneBookName) throws InvalidPhoneBookNameException {
		if (phoneBookName.equals(phoneBookA.getPhoneBookName())) {
			// Fetching phoneBookA

			return phoneBookA;
		} else if (phoneBookName.equals(phoneBookB.getPhoneBookName())) {
			// Fetching phoneBookB

			return phoneBookB;
		} else {
			// The input phone book name is invalid

			throw new InvalidPhoneBookNameException();
		}

	}

	/**
	 * Read unique phone book entries from all phone books.<br/>
	 * 
	 * @return
	 */
	public PhoneBook readUniqueEntriesFromAllPhoneBooks() {
		Map<String, String> combinedEntries = new TreeMap<>();
		combinedEntries.putAll(phoneBookA.getEntry());
		combinedEntries.putAll(phoneBookB.getEntry());
		PhoneBook phoneBook = new PhoneBook(uniquePhoneBookName, combinedEntries);

		return phoneBook;
	}

	/**
	 * Delete an entry from single phone book.<br/>
	 * 
	 * @param phoneBookName
	 * @param custName
	 * @return isExistingPhoneBookEntry
	 * @throws InvalidPhoneBookNameException
	 */
	public boolean deleteEntryFromSinglePhoneBook(String phoneBookName, String custName)
			throws InvalidPhoneBookNameException {
		boolean isExistingPhoneBookEntry = true;

		if (phoneBookName.equals(phoneBookA.getPhoneBookName())) {
			// Removing entry from phoneBookA

			if (phoneBookA.getEntry().remove(custName) == null) {
				isExistingPhoneBookEntry = false;
			}
			;
		} else if (phoneBookName.equals(phoneBookB.getPhoneBookName())) {
			// Removing entry from phoneBookB

			if (phoneBookB.getEntry().remove(custName) == null) {
				isExistingPhoneBookEntry = false;
			}
		} else {
			// The input phone book name is invalid

			throw new InvalidPhoneBookNameException();
		}

		return isExistingPhoneBookEntry;
	}
}
