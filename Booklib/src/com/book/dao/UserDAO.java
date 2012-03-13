package com.book.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.core.Identity;

import com.book.model.User;

@DAO
public interface UserDAO {

	@SQL("select count(*) from user")
	public int rows();

	@SQL("select id, name, password, login_name, create_time, groups from user where login_name=:1")
	public User getByLoginName(String loginName);

	@SQL("select id, name, password, login_name, create_time, groups from user where id=:1")
	public User get(long userId);

	/**
	 * 分页显示
	 * 
	 * @param preLimit
	 * @param limit
	 * @return
	 */
	@SQL("select id, name, password, login_name, create_time, groups from user order by id desc limit :1, :2")
	public List<User> find(int preLimit, int limit);

	@SQL("delete from user where id=:1")
	public void delete(long userId);

	@SQL("insert into user (login_name, password, name, groups) values (:1.loginName, :1.password, :1.name, :1.groups)")
	public Identity save(User user);

	@SQL("update user set login_name=:1.loginName, password=:1.password, name=:1.name where id=:1.id")
	public void update(User user);
}
