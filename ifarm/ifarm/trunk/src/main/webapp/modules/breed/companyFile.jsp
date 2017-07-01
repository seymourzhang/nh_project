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

    <script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>
    <link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />
    <script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>

    <link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">
    <script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>
    <script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>
    <script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>

</head>
<script>
    var isRead="${pd.write_read}";//菜单是否只读

    function getColumns() {
        var dataColumns = [{
            checkbox: true,
            title: "选择",
            width: '2%',
        }, {
            field: "file_name",
            title: "文件名",
            width: '30%',
            formatter: function(value, row, index){
                return "<a href=download?direct=" + row.file_path + "&id=" + row.id + "&dirName=upload&fileName=" + (value) + ">" + value + "</a>";
            }
        }, {
            field: "create_time",
            title: "上传时间",
            width: '8%'
        }, {
            field: "bak",
            title: "备注",
            width: '50%',
        }];
        return dataColumns;
    }

    function uploadConfirm() {
        layer.open({
            type: 2,
            title:"文件上传",
            skin: 'layui-layer-lan', //加上边框
            area: ['520px', '370px'], //宽高
            closeBtn: 0,
            shift: 4, //动画类型
            content: '<%=path%>/breed/editFileUrl'
        });
    }
    function deleteRecord() {
        var temps = $("#breedTable").bootstrapTable("getSelections");
        var array = new Array();

        layer.confirm('是否确认删除？', {
            skin: 'layui-layer-lan'
            , closeBtn: 0
            , shift: 4 //动画类型
        }, function ok() {
            for(var aa=0; aa<temps.length; ++aa){
                array.push(temps[aa]["id"]);
            }
            $.ajax({
                url: path + "/breed/deleteRecord",
                data:{"id":array.toString()},
                dataType: "json",
                type: "post",
                async:false,
                success:function (result) {
                    var list = result.obj;
                    for (var i=0; i<list.length; ++i){
                        var fileName = list[i]["file_name"];
                        fileName = fileName.replace(/\\/g, "");
                        list[i]["file_name"] = fileName;
                    }
                    if (result.msg == "1") {
                        layer.msg('删除成功！');
                        $("#breedTable").bootstrapTable("load", list);
                    }
                },
                error:function (result) {
                    console.info("delete failed!");
                }
            });
        });




    }
    function reflush(list) {
        $("#breedTable").bootstrapTable("load", list);
    }
</script>
<body style="background-color: #ffffff;">
    <div id="page-content"  class="clearfix">
        <div id="toolsBar" class="row-fluid" style="background:#e7e5e5;padding-top: 10px;height: 40px;">
            <div style="padding-left: 10px;">
                <div class="span12">
                    <button type="button" class="btn blue" onclick="uploadConfirm();">
                        <i class="icon-arrow-up"></i>上传文件</button>
                    &nbsp;&nbsp;
                    <button type="button" class="btn blue" onclick="deleteRecord();"><i class="icon-trash"></i> 删除</button>
                </div>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12" align="left">
                <table id="breedTable"></table>
            </div>
        </div>
    </div>





<script>
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


</body>
</html>
