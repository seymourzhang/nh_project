<%--
  Created by IntelliJ IDEA.
  User: Raymon
  Date: 1/17/2017
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<%@ include file="../../framework/inc/system.jsp"%>
	</head>
<body>

	<div id="layerBackground" style="position:absolute; width:100%; height:100%; z-index:-1">
		<img src="<%=path %>/modules/user/image/background.jpg" height="100%" width="100%"/>
	</div>

<%@ include file="../../framework/region_top.jsp"%>

	<%--用户登录区域--%>
	<div class="font s13"
		 id="center"
		 style="border:solid #c5c5c5 1px;width:350px ;height:320px;align:center;position: absolute;top:30%;right:8%;">
		<form class="bd" id="loginForm"  method="POST" >
			<p class="font s32 blue bold"
			   style="height:80px; line-height:80px; overflow:hidden;text-align: center ">
				用户登录
			</p>
			<table cellspacing="0" border="0" style="margin:auto;width: 90%;">
				<tr>
					<td style="height:45px;border: 1px solid #c5c5c5;background:#f2f2f3;text-align:center;">
						<img src="<%=path%>/modules/user/image/loginuser.png" style="vertical-align:middle;">
					</td>
					<td style="height:45px;border: 1px solid #c5c5c5;">
						<input class="form-control"
							   type="text"
							   id="userName"
							   name="user_code"
							   placeholder=" 请输入账号"
							   autocomplete="on"
							   value=""
							   style="width:100%;height:45px;border:1px;">
					</td>
				</tr>
				<tr>
					<td colspan="2" style="height:30px;"></td>
				</tr>
				<tr>
					<td style="height:45px;border: 1px solid #c5c5c5;background:#f2f2f3;text-align:center;">
						<img src="<%=path%>/modules/user/image/loginpassword.png" style="vertical-align:middle;">
					</td>
					<td style="height:45px;border: 1px solid #c5c5c5;">
						<input class="form-control"
							   type="password"
							   onkeydown="javascript:login.enterSumbit()"
							   name="user_password"
							   id="password"
							   placeholder=" 请输入密码"
							   value=""
							   style="width:100%;height:45px;border:1px;">
					</td>
				</tr>
				<tr>
					<td colspan="2" style="height:30px;"></td>
				</tr>
				<tr>
					<td colspan="2" style="height:30px;">
						<button type="button"
								class="btn blue"
								style="width:100%;vertical-align:middle;"
								onclick="javascript:login.systemLogin()">
							登&nbsp;&nbsp;&nbsp;&nbsp;录
						</button>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="../../framework/region_bottom.jsp"%>
	<%@ include file="../../framework/inc/framework.jsp"%>
	<script src="<%=path %>/modules/user/js/login.js"></script>
</body>
</html>