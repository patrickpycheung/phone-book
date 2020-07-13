package com.somecompany.phoneBook;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.somecompany.phoneBook.model.PhoneBook;
import com.somecompany.phoneBook.service.PhoneBookService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		PhoneBookApplication.class }, initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("dev")
public class PhoneBookTest {
	@Autowired
	private PhoneBookService phoneBookService;

	@Autowired
	private PhoneBookTestUtil phoneBookTestUtil;

	@Value("${phoneBookA.name}")
	private String phoneBookAName;

	@Value("${phoneBookB.name}")
	private String phoneBookBName;

	@Autowired
	@Qualifier("phoneBookA")
	private PhoneBook phoneBookA;

	@Autowired
	@Qualifier("phoneBookB")
	private PhoneBook phoneBookB;

	/**
	 * Test creating a new entry in phoneBookA.<br/>
	 * No existing entry is found in all phone books.
	 */
	@Test
	public void shouldBeAbleToCreateANewEntryInPhoneBookAIfNoneExist() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookService.createOrUpdateEntry(phoneBookAName, randomCustName, randomCustNum);

		assertThat(phoneBookA.getEntry().get(randomCustName).equals(randomCustNum));
		assertThat(phoneBookB.getEntry().get(randomCustName) == null);
	}

	/**
	 * Test updating an existing entry in phoneBookA.<br/>
	 * The entry does not exist in the other phone book yet.
	 */
	@Test
	public void shouldBeAbleToUpdateEntryInPhoneBookAIfAlreadyExist() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);

		phoneBookService.createOrUpdateEntry(phoneBookAName, randomCustName, randomCustNum);

		assertThat(phoneBookA.getEntry().get(randomCustName).equals(randomCustNum));
		assertThat(phoneBookB.getEntry().get(randomCustName) == null);
	}

	/**
	 * Test updating an existing entry in phoneBookA.<br/>
	 * The entry exists in the other phone book as well.
	 */
	@Test
	public void shouldBeAbleToUpdateEntryInPhoneBookAIfAlreadyExistAndUpdatePhoneBookBIfAlreadyExist() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);
		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		phoneBookService.createOrUpdateEntry(phoneBookAName, randomCustName, randomCustNum);

		assertThat(phoneBookA.getEntry().get(randomCustName).equals(randomCustNum));
		assertThat(phoneBookB.getEntry().get(randomCustName).equals(randomCustNum));
	}

	/**
	 * Test creating a new entry in phoneBookA.<br/>
	 * The entry exists in the other phone book as well.
	 */
	@Test
	public void shouldBeAbleToCreateANewEntryInPhoneBookAIfNoneExistAndUpdatePhoneBookBIfAlreadyExist() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		phoneBookService.createOrUpdateEntry(phoneBookAName, randomCustName, randomCustNum);

		assertThat(phoneBookA.getEntry().get(randomCustName).equals(randomCustNum));
		assertThat(phoneBookB.getEntry().get(randomCustName).equals(randomCustNum));
	}

	/**
	 * Test reading all entries in all phone books.<br/>
	 */
	@Test
	public void shuoldBeAbleToReadAllEntriesInAllPhoneBooks() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);
		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		List<PhoneBook> phoneBookList = phoneBookService.readAllEntriesFromAllPhoneBooks();

		assertThat(phoneBookList.contains(phoneBookA));
		assertThat(phoneBookList.contains(phoneBookB));
	}

	/**
	 * Test reading all entries in a single phone book.<br/>
	 */
	@Test
	public void shouldBeAbleToReadAllEntriesInSinglePhoneBook() {
		phoneBookA.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);

		PhoneBook phoneBook = phoneBookService.readAllEntriesFromSinglePhoneBook(phoneBookAName);

		assertThat(phoneBook == phoneBookA);
	}

	/**
	 * Test reading unique entries from all phone books.<br/>
	 */
	@Test
	public void shouldBeAbleToReadUniqueEntriesInAllPhoneBooks() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		String anotherRandomCustName = phoneBookTestUtil.getRandomCustName();
		String anotherRandomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);
		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		phoneBookA.getEntry().put(anotherRandomCustName, anotherRandomCustNum);
		phoneBookB.getEntry().put(anotherRandomCustName, anotherRandomCustNum);

		PhoneBook phoneBook = phoneBookService.readUniqueEntriesFromAllPhoneBooks();

		assertThat(phoneBook.getEntry().containsKey(randomCustName));
		assertThat(phoneBook.getEntry().get(randomCustName).equals(randomCustNum));

		assertThat(phoneBook.getEntry().containsKey(anotherRandomCustName));
		assertThat(phoneBook.getEntry().get(anotherRandomCustName).equals(anotherRandomCustNum));
	}

	/**
	 * Test deleting an entry from a single phone book, where the phone book contains the entry.
	 */
	@Test
	public void shouldBeAbleToDeleteEntryFromSinglePhoneBook() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookA.getEntry().put(randomCustName, randomCustNum);
		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		phoneBookService.deleteEntryFromSinglePhoneBook(phoneBookAName, randomCustName);

		// Should have deleted the entry in the specified phone book
		assertThat(phoneBookA.getEntry().get(randomCustName) == null);
		// Should have no change to the other phone book
		assertThat(phoneBookB.getEntry().get(randomCustName).equals(randomCustNum));
	}

	/**
	 * Test deleting an entry from a single phone book, where the phone book does no contain the entry.
	 */
	@Test
	public void shouldBeAbleToHandleNoEntryInPhoneWhenAttemptToDelete() {
		phoneBookA.getEntry().clear();
		phoneBookB.getEntry().clear();

		String randomCustName = phoneBookTestUtil.getRandomCustName();
		String randomCustNum = phoneBookTestUtil.getRandomCustNum();

		phoneBookB.getEntry().put(randomCustName, randomCustNum);

		phoneBookService.deleteEntryFromSinglePhoneBook(phoneBookAName, randomCustName);

		// No error if it can reach this step
		assertThat(phoneBookA.getEntry().get(randomCustName) == null);
		// Should have no change to the other phone book
		assertThat(phoneBookB.getEntry().get(randomCustName).equals(randomCustNum));
	}
}
