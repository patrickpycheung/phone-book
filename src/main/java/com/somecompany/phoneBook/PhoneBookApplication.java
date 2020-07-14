package com.somecompany.phoneBook;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.somecompany.phoneBook.exception.InvalidPhoneBookNameException;
import com.somecompany.phoneBook.model.PhoneBook;
import com.somecompany.phoneBook.service.PhoneBookService;

import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for the phone book application.
 * 
 * @author patrick
 *
 */
@SpringBootApplication(scanBasePackages = { "com.somecompany.phoneBook" })
@Slf4j
public class PhoneBookApplication implements CommandLineRunner {

	@Autowired
	private PhoneBookService phoneBookService;

	/* Create 2 phone books with sample entries on system init */

	@Autowired
	@Qualifier("phoneBookA")
	private PhoneBook phoneBookA;

	@Autowired
	@Qualifier("phoneBookB")
	private PhoneBook phoneBookB;

	@Value("${phoneBook.name.base}")
	private String phoneBookNameBase;

	@Value("${operation.regex}")
	private String operationRegex;

	@Value("${phoneBook.name.regex}")
	private String phoneBookNameRegex;

	@Value("${cust.name.regex}")
	private String custNameRegex;

	@Value("${cust.num.regex}")
	private String custNumRegex;

	private String inputPhoneBookName;

	private String inputCustName;

	private String inputCustNum;

