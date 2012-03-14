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
import com.book.model.Page;
import com.book.model.Remark;
import com.book.model.User;
import com.book.util.WebUtil;

/**
 * @do 书本评论控制器
 * @Modify
 * @author zhangzuoqiang
 */
@LoginRequired
@Path("{id:[0-9]+}/remark")
public class RemarkController {

	private static final int PER_PAGE_LIMIT = 10;

	@Autowired
	private RemarkDAO remarkDAO;

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

			int pageIndex = WebUtil
					.getIntByRequestParament(inv, "pageIndex", 1);

			// 构造一个page对象，第1个参数是当前页，第2个参数是该页最大记录数，第3个是页码上的连接地址
			Page page = new Page(pageIndex, PER_PAGE_LIMIT, remark.getBookId());
			page.setTotalCount(this.remarkDAO.rows(Long.parseLong(remark
					.getBookId())));
			// 出来后的page对象已经有了总记录数了，自然就有了页码信息
			inv.addModel("page", page);

			inv.addModel("remark_error", "评论内容不能为空");
			return "/views/one_book.jsp";
		}
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		remark.setUserName(user.getLoginName());
		this.remarkDAO.save(remark);
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
		this.remarkDAO.deleteByBook(bookId);
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
		this.remarkDAO.delete(Long.parseLong(remarkId));
		return "r:/Booklib/book/" + bookId;
	}
}
