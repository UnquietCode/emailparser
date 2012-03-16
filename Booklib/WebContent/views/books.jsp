<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://weibo.com/zzuoqiang/page" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浏览书目</title>
</head>
<body>
	<h1 style="text-align: center">所有书籍</h1>
	<div align="center">
		<a href="/Booklib/book/add">增加一本书</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/user">所有用户</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/user/add">增加一个新用户</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/user/my">用户中心</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/logs">操作日志</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/user/logout">退出登录</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<table border="1">
			<tr>
				<th>书目ID</th>
				<th>书名</th>
				<th>价格</th>
				<th>作者</th>
				<th>添加时间</th>
				<th>操作</th>
			</tr>
			<c:forEach var="item" items="${books}">
				<tr>
					<td>${item.id }</td>
					<td><a href="/Booklib/book/${item.id }">${item.name }</a></td>
					<td>${item.price }</td>
					<td>${item.author }</td>
					<td>${item.createTime }</td>
					<td>
						<form action="/Booklib/book/${item.id }/delete" method="post">
							<input type="hidden" value="${item.id }" name="id" /> <input
								type="submit" value="删除">
						</form>
					</td>
				</tr>
			</c:forEach>
			<!-- 分页 -->
			<tr bgcolor="#CCCCCC">
				<td colspan="8" align="right" valign="middle">
					<!-- 注意了，两个字母实现通用分页 --> <t:p />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>