/**
 * Created by LeLe on 11/18/2016.
 */
var orgListTreeName = "orgListTree";
var orgListTreeSetting = {};
var orgListTableName = "orgList";

$(document).ready(function(){
    //初始化机构树
    initOrgListTree();

    //刷新机构树数据
    reflushOrgList();

    //初始化表格
    initOrgListTable();

    document.getElementById("labelOrgName").innerHTML="<font size='4' ><B>" + "组织架构" +"</B></font>";
});

//初始化机构树
function initOrgListTree() {
    orgListTreeSetting = {
        view: {
            selectedMulti: false,
            fontCss: { 'color': 'blue', 'font-family': '微软雅黑', 'font-size': '18px' }
        },
        check: {
            enable: false,
            chkDisabledInherit: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: zTreeOnClick
        }
    };
    $.fn.zTree.init($("#" + orgListTreeName), orgListTreeSetting, []);
    // initZtreeStyle();
};
//
// function initZtreeStyle(){
//     var zTree = $.fn.zTree.getZTreeObj(orgListTreeName);
//     var nodes = zTree.getSelectedNodes();
//     for(var i =0; i<nodes.length; i++){
//         var style = "font-size: 18px";
//         zTree.setting.view.fontCss["font-style"] = style;
//         zTree.updateNode(nodes[i]);
//     }
//
// }

//点击事件
function zTreeOnClick(event, treeId, treeNode) {
    // alert(treeNode.id + ", " + treeNode.name);
    document.getElementById("labelOrgName").innerHTML="<font size='4' ><B>" + treeNode.name +"</B></font>";
    if(treeNode.id != 0){
        reflushOrgData(treeNode.id, treeNode.org_level);
    }
};


//获取机构数据
function reflushOrgList(){
    var rt =[];
    // rt = [ {id:"0",pId:"0",name:"组织架构",open:"true",chkDisabled:"false",p_name:""}
    //         ,{id:"1",pId:"0",name:"新疆分公司",open:1,chkDisabled:0,p_name:"", org_code:"",org_level:"",org_level_name:"",create_person:"",create_date:"",create_time:""}
    //         ,{id:"2",pId:"0",name:"内蒙古分公司",open:1,chkDisabled:0,p_name:"", org_code:"",org_level:"",org_level_name:"",create_person:"",create_date:"",create_time:""}
    //         ,{id:"3",pId:"0",name:"河北分公司",open:1,chkDisabled:0,p_name:"", org_code:"",org_level:"",org_level_name:"",create_person:"",create_date:"",create_time:""}
    //     ];
    $("#" + orgListTreeName).empty();
    $.ajax({
        type: "post",
        // async:false,
        url: path + "/org/getOrganizationList",
        data: {},
        dataType: "json",
        success: function (result) {
            rt = eval(result.obj);
            rt.unshift({id:"0",pId:"0",name:"组织架构",open:"true",chkDisabled:"false",p_name:"", org_level:0, org_level_name:""});
            $.fn.zTree.init($("#" + orgListTreeName), orgListTreeSetting, rt);
        }
    });

    return rt;
}

//初始化机构表格
function initOrgListTable(){
    initTable(orgListTableName, getOrgListTableColumns(), []);
};

//获取机构表格表头定义对象
function getOrgListTableColumns(){
    var dataColumns = [
        // {checkbox:true,
    //                     title: "选择",
    //                     width: '5%'
    //                 }, {
                        {
                        field: "parent_id",
                        title: "农场ID",
                        visible: false
                    }, {
                        field: "parent_name",
                        title: "农场名称"
                    }, {
                        field: "name_cn",
                        title: "栋舍名称"
                    }, {
                        field: "level_id",
                        title: "农场层级",
                        visible: false
                    }];
    return dataColumns;
};

//获取农场、栋舍数据
function reflushOrgData(orgId, orgLevelId){
    var rt =[];
    // rt = [{id:"1",house_code:"001",house_name:"H1,H2", farm_id:"1",farm_name:"新疆育成鸡场",house_level_id:"3"}
    //     ,{id:"3",house_code:"001",house_name:"H3,H4", farm_id:"1",farm_name:"新疆产蛋鸡场",house_level_id:"3"}
    // ];
    $.ajax({
        type: "post",
        // async:false,
        url: path + "/org/getFarmWithHouseName",
        data: {
            id: orgId,
            level_id: orgLevelId
        },
        dataType: "json",
        success: function (result) {
            rt = eval(result.obj);
            loadTableData(orgListTableName, rt);
        }
    });
    return rt;
}

