<%--
  Created by IntelliJ IDEA.
  User: LeLe
  Date: 2017/3/16
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String error_code = request.getParameter("error_code");//用request得到
    String text = (error_code.equals("404"))?"建设中...":"系统错误，请联系管理员！";
%>
<body>
    <%--(<%=name%>)系统错误，请联系管理员！--%>
    <%=text%>
</body>
</html>
