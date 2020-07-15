package com.somecompany.phoneBook.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Util for testing.
 * 
 * @author patrick
 *
 */
@Component
@Slf4j
public class PhoneBookTestUtil {

	@Value("${cust.name.limit}")
	private String custNameLimit;

	@Value("${cust.num.limit}")
	private String custNumLimit;

	/**
	 * Generate random customer name.
	 * 
	 * @return generatedCustName
	 */
	public String getRandomCustName() {
		String generatedCustName = RandomStringUtils.randomAlphanumeric(Integer.valueOf(custNameLimit));
		log.info("generatedCustName: " + generatedCustName);
		return generatedCustName;
	}

	/**
	 * Generate random customer phone number.
	 * 
	 * @return generatedCustNum
	 */
	public String getRandomCustNum() {
		String generatedCustNum = RandomStringUtils.randomAlphanumeric(Integer.valueOf(custNumLimit));
		log.info("generatedCustNum: " + generatedCustNum);
		return generatedCustNum;
	}
}
