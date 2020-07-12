package com.somecompany.phoneBook;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.somecompany.phoneBook.model.PhoneBook;

import lombok.extern.slf4j.Slf4j;

/**
 * Entry point for the phone book application.
 * 
 * @author patrick
 *
 */
@SpringBootApplication
@Slf4j
public class PhoneBookApplication implements CommandLineRunner {

	/* Create 2 phone books with sample entries on system init */

	@Autowired
	@Qualifier("phoneBookA")
	private PhoneBook phoneBookA;

	@Autowired
	@Qualifier("phoneBookB")
	private PhoneBook phoneBookB;

	public static void main(String[] args) {
		SpringApplication.run(PhoneBookApplication.class, args).close();
		log.info("End of application.");
	}

	@Override
	public void run(String... args) {
		log.info("Initializing phone book application...");

		/* Create 2 phone books with sample entries */

		System.out.println("");
		System.out.println("Welcome to phone book application!");
		System.out.println("Listing phone book entries...");
		System.out.println("");

		System.out.println("============================================================");
		// Phone book A
		System.out.println("<Phone book A>");
		System.out.println("Customer name | Customer phone number");
		System.out.println("************************************************************");

		phoneBookA.getEntry().forEach((k, v) -> System.out.println(k + " | " + v));

		// Phone book B
		System.out.println("");
		System.out.println("<Phone book B>");
		System.out.println("Customer name | Customer phone number");
		System.out.println("************************************************************");

		phoneBookB.getEntry().forEach((k, v) -> System.out.println(k + " | " + v));

		System.out.println("============================================================");
		System.out.println("");

		boolean isQuit = false;

		while (!isQuit) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please select your action:");
			System.out.println("\"Q\" to quit application");
			String input = scanner.nextLine();
			log.info("input: " + input);

			// Validate the user input

			if (!input.matches("^[a-zA-Z]$")) {
				System.out.println("You may have provided an invalid input, please try again.");
				continue;
			}

			String upperCaseInput = input.toUpperCase();

			// Determine the action to perform

			switch (upperCaseInput) {
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
}
