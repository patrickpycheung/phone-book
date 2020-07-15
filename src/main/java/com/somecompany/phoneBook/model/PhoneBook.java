package com.somecompany.phoneBook.model;

import java.util.Map;
import java.util.TreeMap;

import lombok.Data;

/**
 * Model for the phone book.
 * 
 * @author patrick
 *
 */
@Data
public class PhoneBook {

	private String phoneBookName;

	private Map<String, String> entry;

	public PhoneBook(String phoneBookName, Map<String, String> entry) {
		this.phoneBookName = phoneBookName;

		this.entry = new TreeMap<>();
		this.entry.putAll(entry);
	}
}
