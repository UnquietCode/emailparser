package com.book.dao;

import java.util.List;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.core.Identity;

import com.book.model.Remark;

@DAO
public interface RemarkDAO {

	@SQL("select count(*) from remark where book_id=:1")
	public int rows(long bookId);

	// 标注一个@SQL，写入你的sql语句
	// 不能写select * from remark，这样的后果可能会因为数据库增加了一个字段，但Remark没有相应字段的属性，Jade将抛出异常
	// 参数以冒号开始，:1表示第一个参数
	@SQL("select id, user_name, book_id, essay, create_time from remark where book_id=:1 order by id desc")
	public List<Remark> findByBook(long bookId);

	@SQL("delete from remark where book_id=:1")
	public void deleteByBook(long bookId);

	// 返回int表示变更的条数，就这个示例而言，应该就是返回1
	@SQL("delete from remark where id=:1")
	public int delete(long remarkId);

	// 返回Idenity用于获取自增生成的那个id
	// :1.userName表示第一个参数的userName属性
	@SQL("insert into remark (user_name, book_id, essay) values (:1.userName, :1.bookId, :1.essay)")
	public Identity save(Remark remark);
}
