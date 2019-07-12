<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div id="header" align="center">
    <div style="position: relative;">
		<shiro:user>
			<div style="padding: 5px;">
				<shiro:authenticated>
					<ul class="nav nav-tabs">
						<li><a href="${ctx }/index.html">Home</a></li>
						<c:forEach items="${menus }" var="m">
						    <li class="dropdown">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fn:escapeXml(m.description) }<span class="caret"></span></a>
								<ul class="dropdown-menu">
									<c:forEach items="${m.childs }" var="mc">
										<li><a href="${ctx }${mc.url }">${fn:escapeXml(mc.description) }</a></li>
									</c:forEach>
								</ul>
							</li>
						</c:forEach>
					</ul>
				</shiro:authenticated>
			</div>
			<div align="right" style="padding-right: 10px;">
				<span>Hello, <shiro:principal property="name" /></span>
				<shiro:authenticated>
					<a href="${ctx}/logout.html" class="btn btn-danger" role="button"><spring:message code='PUB.logout' /></a>
				</shiro:authenticated>
				<shiro:notAuthenticated>
					<a href="${ctx}/login.html" class="btn btn-primary" role="button"><spring:message code='PUB.login' /></a>
				</shiro:notAuthenticated>
				<label>语言</label>
			    <select id="lang" onchange="changeLang(this)">
			      <option value="en_US">English</option>
			      <option value="zh_CN">简体中文</option>
			      <option value="zh_TW">繁体中文</option>
			    </select>
			</div>
		</shiro:user>
		<shiro:guest>
			<div align="right" style="padding-right: 10px;">
				<a href="${ctx}/login.html" class="btn btn-primary" role="button"><spring:message code='PUB.login' /></a>
				<label>语言</label>
			    <select id="lang" class="select" onchange="changeLang(this)">
			      <option value="en_US" selected="selected">English</option>
			      <option value="zh_CN">简体中文</option>
			      <option value="zh_TW">繁体中文</option>
			    </select>
			</div>
		</shiro:guest>
    </div>
</div>

<script type="text/javascript">
	//关闭click.bs.dropdown.data-api事件，使顶级菜单可点击
// 	$(document).off('click.bs.dropdown.data-api');
	//自动展开
	$('.nav .dropdown').mouseenter(function(){
		$(this).addClass('open');
	});
	//自动关闭
	$('.nav .dropdown').mouseleave(function(){
		$(this).removeClass('open');
	});
// 	//切换语言
	function changeLang(obj){
		var lang = $(obj).val();
		var search = window.location.search||"?";
		if(search.indexOf("lang=")!=-1){
			search = search.replace(/lang=\w+/,"lang="+lang);
		}else{
			if(search==="?"){
				search = search+"lang="+lang;
			}else{
				search = search+"&lang="+lang;
			}
		}
		window.location.href=search;
	}
	$("#lang").val($.cookie('Language'));
</script>
