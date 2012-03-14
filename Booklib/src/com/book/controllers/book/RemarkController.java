package com.book.controllers.book;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.book.controllers.AdminRequired;
import com.book.controllers.LoginRequired;
import com.book.dao.RemarkDAO;
import com.book.model.Remark;
import com.book.model.User;

/**
 * @do 书本评论控制器
 * @Modify
 * @author zhangzuoqiang
 */
@LoginRequired
@Path("{id:[0-9]+}/remark")
public class RemarkController {

	@Autowired
	private RemarkDAO remarkDao;

	/**
	 * @do 增加一个评论
	 * @Modify
	 * @param inv
	 * @param remark
	 * @return
	 */
	@Post("add")
	public String add(final Invocation inv, final Remark remark) {
		if (StringUtils.isEmpty(remark.getEssay())) {
			inv.addModel("book",
					inv.getRequest().getSession().getAttribute("book"));
			inv.addModel("remarks",
					inv.getRequest().getSession().getAttribute("remarks"));
			inv.addModel("remark_error", "评论内容不能为空");
			return "one_book";
		}
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		remark.setUserName(user.getLoginName());
		this.remarkDao.save(remark);
		return "r:/Booklib/book/" + remark.getBookId();
	}

	/**
	 * @do 清除所有评论
	 * @Modify
	 * @param bookId
	 * @return
	 */
	@Post("deleteAll")
	@AdminRequired
	public String clear(@Param("bookId") final long bookId) {
		this.remarkDao.deleteByBook(bookId);
		return "r:/Booklib/book/" + bookId;
	}

	/**
	 * @do 删除指定ID的评论
	 * @Modify
	 * @param bookId
	 * @param remarkId
	 * @return
	 */
	@Post("{remarkId}/delete")
	@AdminRequired
	public String delete(@Param("bookId") final long bookId,
			@Param("remarkId") final String remarkId) {
		this.remarkDao.delete(Long.parseLong(remarkId));
		return "r:/Booklib/book/" + bookId;
	}
}
