package com.book.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.core.Identity;

import com.book.model.Log;

/**
 * @do
 * @Modify
 * @author zhangzuoqiang
 */
@DAO
public interface LogDAO {

	@SQL("select id, user_name, resource_pattern, resource_id, success, remarks, create_time from log")
	public List<Log> find();

	@SQL("select id, user_name, resource_pattern, resource_id, success, remarks, create_time from log where user_name=:1")
	public List<Log> find(String userName);
	
	/**
	 * @do 查询前几行的数据
	 * @Modify
	 * @param limit
	 * @return
	 */
	@SQL("select id, user_name, resource_pattern, resource_id, success, remarks, create_time from log where user_name=:1 order by id desc limit :2")
	public List<Log> find(String userName, int limit);

	@SQL("select id, user_name, resource_pattern, resource_id, success, remarks, create_time from log where user_name=:1 order by id desc id < :2 limit :3")
	public List<Log> find(String userName, long logId, int limit);

	@SQL("insert into log (user_name, resource_pattern, resource_id, success, remarks) values (:1.userName, :1.resourcePattern, :1.resourceId, :1.success, :1.remarks)")
	public Identity save(Log log);

	@SQL("delete from log where id=:1")
	public void delete(long logId);
}
