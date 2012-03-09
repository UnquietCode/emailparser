<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改书籍信息</title>
</head>
<body>
	<h1 style="text-align: center">修改书的详细信息</h1>
	<div align="right">
		<a href="/Booklib/book">返回书目列表</a>
	</div>
	<div align="center">
		<form action="/Booklib/book/${book.id }/update" method="post">
			书的ID：${book.id }<br> <input type="hidden" value="${book.id }"
				name="id" /> 书的名称：${book.name }<br> 书的价格：<input type="text"
				value="${book.price }" name="price" /><br> 书的作者：${book.author
			}<br> 书的创建时间：${book.createTime }<br> <input type="submit"
				value="提交修改" /><br> <font color=red>${error }</font>
		</form>
	</div>
</body>
</html>