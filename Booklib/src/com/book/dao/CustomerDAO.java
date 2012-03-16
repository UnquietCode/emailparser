package com.book.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.core.Identity;

import com.book.model.Customer;

@DAO
public interface CustomerDAO {

	@SQL("select count(*) from customer")
	public int rows();

	@SQL("select id, user_name, address, newsletter, sex, country, commons, create_time from customer where id = :1")
	public Customer get(long customerId);

	@SQL("select id, user_name, address, newsletter, sex, country, commons, create_time from customer order by id desc limit :1, :2")
	public List<Customer> find(int preLimit, int limit);

	@SQL("update customer set user_name=:1.userName, address=:1.address, newsletter=:1.newsletter, sex=:1.sex, country=:1.country , commons=:1.commons where id=:1.id")
	public void update(Customer customer);

	@SQL("insert into customer (user_name, address, newsletter, sex, country, commons) values (:1.userName, :1.address, :1.newsletter, :1.sex, :1.country, :1.commons)")
	public Identity save(Customer customer);

	@SQL("delete from customer where id = :1")
	public void delete(long customerId);
}
