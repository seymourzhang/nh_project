/**
 * Created by LeLe on 2/10/2017.
 */
//菜单状态切换
var fmid = "fhindex";
var oid="fhindex";
var sid="fhindex";
var mid = "fhindex";

$(document).ready(function() {
    if(menuId!="" && menuPid!=""){
        $("#fhindex").removeClass();
        $("#lm"+menuPid).attr("class","active");
        $("#se"+menuPid).attr("class","selected");
        $("#z"+menuId).attr("class","active");
        $("#op"+menuPid).attr("class","arrow open");
    }
});


function forwardMenu(id,fid,seid,opid,MENU_NAME,MENU_URL,write_read){
    var mId = id.replace('z','');

    $.ajax({
        // async: true,
        url: path + "/user/getRights",
        data: {
            obj_type: 1,
            menu_id: mId
        },
        type: "POST",
        dataType: "json",
        cache: false,
        async: false,
        // timeout:50000,
        success: function (result) {
            var rt = result.obj;
            if(rt.pass == 1){
                siMenu(id,fid,seid,opid,MENU_NAME,MENU_URL,write_read);
            } else{
                layer.alert('无权限，请联系管理员!', {
                    skin : 'layui-layer-lan',
                    closeBtn : 0,
                    shift : 4
                    // 动画类型
                });
            }
        }
    });
}


function siMenu(id,fid,seid,opid,MENU_NAME,MENU_URL,write_read){

    jQuery('.page-sidebar ul').children('li.active').children('ul.sub-menu').css("display","none");
    jQuery('.page-sidebar ul').each(function () {
        var menuContainer = jQuery('.page-sidebar ul');
        menuContainer.children('li.active').removeClass('active');
        menuContainer.children('arrow.open').removeClass('open');
    });

    if(id != mid){
        $("#"+mid).removeClass();
        mid = id;
    }
    if(fid != fmid){
        $("#"+fmid).removeClass();
        fmid = fid;
    }
    if(seid!=sid){
        $("#"+sid).removeClass();
        $("#"+sid).attr("class","arrow");
        sid = seid;
    }
    if(opid!=oid){
        $("#"+oid).removeClass();
        oid = opid;
    }
    $("#"+fid).children('ul.sub-menu').css("display","");
    $("#"+fid).attr("class","active");
    $("#"+sid).attr("class","selected");
    $("#"+id).attr("class","active");
    //$("#"+fid).append("<span id="+oid+" class='arrow open'></span>");
    $("#"+oid).attr("class","arrow open");
    //MENU_URL=path+MENU_URL;

    var murl=MENU_URL;
    var paramWriteRead ="write_read=" + write_read + "&menu_id=" + id;
    if(murl.indexOf("?") != -1){
        paramWriteRead = "&" + paramWriteRead;
    } else{
        paramWriteRead = "?" + paramWriteRead;
    }
    murl += paramWriteRead;


    top.mainFrame.tabAddHandler(id,MENU_NAME,murl);

    //activateUrl(id,fid,MENU_NAME,MENU_URL);
}

function activateUrl(mid,pid,mtitle,murl,write_read){
    mid = mid.substring(1);   // 取子字符串。
    pid = pid.substring(2);
    alert(path+murl+"?id="+mid+"&pid="+pid+"&write_read"+write_read)
    window.location.href=path+murl+"?id="+mid+"&pid="+pid+"&write_read"+write_read;
}
