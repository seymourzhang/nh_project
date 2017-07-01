<%--
  Created by IntelliJ IDEA.
  User: Seymour
  Date: 2016/11/2
  Time: 11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <%@ include file="../../framework/inc.jsp"%>
    <link rel="stylesheet" href="<%=path%>/modules/breed/css/jquery.fileupload.css">
    <%--<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->--%>
    <script src="<%=path%>/framework/jquery/jquery.ui.widget.js"></script>
    <!-- The basic File Upload plugin -->
    <script src="<%=path%>/framework/jquery/jquery.fileupload.js"></script>
<!--     <link href="<%=path%>/framework/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" /> -->
</head>
<script>
    var isRead="${pd.write_read}";//菜单是否只读
    var count0rg=-1;
    var asyncFlag = false;
    var num=2;
    var orgLevel = 2;

    function OrgSearch(count0rg,num){
    	if(count0rg !=-1){
            $('#uploadButtonFacade').hide();
            $('#fileupload').fileupload({
                url: path + "/analyze/upload?upload_file_type=3&farm_id="+$("#orgId" + (count0rg - 1)).val().split(",")[1]+"&farm_name="+$("#orgId" + (count0rg - 1)).val().split(",")[2],
                autoUpload: true,
                add: function (e, data){
                    if(data.originalFiles.length>0){
                    }
                    $('#uploadButtonFacade').show();
                    $('#uploadButtonFacade').button('loading');
                    $('#uploadButton').hide();
                    data.submit();
                },
                done: function (e, data) {
                    $('#uploadButtonFacade').button('reset');
                    $('#uploadButtonFacade').hide();
                    $('#uploadButton').show();
                    var rt = '(' + data.result + ')';
                    var json = eval(rt);
    //                reFlushData(currTabName);
                    getFiles();
                    if(json.success == true){
                        layer.msg("文件上传成功");
                    } else {
                            layer.msg(json.msg);

                    }
                    return;
                },
                fail:function (e, data) {
                    layer.msg(e);
                    return;
                }
            });
            getFiles();
    	}
    }
</script>
<body style="background-color: #ffffff;">
    <div id="page-content"  class="clearfix">
        <div class="row-fluid" style="background:#e7e5e5;padding-top: 10px;">
            <div class="span8" >
					<div style="padding-left: 10px;">
							<%@ include file="../../framework/org.jsp"%>
					</div>
			</div>		
                <div class="span4" >
                    <p class="text-right">
                        <button id="uploadButtonFacade" data-loading-text="文件上传中..." class="btn blue"></button>
                        <button id="uploadButton" class="btn blue fileinput-button">
					     <span><i class="icon-arrow-up">&nbsp;上传报表文件</i></span>
							<input id="fileupload" type="file" name="eFiles" >
						</button>
                    <a class="btn blue" href=download><i class="icon-arrow-down">&nbsp;下载报表模版文件</i></a></p>
                </div>
        </div>
        <div class="row-fluid">
            <div class="span12" align="left">
                <table id="reportFileTable"></table>
            </div>
        </div>
    
     </div>




<script>
// var url = path + "/analyze/upload?upload_file_type=3&farm_id="+$("#orgId" + (count0rg - 1)).val().split(",")[1]+"&farm_name="+$("#orgId" + (count0rg - 1)).val().split(",")[2];
    $(function () {
        getColumns();
        initTable("breed", getColumns(), ${files});
        if(!checkRights()){
            document.getElementById("toolsBar").style.display = "none";
        }
    });
    //    alert(isRead);
    //检查权限
    function checkRights(){
        if(isRead==0){
            return false;
        } else {
            return true;
        };
    };



</script>
<script type="text/javascript" src="<%=path%>/js/bootbox.min.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=path %>/framework/js/bootstrap-datepicker.zh-CN.js"></script>

<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>
<script type="text/javascript" src="<%=path%>/modules/analyze/js/uploadReportFile.js"></script>

</body>
</html>
