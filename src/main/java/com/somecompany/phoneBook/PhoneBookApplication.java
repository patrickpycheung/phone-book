package com.somecompany.phoneBook;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	@Value("${cust.name.limit}")
	private String custNameLimit;

	@Value("${cust.num.limit}")
	private String custNumLimit;

	private String inputPhoneBookName;

	private String inputCustName;

	private String inputCustNum;

	public static void main(String[] args) {
		SpringApplication.run(PhoneBookApplication.class, args).close();
		log.info("End of application.");
	}

	@Override
	public void run(String... args) {
		log.info("Initializing phone book application...");

		System.out.println("");
		System.out.println("Welcome to phone book application!");

		// Display latest entries of all phone books
		doReadAllEntriesFromAllPhoneBooks();

		boolean isQuit = false;

		while (!isQuit) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please select your operation:");
			System.out.println("Create a new record or update an existing contact. (C)");
			System.out.println("Retrieve all contacts in an address book. (R)");
			System.out.println("Quit application. (Q)");
			String inputOperation = scanner.nextLine().trim();
			log.info("inputOperation: " + inputOperation);

			// Validate the user input

			if (!inputOperation.matches("^[a-zA-Z]$")) {
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
			case "R":
				// Read all entries from a single phone book
				doReadAllEntriesFromSinglePhoneBook();
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

		System.out.println("Goodbye!");
		log.info("Ending application...");
	}

	/**
	 * Perform the create or update entry operation.
	 */
	private void doCreateOrUpdateEntry() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter the phoneBook that you would like to update:");
		System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
		inputPhoneBookName = scanner.nextLine().trim();
		log.info("inputPhoneBookName: " + inputPhoneBookName);

		System.out.println("Please enter the customer name:");
		inputCustName = scanner.nextLine().trim();
		log.info("inputCustName: " + inputCustName);

		System.out.println("Please enter the customer phone number:");
		inputCustNum = scanner.nextLine().trim();
		log.info("inputCustNum: " + inputCustNum);

		boolean isErrorInput = false;

		// Validate the user input
		if (!inputPhoneBookName.matches("^[aAbB]$")) {
			System.out.println("");
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + " | " + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			isErrorInput = true;
		}

		int custNameLimitInt = Integer.valueOf(custNameLimit);

		if (!inputCustName.matches("^[a-zA-Z0-9 ]{1," + custNameLimitInt + "}$")) {
			System.out.println("");
			System.out.println("Invalid customer name!");
			System.out.println(
					"Customer names should be composed of alphabets (case sensitive) and digits only, with maximum length of 70 characters.");
			System.out.println("");

			isErrorInput = true;
		}

		int custNumLimitInt = Integer.valueOf(custNumLimit);

		if (!inputCustNum.matches("^[0-9]{1," + custNumLimitInt + "}$")) {
			System.out.println("");
			System.out.println("Invalid customer phone number!");
			System.out.println(
					"Customer phone numbers should be composed of digits only, with maximum length of 30 characters.");
			System.out.println("");

			isErrorInput = true;
		}

		if (isErrorInput) {
			// Go back to main menu
			return;
		}

		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());
		log.info("editedInputPhoneBookName: " + editedInputPhoneBookName);
		boolean isExistingPrimaryPhoneBookEntry = phoneBookService.createOrUpdateEntry(editedInputPhoneBookName,
				inputCustName, inputCustNum);
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
		Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter the phoneBook that you would like to retrieve:");
		System.out.println(phoneBookA.getPhoneBookName() + " (A)" + "|" + phoneBookB.getPhoneBookName() + " (B)");
		inputPhoneBookName = scanner.nextLine().trim();

		// Validate the user input
		if (!inputPhoneBookName.matches("^[aAbB]$")) {
			System.out.println("");
			System.out.println("Invalid phoneBook selection!");
			System.out.println(phoneBookA.getPhoneBookName() + " (A)" + "|" + phoneBookB.getPhoneBookName() + " (B)");
			System.out.println("");

			return;
		}

		String editedInputPhoneBookName = phoneBookNameBase.concat(inputPhoneBookName.toUpperCase());

		System.out.println("Listing latest phone book entries from " + editedInputPhoneBookName + " ...");
		System.out.println("");

		System.out.println("============================================================");

		PhoneBook phoneBook = phoneBookService.readAllEntriesFromSinglePhoneBook(inputPhoneBookName);

		System.out.println("<" + phoneBook.getPhoneBookName() + ">");
		System.out.println("Customer name | Customer phone number");
		System.out.println("************************************************************");

		phoneBook.getEntry().forEach((name, num) -> System.out.println(name + " | " + num));
		System.out.println("");

	}
}
