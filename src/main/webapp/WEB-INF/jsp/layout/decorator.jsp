<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title><sitemesh:write property='title' /></title>
<sitemesh:write property='head' />
</head>
<body>
	<%@ include file="/WEB-INF/jsp/layout/header.jsp"%>
	<div style="width:100%; padding: 5px;">
		<sitemesh:write property='body' />
	</div>
	<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>
</body>
</html>