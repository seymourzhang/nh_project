<%--
  Created by IntelliJ IDEA.
  User: Raymon
  Date: 2/6/2017
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<a id="btnHelp" href="javascript:void(0);" onclick="help.showHelp()" style="vertical-align: middle;">
    <i class="icon-question-sign">
        <font style="color: #871600; size: 13px;">&nbsp;帮助&nbsp;</font>
    </i>
</a>

<script>
    var help = {
        context: "",
        size: ['400px', '200px'],
        showHelp: function(){
            layer.confirm(this.context, {
                title: '小贴士'
                , skin: 'layui-layer-lan'
                , closeBtn: 0
                , shift: 4 //动画类型
                , btn : ['关闭']
                , area: this.size,
            });
        },
        hideHelp: function(displayFlag){
            document.getElementById("btnHelp").style.display = displayFlag;
        },
        showHelpIcon: function(elemId, msg){
            var id = Math.floor(Math.random()*1000000);
            var context = "<img id='" + id + "'  alt='mouse'' src='../framework/help/image/helpIcon.png' style='width: 34px;height: 34px;margin-bottom: 3px;'/>";
            document.write(context);
            var obj = document.getElementById(id);
            obj.onclick=function()
                {
                    $("#" + elemId).tips({
                        side : 1,
                        msg : msg,
                        time : 5,
                        bg: '#4D4D4D',
                        color: '#FFFFFF'
                    });
                    $("#" + elemId).focus();
                }
        },
    };
</script>
