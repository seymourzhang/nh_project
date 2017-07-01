<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<head>
<meta charset="utf-8" />
<%@ include file="inc.jsp"%>
<script src="<%=path %>/framework/jquery/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="<%=path %>/framework/jquery/jquery.pulsate.min.js" type="text/javascript"></script>
<script src="<%=path %>/framework/layer/layer.js" type="text/javascript"></script>

</head>
<body class="page-header-fixed" style="overflow: hidden;background-image: url(../image/bg.jpg) ">
	<!-- BEGIN HEADER -->
	<%@ include file="head.jsp"%>
	<!-- END HEADER -->
	<!-- BEGIN CONTAINER -->
	<div class="page-container row-fluid" >
		<!-- BEGIN SIDEBAR -->
		<%@ include file="left.jsp"%>
		<!-- END SIDEBAR -->
		<!-- BEGIN PAGE -->

		<div class="page-content" style="padding-top: 4px; ">

			<!-- BEGIN PAGE CONTAINER-->
			<iframe name="mainFrame" id="mainFrame" frameborder="0"  scrolling="yes"  marginheight="0" marginwidth="0" src="<%=path%>/login/tab" style="background-color: #fff;margin:0 auto;width:100%;"></iframe>
		</div>

		<!-- END PAGE -->
	</div>
	<!-- END CONTAINER -->
	<%--   <%@ include file="bottom.jsp"%> --%>
	<script>
		jQuery(document).ready(function() {
			App.init(); // initlayout and core plugins
				
			 	$("#lm1").attr("class","active");
				$("#se1").attr("class","selected");
				$("#z101").attr("class","active");
				$("#op1").attr("class","arrow open"); 
				$(".page-sidebar .sidebar-toggler").click(); 
				
				layer.load(1, {
					  shade: [0.3,'#fff'], //0.1透明度的白色背景
					  time: 2000
					});
				
		});
		
	
	function cmainFrame(){
	    var isIE = App.isIE();
	    var hsheight = (isIE == true)?45:51;

		var hmain = document.getElementById("mainFrame");
		var bheight = document.documentElement.clientHeight;
		hmain.style.width = '100%';
		hmain.style.height = (bheight  - hsheight + 8) + 'px';
	}
	cmainFrame();
	window.onresize=function(){  
		cmainFrame();
	};
/* 	var i = 20;  
	function remainTime(){  
	    if(i==0){  
            $('#header_notification_bar').pulsate({
                color: "#66bce6",
                repeat: 5
            });
            i=20;
	    }  
	    i--; 
	    setTimeout(remainTime(),1000);  
	}  
	remainTime(); */
	
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
