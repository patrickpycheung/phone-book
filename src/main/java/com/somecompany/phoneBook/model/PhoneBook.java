package com.somecompany.phoneBook.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for the phone book.
 * 
 * @author patrick
 *
 */
public class PhoneBook {

	@Getter
	@Setter
	private String phoneBookName;

	@Getter
	@Setter
	private Map<String, String> entry;

	public PhoneBook(String phoneBookName, Map<String, String> entry) {
		this.phoneBookName = phoneBookName;

		this.entry = new TreeMap<>();
		this.entry.putAll(entry);
	}
}
