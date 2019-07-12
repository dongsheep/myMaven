<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="common.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code='PUB.login' /></title>
</head>
<body>
<!-- 	<myTag>Welcome to the dong project</myTag> -->
	<div align="center" style="margin-top: 10px;">
		<form action="${ctx }/login.do" method="post">
			<div>
				<spring:message code="User.name" /><input type="text" id="username" name="username">
			</div>
			<div>
				<spring:message code="User.password" /><input type="password" id="password" name="password">
			</div>
			<div>
				<input type="submit" id="login" class="btn btn-primary" value="<spring:message code='PUB.login' />" />
				<input type="button" id="regist" class="btn btn-info" value="<spring:message code='PUB.regist' />" />
			</div>
		</form>
	</div>
	
	<script type="text/javascript">
		// 跳转注册用户页面
		$("#regist").click(function(){
			
		});
	</script>
</body>
</html>