//检查权限
function checkRights(){
    if(isRead==0){
        layer.alert('无权限，请联系管理员!', {
            skin: 'layui-layer-lan'
            ,closeBtn: 0
            ,shift: 4 //动画类型
        });
        return false;
    } else {
        return true;
    };
};

//新建机构
function addOrg(){
    if(!checkRights()){
        return;
    }
    var treeObj=$.fn.zTree.getZTreeObj(orgListTreeName);
    var nodes = treeObj.getSelectedNodes();
    var parentOrgId = "0";
    var parentOrgName = "组织架构";
    var parentOrgLevelId = 0;
    var parentOrgLevelName = "";
    var orgName = "";
    var orgLevelId = null;
    var orgLevelName = "" ;

    //设置父级机构信息
    // if(nodes.length == 1){
    //     parentOrgId = nodes[0].id;
    //     parentOrgName = nodes[0].name;
    //     parentOrgLevelId = nodes[0].org_level;
    //     parentOrgLevelName = nodes[0].org_level_name;
    // }
    // else{
        // layer.msg('请选择新建机构的上级机构!', {});
        // return;
    // }


    //设置新建机构层级
    nodes = treeObj.getNodes();
    var cNodes = treeObj.transformToArray(nodes);
    if (cNodes) {
        for(var key in cNodes){
            if(parentOrgId == cNodes[key].pId){
                orgLevelId = cNodes[key].org_level;
                orgLevelName = cNodes[key].org_level_name
            }
        }
    }


    //设置新建机构窗口内容
    var str ='<br><div class="container-fluid" >';
            str += '<div class="row-fluid">';
                str += '<div class="span3" align="center">';
                str += '<span_customer2 >上级机构</span_customer2 >';
                str += '</div>';
                str += '<div class="span3" align="left">';
                str += '<input type="text" id="parentOrgName" name="parentOrgName" value="' + parentOrgName + '" disabled="true">';
                str += '<input type="hidden" id="parentOrgId" name="parentOrgId" value="' + parentOrgId + '" disabled="true">';
                str += '</div>';
            str += '</div>';
            str += '<div class="row-fluid">';
                str += '<div class="span3" align="center">';
                str += '<span_customer2 >新增机构</span_customer2 >';
                str += '</div>';
                str += '<div class="span3" align="left">';
                str += '<input type="text" id="orgName" name="orgName" value="" placeholder="请输入新建机构名称">';
                str += '<input type="hidden" id="orgId" name="orgId" value="" disabled="true">';
                str += '</div>';
            str += '</div>';
            str += '<div class="row-fluid">';
                str += '<div class="span3" align="center">';
                    str += '<span_customer2 >层级名称</span_customer2 >';
                str += '</div>';
                str += '<div class="span3" align="left">';
                    if(orgLevelId == null){
                        str += '<input type="hidden" id="orgLevelId" name="orgLevelId" value="' + (parentOrgLevelId+1) + '" disabled="true">';
                        str += '<input type="text" id="orgLevelName" name="orgLevelName" value="">';
                    } else{
                        str += '<input type="hidden" id="orgLevelId" name="orgLevelId" value="' + orgLevelId + '" disabled="true">';
                        str += '<input type="text" id="orgLevelName" name="orgLevelName" value="' + orgLevelName + '" disabled="true">';
                    }
                str += '</div>';
            str += '</div>';
        str += '</div>';
    layer.open({
        type: 1,
        skin: 'layui-layer-lan', //加上边框
        area: ['340px', '250px'], //宽高
        content: str,
        btn: ['确定','取消'],
        yes: function(index){
            parentOrgId= document.getElementById("parentOrgId").value;
            parentOrgName = document.getElementById("parentOrgName").value;
            orgName= document.getElementById("orgName").value;
            orgLevelId= document.getElementById("orgLevelId").value;
            orgLevelName= document.getElementById("orgLevelName").value;

            if(orgName == ""){
                layer.msg('机构名称不能为空，请重新输入!', { });
                return;
            }
            if(orgLevelName == ""){
                layer.msg('机构层级不能为空，请重新输入!', {});
                return;
            }

            $.ajax({
                type: "post",
                // async:false,
                url: path + "/org/addOrg",
                data: {
                    org_name: orgName,
                    org_level_name:orgLevelName,
                    org_level_id:orgLevelId,
                    parent_org_id: parentOrgId,
                    parent_org_name: parentOrgName,
                    parent_org_level_id: parentOrgLevelId
                },
                dataType: "json",
                success: function (result) {
                    layer.close(index);
                    if(result.success){
                        layer.msg('成功新增机构！', {});
                        reflushOrgList();
                        loadTableData(orgListTableName, []);
                    } else{
                        layer.msg('新增机构失败！('+result.msg+')', {});
                        return;
                    }
                }
            });
        }
    });
}

