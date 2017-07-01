<%--
  Created by IntelliJ IDEA.
  User: Seymour
  Date: 2016/11/4
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

    <%--<link rel="stylesheet" href="<%=path %>/framework/css/bootstrap.min.css" />--%>
    <link rel="stylesheet" href="<%=path%>/modules/breed/css/bootstrap.min.css">
    <!-- Generic page styles -->
    <link rel="stylesheet" href="<%=path%>/modules/breed/css/style.css">
    <!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
    <link rel="stylesheet" href="<%=path%>/modules/breed/css/jquery.fileupload.css">

    <%--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>--%>
    <%--<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->--%>
    <script src="<%=path%>/framework/jquery/jquery.ui.widget.js"></script>
    <%--<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->--%>
    <script src="<%=path%>/framework/jquery/jquery.iframe-transport.js"></script>
    <!-- The basic File Upload plugin -->
    <script src="<%=path%>/framework/jquery/jquery.fileupload.js"></script>
    <!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
    <%--<script src="<%=path%>/framework/js/bootstrap.min.js"></script>--%>

    <%--<script src="<%=path%>/framework/js/bootstrap_table/bootstrap-table.js"></script>--%>
    <%--<link href="<%=path%>/framework/js/bootstrap_table/bootstrap-table.css" rel="stylesheet" />--%>
    <%--<script src="<%=path%>/framework/js/bootstrap_table/locale/bootstrap-table-zh-CN.js"></script>--%>

    <%--<link rel="stylesheet" href="<%=path%>/framework/js/bootstrap_editable/1.5.1/css/bootstrap-editable.css">--%>
    <%--<script src="<%=path%>/framework/js/bootstrap_editable/1.5.1/js/bootstrap-editable.js"></script>--%>
    <%--<script src="<%=path%>/framework/js/bootstrap_table/extensions/editable/bootstrap-table-editable.js"></script>--%>
    <%--<script type="text/javascript" src="<%=path%>/framework/table/table.js"></script>--%>

</head>

<body>

    <div class="row-fluid">
        <div class="span7">
            <div style="padding-top: 5px;">
                <p>
                    <span_customer2>文件列表</span_customer2>
                </p>
                <div id="files" class="files" style="border: 1px solid #808080;height: 200px"></div>
            </div>
        </div>
        <div class="span5">
            <%--浏览文件--%>
            <div class="row-fluid">
                <div class="span12" align="right">
                        <span class="btn btn-success fileinput-button">
                            <span>浏览</span>
                            <input id="fileupload" type="file" name="eFiles" multiple >
                        </span>
                    <%--备注--%>
                    <textarea id="tips" name="user_code" style="width: 100%;height: 200px" placeholder="备注"></textarea>
                </div>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <%--进度条--%>
            <div id="progress" class="progress" style="height: 10px;">
                <div class="progress-bar progress-bar-success" ></div>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12" align="center">
            <button id="uploadOk" type="button" class="btn blue" onclick="uploadSubm()">
                <i class="icon-ok"></i>&nbsp;确定
            </button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn blue" onclick="uploadReset()">
                <i class="icon-undo"></i>&nbsp;重置
            </button>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="btn" onclick="uploadCanc()">
                取消
            </button>
        </div>
    </div>

    <%--</div>--%>
    <!-- The container for the uploaded files -->

    <%--<div class="control-group">--%>
        <%--<label class="control-label" style="width: 100px; left: 5px; position: relative;top: 45px;">备注:</label>--%>
        <%--<div class="controls" style="margin-left: 45px;position: relative;top: 20px;">--%>
            <%--<input id="tips" type="text" style="width: 460px; margin-bottom: 0px" name="user_code" value="">--%>
        <%--</div>--%>
    <%--</div>--%>
    <script>
        var fileList = [];
        var stateProgressId = 0;
        $(function () {
            'use strict';
            var url = "<%=path%>/breed/newUpload";
            $('#fileupload').fileupload({
                url: url,
                autoUpload: true,
                add: function (e, data){
                    if(data.originalFiles.length>0){
//                        if(progress==0){
//                            progress += parseInt(100/(data.originalFiles.length), 10);
//                        }
//                        if(progress==data.originalFiles.length){
//                            progress += parseInt(100/(data.originalFiles.length), 10);
//                        }
                    }
                    data.submit();
                    if(stateProgressId==0){
                        $('#progress .progress-bar').css(
                            'width',
                            0 + '%'
                        );
                    }
                    stateProgressId += 1;
                    var  progress = parseInt(100*(stateProgressId/(data.originalFiles.length)), 10);
                    $('#progress .progress-bar').css(
                            'width',
                            progress + '%'
                        );
                    console.log("add:");
                    console.log(stateProgressId);

                },
                done: function (e, data) {
                    var json = eval('(' + data.result + ')');
                    if (json.msg == "1") {
                        $.each(data.files, function (index, file) {
                            $('<p/>').text(file.name).appendTo('#files');
                            fileList.push(file.name);
                        });
                    } else {
                        uploadReset();
                        layer.msg(json.msg);
//                        return;
                    }
                    stateProgressId = 0;
                    return;
                },
                fail:function (e, data) {
//                    console.log("upload fail: ");
//                    console.log(e);
                    uploadReset();
                    layer.msg("上传的单个文件大小不能超过10MB，请重新选择文件！");
                    stateProgressId = 0;
                    return;
                }
            });
        });

        function uploadReset(){
            $("#files > p").remove();
            $('#progress .progress-bar').css(
                'width',
                0 + '%'
            );
            fileList=[];
        }

        function uploadSubm() {
            var tips = document.getElementById("tips").value;
//            var fileName = $("#files > p")[0].textContent;
            if ($("#files > p").isEmpty) {
                layer.alert('请点击浏览按钮，选择文件！', {
                    skin: 'layui-layer-lan'
                    , closeBtn: 0
                    , shift: 4 //动画类型
                })
                return;
            } else {
                document.getElementById("uploadOk").disabled = true;
                layer.msg('正在上传文件，请稍后...');
                for(var key in fileList){
                    var fileName = fileList[key];
                    $.ajax({
                        url: path + "/breed/saveTips",
                        data: {"bak": tips, "file_name": fileName, "ISENABLED": "1"},
                        dataType: "json",
                        success: function (result) {
                            var list = result.obj;
                            for (var i = 0; i < list.length; ++i) {
                                var fileName = list[i]["file_name"];
                                fileName = fileName.replace(/\\/g, "");
                                list[i]["file_name"] = fileName;
                            }
                            layer.msg('上传成功', function () {
//                    parent.parent.document.getElementById("stockTable").bootstrapTable("load", list);
                                parent.reflush(list);
                                parent.layer.closeAll();
                            });
                        },
                        error: function (result) {
                            layer.msg('上传失败！');
                            console.info("上传失败！");
                        }
                    })
                }

                document.getElementById("uploadOk").disabled = false;
            }
        }
        function uploadCanc() {
            parent.layer.closeAll();
        }
    </script>
</body>
</html>
