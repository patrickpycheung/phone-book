package com.somecompany.phoneBook.model;

import lombok.Data;

@Data
public class CreateEntryReqParam {

	private String phoneBookName;

	private String custName;

	private String custNum;
}
