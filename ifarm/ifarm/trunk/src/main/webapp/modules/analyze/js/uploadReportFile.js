/**
 * Created by yoven on 06/09/2017.
 */
    
    $(function () {
    	OrgSearch(count0rg,num);
    	initTable("reportFile", getColumns(), []);
//    	setTimeout("javascript:newupload();", 1000);
    	getFiles();
        
    });
    
    function newupload(){
//    	$('#uploadButtonFacade').hide();
//    	$('#fileupload').fileupload({
//            url: path + "/analyze/upload?upload_file_type=3&farm_id="+$("#orgId" + (count0rg - 1)).val().split(",")[1]+"&farm_name="+$("#orgId" + (count0rg - 1)).val().split(",")[2],
//            autoUpload: true,
//            add: function (e, data){
//                if(data.originalFiles.length>0){
//                }
//                $('#uploadButtonFacade').show();
//                $('#uploadButtonFacade').button('loading');
//                $('#uploadButton').hide();
//                data.submit();
//            },
//            done: function (e, data) {
//            	$('#uploadButtonFacade').button('reset');
//                $('#uploadButtonFacade').hide();
//                $('#uploadButton').show();
//                var rt = '(' + data.result + ')';
//                var json = eval(rt);
////                reFlushData(currTabName);
//                getFiles();
//                if(json.success == true){
//                	layer.msg("文件上传成功");
//                } else {
//                		layer.msg(json.msg);
//                	
//                }
//                return;
//            },
//            fail:function (e, data) {
//            	layer.msg(e);
//                return;
//            }
//        });
    }
    
    function getColumns() {
        var dataColumns = [{
            field: "file_name",
            title: "文件名",
            width: '50%'
        }, {
            field: "status",
            title: "状态",
            width: '20%'
        }, {
            field: "create_date",
            title: "上传时间",
            width: '30%'
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
        $("#reportFileTable").bootstrapTable("load", list);
    }
    
    function getFiles(){
        if(count0rg!=-1){
            var fid = $("#orgId" + (count0rg - 1)).val().split(",")[1];
            $.ajax({
                // async: true,
                url: path+"/analyze/getFiles",
                data: {farm_id: fid},
                type : "POST",
                dataType: "json",
                // timeout:50000,
                success: function(result) {
                    var list = eval(result.obj);
                    $("#reportFileTable").bootstrapTable("load", list);
                }
            });
        }




    }