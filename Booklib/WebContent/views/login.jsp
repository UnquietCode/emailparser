<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登陆</title>
</head>
<body>
	<h1 style="text-align: center">请先登录</h1>
	<div align="center">
		<form action="/Booklib/login" method="post">
			登录名：<input type="text" value="${loginName }" name="loginName" /><br>
			&nbsp;&nbsp;&nbsp;密码：<input type="password" value="" name="password" /><br> 
			<input type="submit" value="登陆" /><br><font color=red>${error}</font>
		</form>
	</div>
</body>
</html>