	private Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		SpringApplication.run(PhoneBookApplication.class, args).close();
		log.info("End of application.");
	}

	@Override
	public void run(String... args) {
		log.info("Initializing phone book application...");

		System.out.println("");
		System.out.println("Welcome to the phone book application!");

		// Display latest entries of all phone books
		doReadAllEntriesFromAllPhoneBooks();

		boolean isQuit = false;

		while (!isQuit) {
			System.out.println("Please select your operation:");
			System.out.println("- Create a new contact or update an existing contact. (C)");
			System.out.println("- Read all contacts from all phone books. (A)");
			System.out.println("- Read all contacts in a phone book. (R)");
			System.out.println("- Retrieve a unique set of all contacts across all phone books. (U)");
			System.out.println("- Delete an existing contact entry from an existing phone book. (D)");
			System.out.println("- Quit application. (Q)");
			String inputOperation = scanner.nextLine().trim();
			log.info("inputOperation: " + inputOperation);

			// Validate the user input

			if (!inputOperation.matches(operationRegex)) {
				System.out.println("You may have provided an invalid input, please try again.");
				continue;
			}

			String upperCaseInputOperation = inputOperation.toUpperCase();

			// Determine the action to perform

			switch (upperCaseInputOperation) {
			case "C":
				// Create or update customer entry
				doCreateOrUpdateEntry();
				break;
			case "A":
				doReadAllEntriesFromAllPhoneBooks();
				break;
			case "R":
				// Read all entries from a single phone book
				doReadAllEntriesFromSinglePhoneBook();
				break;
			case "U":
				// Read unique phone book entries from all phone books
				doReadUniqueEntriesFromAllPhoneBooks();
				break;
			case "D":
				// Delete an entry from a single phone book
				doDeleteEntryFromSinglePhoneBook();
				break;
			case "Q":
				// Quit application
				isQuit = true;
				break;
			default:
				// Invalid input
				System.out.println("You may have provided an invalid input, please try again.");
			}
		}

		scanner.close();

		System.out.println("Goodbye!");
		System.out.println("");
		log.info("Ending application...");
	}

	/**
	 * Perform the create or update entry operation.
	 */
	private void doCreateOrUpdateEntry() {
		System.out.println("Please enter the phoneBook that you would like to update:");
		System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
		inputPhoneBookName = scanner.nextLine().trim();
		log.info("inputPhoneBookName: " + inputPhoneBookName);

		System.out.println("Please enter the customer name in exact letters. Names are case-sensitive:");
		inputCustName = scanner.nextLine().trim();
		log.info("inputCustName: " + inputCustName);

		System.out.println("Please enter the customer phone number:");
		inputCustNum = scanner.nextLine().trim();
		log.info("inputCustNum: " + inputCustNum);

		// Validate the user input
		if (!isValidInputPhoneBookName()) {
			return;
		}

		if (!isValidInputCustName()) {
			return;
		}

		if (!isValidInputCustNum()) {
			return;
		}

		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);

		boolean isExistingPrimaryPhoneBookEntry = false;

		try {
			isExistingPrimaryPhoneBookEntry = phoneBookService.createOrUpdateEntry(editedInputPhoneBookName,
					inputCustName, inputCustNum);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			return;
		}

		if (isExistingPrimaryPhoneBookEntry) {
			// Update operation was performed
			System.out.println("Existing customer entry is found in " + editedInputPhoneBookName
					+ ", and is updated with the new phone number.");
		} else {
			// Create operation was performed
			System.out.println("New customer entry added to " + editedInputPhoneBookName + " successfully.");
		}

		// Display latest entries of all phone books
		doReadAllEntriesFromAllPhoneBooks();
	}

	/**
	 * Perform the read all entries from all phone books operation.
	 */
	private void doReadAllEntriesFromAllPhoneBooks() {
		System.out.println("Listing latest phone book entries from all phone books...");
		System.out.println("");

		System.out.println("============================================================");

		List<PhoneBook> phoneBookList = phoneBookService.readAllEntriesFromAllPhoneBooks();

		phoneBookList.stream().forEach(phoneBook -> {
			System.out.println("<" + phoneBook.getPhoneBookName() + ">");
			System.out.println("Customer name | Customer phone number");
			System.out.println("************************************************************");

			phoneBook.getEntry().forEach((name, num) -> System.out.println(name + " | " + num));
			System.out.println("");
		});
	}

	/**
	 * Perform the read all entries from single phone book operation.
	 */
	private void doReadAllEntriesFromSinglePhoneBook() {
		System.out.println("Please enter the phoneBook that you would like to retrieve:");
		System.out.println(phoneBookA.getPhoneBookName() + " (A)" + "|" + phoneBookB.getPhoneBookName() + " (B)");
		inputPhoneBookName = scanner.nextLine().trim();
		log.info("inputPhoneBookName: " + inputPhoneBookName);

		// Validate the user input
		if (!isValidInputPhoneBookName()) {
			return;
		}

		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());

		System.out.println("Listing latest phone book entries from " + editedInputPhoneBookName + " ...");
		System.out.println("");

		System.out.println("============================================================");

		PhoneBook phoneBook;

		try {
			phoneBook = phoneBookService.readAllEntriesFromSinglePhoneBook(editedInputPhoneBookName);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			return;
		}

		System.out.println("<" + phoneBook.getPhoneBookName() + ">");
		System.out.println("Customer name | Customer phone number");
		System.out.println("************************************************************");

		phoneBook.getEntry().forEach((name, num) -> System.out.println(name + " | " + num));
		System.out.println("");
	}

	/**
	 * Perform the read unique entries from all phone books operation.
	 */
	public void doReadUniqueEntriesFromAllPhoneBooks() {
		System.out.println("Listing unique phone book entries across all phone books...");
		System.out.println("");

		System.out.println("============================================================");
		System.out.println("<Unique phone book entries>");
		System.out.println("Customer name | Customer phone number");
		System.out.println("************************************************************");

		PhoneBook phoneBook = phoneBookService.readUniqueEntriesFromAllPhoneBooks();

		phoneBook.getEntry().forEach((name, num) -> System.out.println(name + " | " + num));
		System.out.println("");
	}

	/**
	 * Perform the Delete an entry from single phone book operation.
	 */
	public void doDeleteEntryFromSinglePhoneBook() {
		System.out.println("Please enter the phoneBook from which you would like to delete the customer entry:");
		System.out.println(phoneBookA.getPhoneBookName() + " (A)" + "|" + phoneBookB.getPhoneBookName() + " (B)");
		inputPhoneBookName = scanner.nextLine().trim();
		log.info("inputPhoneBookName: " + inputPhoneBookName);

		System.out.println("Please enter the customer name in exact letters. Names are case-sensitive:");
		inputCustName = scanner.nextLine().trim();
		log.info("inputCustName: " + inputCustName);

		// Validate the user input
		if (!isValidInputPhoneBookName()) {
			return;
		}

		if (!isValidInputCustName()) {
			return;
		}

		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);

		boolean isExistingPhoneBookEntry = false;

		try {
			isExistingPhoneBookEntry = phoneBookService.deleteEntryFromSinglePhoneBook(editedInputPhoneBookName,
					inputCustName);
		} catch (InvalidPhoneBookNameException invalidPhoneBookNameException) {
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			return;
		}

		if (isExistingPhoneBookEntry) {
			// Existing entry found and deleted
			System.out.println("Existing entry for \"" + inputCustName + "\" deleted.");

			// Display latest entries of all phone books
			doReadAllEntriesFromAllPhoneBooks();
		} else {
			// No existing entry found
			System.out.println("No existing entry for \"" + inputCustName + "\" found, please check and confirm.");
			System.out.println("");
		}
	}

	/**
	 * Validate the user input for phone book name.
	 * 
	 * @return
	 */
	private boolean isValidInputPhoneBookName() {
		if (!inputPhoneBookName.matches(phoneBookNameRegex)) {
			System.out.println("");
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			return false;
		}

		return true;
	}

	/**
	 * Validate the user input for customer name.
	 * 
	 * @return
	 */
	private boolean isValidInputCustName() {
		if (!inputCustName.matches(custNameRegex)) {
			System.out.println("");
			System.out.println("Invalid customer name!");
			System.out.println(
					"Customer names should be composed of alphabets (case sensitive) and digits only, with maximum length of 70 characters.");
			System.out.println("");

			return false;
		}

		return true;
	}

	/**
	 * Validate the user input for customer phone number.
	 * 
	 * @return
	 */
	private boolean isValidInputCustNum() {
		if (!inputCustNum.matches(custNumRegex)) {
			System.out.println("");
			System.out.println("Invalid customer phone number!");
			System.out.println(
					"Customer phone numbers should be composed of digits only, with maximum length of 30 characters.");
			System.out.println("");

			return false;
		}

		return true;
	}
}
