package com.book.controllers;

import java.util.List;

import javax.servlet.http.Cookie;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.BookDAO;
import com.book.dao.RemarkDAO;
import com.book.model.Book;
import com.book.model.Remark;
import com.book.util.Utils;

@LoginRequired
public class BookController {
	
	public static final int PER_PAGE_LIMIT = 20;

	// 推荐使用bookDAO作为字段名，但这不是必须的，如果要以其它名称作为名字也不需要另外的配置
	// 如果使用多个DAO，则需要写多个@Autowired在每个DAO声明前
	@Autowired
	private BookDAO bookDAO;

	@Autowired
	private RemarkDAO remarkDAO;

	/**
	 * @do 显示所有书
	 * @Modify
	 * @param inv
	 * @param byBookId
	 * @return
	 */
	@Get("")
	public String list(final Invocation inv,
			@Param("byBookId") final long byBookId) {
		final Cookie[] cookies = inv.getRequest().getCookies();
		cookies.toString();
		final List<Book> books = (byBookId <= 0) ? this.bookDAO.find(PER_PAGE_LIMIT)
				: this.bookDAO.find(byBookId, PER_PAGE_LIMIT);
		inv.addModel("books", books);
		return "books";
	}

	/**
	 * @do 跳转到增加书的页面
	 * @Modify
	 * @return
	 */
	@Get("add")
	// 写成add和/add效果是一样的
	public String addBookView() {
		return "add_book";
	}

	/**
	 * @do 数据库操作，增加书
	 * @Modify
	 * @param inv
	 * @param book
	 * @return
	 */
	@Post("add")
	public String addBook(final Invocation inv, final Book book) {
		// 服务器端验证字符的合法性
		if (StringUtils.isEmpty(book.getName())
				|| StringUtils.isEmpty(book.getPrice())
				|| StringUtils.isEmpty(book.getAuthor())) {
			inv.addModel("error", "任何一项都不能为空！");
			return "add_book";
		}
		// 书的名称和作者不能同时在数据库中有重复
		final List<Book> books = this.bookDAO.getBooksByName(book.getName());
		for (final Book b : books) {
			if (b.getAuthor().equals(book.getAuthor())) {
				inv.addModel("error", "书的名称和作者不能同时在数据库中有重复！");
				return "add_book";
			}
		}
		this.bookDAO.save(book);
		return "r:/Booklib/book";
	}

	/**
	 * @do 显示一本书的详细信息，如果带有edit参数就显示编辑页面
	 * @Modify
	 * @param inv
	 * @param id
	 * @param edit
	 * @return
	 */
	@Get("{id:[0-9]+}")
	public String oneBook(final Invocation inv, @Param("id") final long id,
			@Param("edit") final boolean edit) {
		final Book book = this.bookDAO.get(id);
		final List<Remark> remarks = this.remarkDAO.findByBook(id);
		inv.addModel("book", book);
		inv.addModel("remarks", remarks);
		// 放在session中，为添加评论返回错误提供方便。
		inv.getRequest().getSession().setAttribute("book", book);
		inv.getRequest().getSession().setAttribute("remarks", remarks);
		if (edit) {
			return "one_book_edit";
		} else {
			return "one_book";
		}
	}

	/**
	 * @do 从数据库修改一本书的信息，如果成功，直接跳转至该书的详细信息页面，如果失败，跳转至编辑页面
	 * @Modify
	 * @param inv
	 * @param id
	 * @param book
	 * @return
	 */
	@Post("{id:[0-9]+}/update")
	public String updateBook(final Invocation inv, @Param("id") final long id,
			final Book book) {
		// 服务器端验证字符的合法性
		if (StringUtils.isEmpty(book.getPrice())) {
			inv.addModel("error", "不能为空！");
			return "one_book_edit";
		}
		final Book fromDB = this.bookDAO.get(id);
		Utils.updateModel(fromDB, book);
		this.bookDAO.update(fromDB);
		return "r:/Booklib/book/" + id;
	}

	/**
	 * @do 删除一本书
	 * @Modify
	 * @param id
	 * @return
	 */
	@Post("{id:[0-9]+}/delete")
	@AdminRequired
	public String deleteBook(@Param("id") final long id) {
		this.bookDAO.delete(id);
		return "r:/Booklib/book";
	}
}
