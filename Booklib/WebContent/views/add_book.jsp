<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加一本书</title>
</head>
<body>
	<h1 style="text-align: center">增加一本书</h1>
	<div align="right">
		<a href="/Booklib/book">返回书目列表</a>
	</div>
	<div align="center">
		<form action="/Booklib/book/add" method="post">
			书的名称：<input type="text" value="" name="name" /><br>
			书的价格：<input type="text" value="" name="price" /><br>
			书的作者：<input type="text" value="" name="author" /><br>
			<input type="submit" value="增加" /><br>
			<font color=red>${error }</font>
		</form>
	</div>
</body>
</html>