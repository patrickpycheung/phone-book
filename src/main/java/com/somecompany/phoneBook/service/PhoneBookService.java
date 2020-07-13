package com.somecompany.phoneBook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
	 */
	public boolean createOrUpdateEntry(String phoneBookName, String name, String number) {
		log.info("phoneBookName: " + phoneBookName);

		// Indicates whether the customer entry is already existing
		boolean isExistingPrimaryPhoneBookEntry = false;

		if (phoneBookName.equals(phoneBookA.getPhoneBookName())) {
			// Update phoneBookA and edit phoneBookB if necessary
			isExistingPrimaryPhoneBookEntry = executeCreateOrUpdateEntry(phoneBookA, phoneBookB, name, number);
		} else {
			// Update phoneBookB and edit phoneBookA if necessary
			isExistingPrimaryPhoneBookEntry = executeCreateOrUpdateEntry(phoneBookB, phoneBookA, name, number);
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
	private boolean executeCreateOrUpdateEntry(PhoneBook primaryPhoneBook, PhoneBook secondaryPhoneBook, String name,
			String number) {
		// Update primaryPhoneBook

		// Indicates whether the customer entry is already existing
		boolean isExistingPrimaryPhoneBookEntry = false;

		log.info("primaryPhoneBook name: " + primaryPhoneBook.getPhoneBookName());
		log.info("Original primaryPhoneBook entries: " + primaryPhoneBook.getEntry().entrySet());

		if (primaryPhoneBook.getEntry().containsKey(name)) {
			// The specified phone book contains the customer entry already
			isExistingPrimaryPhoneBookEntry = true;
		}
		primaryPhoneBook.getEntry().put(name, number);

		log.info("New primaryPhoneBook entries: " + primaryPhoneBook.getEntry().entrySet());

		// Update the same entry in the secondaryPhoneBook, if exist

		log.info("secondaryPhoneBook name: " + secondaryPhoneBook.getPhoneBookName());

		if (secondaryPhoneBook.getEntry().containsKey(name)) {
			// secondaryPhoneBook also has the customer entry
			log.info("Original secondaryPhoneBook entries: " + secondaryPhoneBook.getEntry().entrySet());
			secondaryPhoneBook.getEntry().put(name, number);
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
	 */
	public PhoneBook readAllEntriesFromSinglePhoneBook(String phoneBookName) {
		if (phoneBookName.equals(phoneBookA.getPhoneBookName())) {
			// Fetching phoneBookA

			return phoneBookA;
		}

		// Fetching phoneBookB

		return phoneBookB;
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
		PhoneBook phoneBook = new PhoneBook("uniquePhoneBook", combinedEntries);

		return phoneBook;
	}
}