//修改机构
function editOrg(){
    if(!checkRights()){
        return;
    }
    var treeObj=$.fn.zTree.getZTreeObj(orgListTreeName);
    var nodes = treeObj.getSelectedNodes();
    var orgId = "";
    var orgName = "";
    var orgNameUpdated = "";

    //设置机构信息
    if(nodes.length == 1 && nodes[0].org_level != 0){
        orgId = nodes[0].id;
        orgName = nodes[0].name;
    } else{
        layer.msg('请选择需要修改的机构!', {});
        return;
    }

    var str ='<br><div class="container-fluid" >';
            str += '<div class="row-fluid">';
                str += '<div class="span4" align="center" style="margin-left: -10px;">';
                    str += '<span_customer2 >修改前机构名</span_customer2 >';
                str += '</div>';
                str += '<div class="span8" align="left">';
                    str += '<input type="text" id="orgName" name="orgName" value="' + orgName + '" disabled="true">';
                    str += '<input type="hidden" id="orgId" name="orgId" value="' + orgId + '" disabled="true">';
                str += '</div>';
            str += '</div>';
            str += '<div class="row-fluid">';
                str += '<div class="span4" align="center" style="margin-left: -10px;">';
                    str += '<span_customer2 >修改后机构名</span_customer2 >';
                str += '</div>';
                str += '<div class="span8" align="left">';
                    str += '<input type="text" id="orgNameUpdated" name="orgNameUpdated" value="' + orgNameUpdated + '" placeholder="请输入修改机构名称">';
                str += '</div>';
            str += '</div>';
        str += '</div>';
    layer.open({
        type: 1,
        skin: 'layui-layer-lan', //加上边框
        area: ['360px', '210px'], //宽高
        content: str,
        btn: ['确定','取消'],
        yes: function(index){
            orgNameUpdated= document.getElementById("orgNameUpdated").value;

            if(orgNameUpdated == ""){
                layer.msg('机构名称不能为空，请重新输入!', {});
                return;
            }

            $.ajax({
                type: "post",
                // async:false,
                url: path + "/org/updateOrg",
                data: {
                    org_id: orgId,
                    org_name_updated:orgNameUpdated
                },
                dataType: "json",
                success: function (result) {
                    layer.close(index);
                    if(result.success){
                        layer.msg('成功修改机构！', {});
                        reflushOrgList();
                        loadTableData(orgListTableName, []);
                    } else{
                        layer.msg('修改机构失败！('+result.msg+')', {});
                        return;
                    }
                }
            });

        }
    });
}

