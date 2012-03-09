<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户信息</title>
</head>
<body>
	<h1 style="text-align: center">修改用户详细信息</h1>
	<div align="right">
		<a href="/Booklib/user">返回用户列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
			href="/Booklib/book">返回书目列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	<div align="center">
		<form action="/Booklib/user/${user.id }/info/update" method="post">
			ID：${user.id }<br> <input type="hidden" value="${user.id }"
				name="id" /> 登录名：${user.loginName }<br> 姓名：<input type="text"
				value="${user.name }" name="name" /><br> 用户组别：
			<c:choose>
				<c:when test="${user.groups == 0 }">普通用户</c:when>
				<c:otherwise>管理用户</c:otherwise>
			</c:choose>
			<br> 创建时间：${user.createTime }<br> <input type="submit"
				value="提交修改" /><br>
		</form>
		<br> <br> <br>

		<form action="/Booklib/user/${user.id }/password/update" method="post">
			<input type="hidden" value="${user.id }" name="id" /> 旧密码： <input
				type="password" value="" name="old_password" /><br> 密码： <input
				type="password" value="" name="password" /><br> 确认密码：<input
				type="password" value="" name="password2" /><br> <input
				type="submit" value="提交修改" /><br>
		</form>
		<br> <font color=red>${error }</font>
	</div>
</body>
</html>