<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>所有用户</title>
</head>
<body>
	<h1 style="text-align: center">所有用户</h1>
	<div align="right">
		<a href="/Booklib/user/add">增加一个用户</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
			href="/Booklib/book">返回书目列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div align="center">
		<table border="1">
			<tr>
				<th>用户ID</th>
				<th>登录名</th>
				<th>姓名</th>
				<th>用户组别</th>
				<th>创建时间</th>
			</tr>
			<c:forEach var="item" items="${users}">
				<tr>
					<td>${item.id }</td>
					<td><a href="/Booklib/user/${item.id }">${item.loginName }</a></td>
					<td>${item.name }</td>
					<td><c:choose>
							<c:when test="${item.groups == 0 }">普通用户</c:when>
							<c:otherwise>管理用户</c:otherwise>
						</c:choose></td>
					<td>${item.createTime }</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>