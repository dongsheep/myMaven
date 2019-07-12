<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="common.jsp"%>
<title><spring:message code="PUB.index" /></title>
</head>
<body>
	<shiro:authenticated>
		<div align="center">
			<h1><spring:message code="PUB.welcome" /></h1>
		</div>
	</shiro:authenticated>
</body>
</html>
