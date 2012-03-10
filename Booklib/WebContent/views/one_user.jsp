<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看用户信息</title>
</head>
<body>
	<h1 style="text-align: center">查看用户信息</h1>
	<div align="right">
		<a href="/Booklib/user/${user.id }?edit=true">修改该用户的信息</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/Booklib/book">返回书目列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div align="center">
		ID：${user.id }<br>
		登录名：${user.loginName }<br>
		姓名：${user.name}<br>
		用户组别：
		<c:choose>
			<c:when test="${user.groups == 0 }">普通用户</c:when>
			<c:otherwise>管理用户</c:otherwise>
		</c:choose>
		<br> 创建时间：${user.createTime }<br>
		<form action="/Booklib/user/${user.id }/delete" method="post">
			<input type="submit" value="注销此用户" />
		</form>
	</div>
</body>
</html>