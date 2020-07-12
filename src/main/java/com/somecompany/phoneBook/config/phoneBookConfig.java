package com.somecompany.phoneBook.config;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.somecompany.phoneBook.model.PhoneBook;

/**
 * Configuration for the phone book application.<br/>
 * It initializes the 2 phone books.
 * 
 * @author patrick
 *
 */
@Configuration
public class phoneBookConfig {

	/* Sample entries for the phone books */

	@Value("${sampleEntryA.name}")
	private String sampleEntryAName;

	@Value("${sampleEntryA.number}")
	private String sampleEntryANumber;

	@Value("${sampleEntryB.name}")
	private String sampleEntryBName;

	@Value("${sampleEntryB.number}")
	private String sampleEntryBNumber;

	@Value("${sampleEntryCommon.name}")
	private String sampleEntryCommonName;

	@Value("${sampleEntryCommon.number}")
	private String sampleEntryCommonNumber;

	@Value("${phoneBookA.name}")
	private String phoneBookAName;

	@Value("${phoneBookB.name}")
	private String phoneBookBName;

	@Bean("phoneBookA")
	public PhoneBook initPhoneBookA() {
		Map<String, String> entry = new TreeMap<>();
		entry.put(sampleEntryCommonName, sampleEntryCommonNumber);
		entry.put(sampleEntryAName, sampleEntryANumber);
		return new PhoneBook(phoneBookAName, entry);
	}

	@Bean("phoneBookB")
	public PhoneBook initPhoneBookB() {
		Map<String, String> entry = new TreeMap<>();
		entry.put(sampleEntryCommonName, sampleEntryCommonNumber);
		entry.put(sampleEntryBName, sampleEntryBNumber);
		return new PhoneBook(phoneBookBName, entry);
	}
}
