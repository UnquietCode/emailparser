<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加用户</title>
</head>
<body>
	<h1 style="text-align: center">增加一名用户</h1>
	<div align="right">
		<a href="/Booklib/book">返回书目列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
			href="/Booklib/user">返回用户列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div align="center">
		<form action="/Booklib/user/add" method="post">
			登录名：<input type="text" value="" name="loginName" /><br> 密码： <input
				type="password" value="" name="password" /><br> 确认密码：<input
				type="password" value="" name="password2" /><br> 用户组别： <select
				name="groups">
				<option value="0">普通用户</option>
				<option value="1">管理用户</option>
			</select><br> 姓名：<input type="text" value="" name="name" /><br> <input
				type="submit" value="增加" /><br> <font color=red>${error
				}</font>
		</form>
	</div>
</body>
</html>