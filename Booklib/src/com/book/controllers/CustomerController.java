package com.book.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.CustomerDAO;

/**
 * 
 * @author zhangzuoqiang
 * @Email z.zuoqiang@gmail.com
 * @time 2012-3-16 上午11:42:33
 * @do
 * @Modify
 */
@LoginRequired
public class CustomerController {

	private static final int PER_PAGE_LIMIT = 10;

	@Autowired
	private CustomerDAO customerDAO;

	
}