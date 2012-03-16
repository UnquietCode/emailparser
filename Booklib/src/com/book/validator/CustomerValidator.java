package com.book.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.book.model.Customer;

public class CustomerValidator implements Validator {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		// just validate the Customer instances
		return Customer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	}
}