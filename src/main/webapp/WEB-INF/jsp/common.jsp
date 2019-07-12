<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--解决EL表达式无效 servlet2.3以下需要 --%>
<%-- <%@ page isELIgnored="false" %> --%>
<%--引入JSTL核心标签库 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.cookie.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/myStyle.css">

<!-- bootstarp和easyui一起用的时候，注意引入先后顺序，先bootstarp再easyui -->
<script type="text/javascript" src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>  
<link rel="stylesheet" type="text/css" href="${ctx}/static/bootstrap/css/bootstrap-theme.min.css">   
<link rel="stylesheet" type="text/css" href="${ctx}/static/bootstrap/css/bootstrap.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1.0">  

<!-- bootstrap table -->
<script type="text/javascript" src="${ctx}/static/bootstrap/js/bootstrap-table.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/bootstrap/css/bootstrap-table.min.css">
<!-- 动态加载bootstrap table语言包 -->
<script type="text/javascript" id="changeLangJs">
// 先删除以前的语言包
$("script[src='${ctx}/static/bootstrap/js/bootstrap-table-en-US.min.js']").remove();
$("script[src='${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.min.js']").remove();
$("script[src='${ctx}/static/bootstrap/js/bootstrap-table-zh-TW.min.js']").remove();
// 判断当前选择的语言，重新添加语言包
var lang = $.cookie('Language');
if ("en_US"==lang) {
	createLanguage("${ctx}/static/bootstrap/js/bootstrap-table-en-US.min.js");
}else if ("zh_CN"==lang) {
	createLanguage("${ctx}/static/bootstrap/js/bootstrap-table-zh-CN.min.js");
}else if ("zh_TW"==lang) {
	createLanguage("${ctx}/static/bootstrap/js/bootstrap-table-zh-TW.min.js");
}

function createLanguage(src){
	var oScript= document.createElement("script");
	oScript.type = "text/javascript";   
	oScript.src= src;   
	$("#changeLangJs").after(oScript);
}
</script>

<script type="text/javascript" src="${ctx}/static/vue.min.js"></script>
