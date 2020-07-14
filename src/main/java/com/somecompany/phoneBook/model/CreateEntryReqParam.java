package com.somecompany.phoneBook.model;

import lombok.Data;

/**
 * Model for the createEntry request.
 * 
 * @author patrick
 *
 */
@Data
public class CreateEntryReqParam {

	private String phoneBookName;

	private String custName;

	private String custNum;
}