//删除机构
function deleteOrg(){
    if(!checkRights()){
        return;
    }
    var treeObj=$.fn.zTree.getZTreeObj(orgListTreeName);
    var nodes = treeObj.getCheckedNodes(true);
    var orgIds = "";

    if(nodes.length == 0 ){
        nodes = treeObj.getSelectedNodes();
    }

    //设置机构信息
    if(nodes.length > 0){
        for(var key in nodes){
            if(nodes[key].org_level != 0){
                orgIds += nodes[key].id + ",";
            }
        }
    } else{
        layer.msg('请选择需要删除的机构!', {});
        return;
    }

    layer.confirm('是否确认删除选中的机构？', {
        skin: 'layui-layer-lan'
        , closeBtn: 0
        , shift: 4 //动画类型
    }, function ok() {
        $.ajax({
            type: "post",
            // async:false,
            url: path + "/org/deleteOrg",
            data: {
                org: orgIds
            },
            // traditional: true,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    layer.msg('删除机构成功！', {});
                    reflushOrgList();
                    loadTableData(orgListTableName, []);
                } else{
                    layer.msg('删除机构失败！('+result.msg+')', {});
                    return;
                }
            }
        });
    });

}

//绑定农场
function mappingFarmHouse(){
    if(!checkRights()){
        return;
    }
    var treeObj=$.fn.zTree.getZTreeObj(orgListTreeName);
    var nodes = treeObj.getCheckedNodes(true);
    var orgId = "";
    var orgLevelId = "";

    if(nodes.length == 0){
        nodes = treeObj.getSelectedNodes();
    }

    //设置机构信息
    if(nodes.length == 1){
        orgId = nodes[0].id;
        orgLevelId = nodes[0].org_level;
    } else{
        if(nodes.length > 1){
            layer.msg('请选择一个机构!', { });
        } else{
            layer.msg('请选择需要绑定农场的机构!', {});
        }
        return;
    }

    $.ajax({
        type: "post",
        // async:false,
        url: path + "/org/getFarmForMapping",
        data: {},
        dataType: "json",
        success: function (result) {
            if(result.success) {
                var farmList = eval(result.obj);
                var str = '<br><div class="container-fluid" >';
                str += '<div class="row-fluid">';
                str += '<div class="span12" align="left">';
                str += '请选择需要绑定的农场（可以按住Shfit或Ctrl键进行多选）';
                str += '</div>';
                str += '</div>';
                str += '<div class="row-fluid">';
                str += '<div class="span12" align="left">';
                str += '<select id="farmSelect" multiple="multiple" style="width: 100%">';
                for (var key in farmList) {
                    str += '<option value ="' + farmList[key].id + '">' + farmList[key].name_cn + '</option>';
                }
                str += '</select>';
                str += '</div>';
                str += '</div>';
                str += '</div>';

                layer.open({
                    type: 1,
                    skin: 'layui-layer-lan', //加上边框
                    area: ['400px', '240px'], //宽高
                    content: str,
                    btn: ['确定', '取消'],
                    yes: function (index) {
                        var selectItemsValuesStr = "";
                        var objSelect = document.getElementById("farmSelect");
                        if (null != objSelect && typeof(objSelect) != "undefined") {
                            var length = objSelect.options.length
                            for (var i = 0; i < length; i = i + 1) {
                                if (objSelect.options[i].selected) {
                                    if (0 == i) {
                                        selectItemsValuesStr = objSelect.options[i].value + ",";
                                    } else {
                                        selectItemsValuesStr += objSelect.options[i].value + ",";
                                    }
                                }
                            }
                        }

                        // layer.msg(selectItemsValuesStr, {
                        //     skin: 'layui-layer-lan',
                        //     closeBtn: 0,
                        //     shift: 4
                        // });

                                            if(selectItemsValuesStr != ""){
                                                $.ajax({
                                                    type: "post",
                                                    // async:false,
                                                    url: path + "/org/setFarmMapping",
                                                    data: {
                                                        parent_id: orgId,
                                                        level_id: orgLevelId,
                                                        org: selectItemsValuesStr
                                                    },
                                                    dataType: "json",
                                                    success: function (result) {
                                                        layer.close(index);
                                                        if(result.success){
                                                            layer.msg('成功绑定农场！', {});
                                                            reflushOrgList();
                                                            reflushOrgData(orgId, orgLevelId);
                                                        } else{
                                                            layer.msg('绑定农场失败！(' + result.msg + ')', {});
                                                        }
                                                    }
                                                });
                                            } else{
                                                layer.msg('请选择需要绑定的农场!', {});
                                            }
                                        }
                                    });
                                } else{
                                    layer.msg('当前没有需要绑定的农场!', {});
                                    return;
                                }
                    }
                });


}