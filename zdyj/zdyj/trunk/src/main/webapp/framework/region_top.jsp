<%--
  Created by IntelliJ IDEA.
  User: LeLe
  Date: 1/18/2017
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--顶部区域（登录用）--%>
<div class ='font s32 white'
     id="topLogin"
     cellpadding="0"
     cellspacing="0"
     style="background-color: #417CC5;width:100% ;height:85px;position: absolute;top:0px;display: none">
    <p style="height:85px; line-height:85px; width:100%; overflow:hidden; ">
        &nbsp;&nbsp;&nbsp;&nbsp;<%=systemName %>&nbsp;&nbsp;<%= systemVersion%>
    </p>
</div>
<%--顶部区域（系统用）--%>
<div class ='font s32 white container-fluid'
     id="topSystem"
     cellpadding="0"
     cellspacing="0"
     style="width:100% ;height:60px;position: fixed;top:0px;display: none">
    <%--<div class="container-fluid">--%>
        <div class="row">
            <%--系统logo及名称--%>
            <div class="col-lg-7">
                <a class="brand" href="#">
                    <img src="<%=path%>/framework/image/logo.png"
                         alt="logo"
                         style="padding-left:20px;width:70px;margin-top:5px;">
                </a>
                <span class="font s24 white bold" style="vertical-align:middle;"><%=systemName%></span>
            </div>

            <%--用户名称及退出按钮--%>
            <div class="col-lg-5">
                <%--天气预报--%>
                <iframe  name="weather_inc"
                         src="http://i.tianqi.com/index.php?c=code&id=12&color=%23FFFFFF&icon=4&num=1"
                         width="190" height="48" frameborder="0" marginwidth="0" marginheight="0" scrolling="no">
                </iframe>
                <%--日期信息--%>
                <span class="font white s15" id="dateName" style="text-align: center;"></span>
                <%--提醒信息--%>
                <a href="#" class="dropdown-toggle" id="dropdownRemind" data-toggle="dropdown">
                    <img alt="" src="<%=path%>/framework/image/remind.png" style="width: 20px;height: 20px;vertical-align:middle;">
                    <span class="badge" id="remindCurrCount"></span>
                </a>
                <%--报警信息--%>
                <a href="#" class="dropdown-toggle" id="dropdownWarning" data-toggle="dropdown">
                    <img alt="" src="<%=path%>/framework/image/warn.png" style="width: 20px;height: 20px;vertical-align:middle;">
                    <span class="badge" id="warningCurrCount"></span>
                </a>
                <%--用户信息--%>
                <div class="dropdown">
                    <a href="#" class="dropdown-toggle" id="dropdownMenuUser" data-toggle="dropdown">
                        <img alt="" src="<%=path%>/framework/image/user.png" style="width: 20px;height: 20px;vertical-align:middle;">
                        <span class="font white s15">${sessionUser.user_real_name }</span>
                        <i class="icon-angle-down" style="vertical-align:middle;"></i>
                    </a>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenuUser" style="align: left">
                        <li role="presentation">
                            <a role="menuitem" tabindex="-1" href="<%=path%>/login/outLogin">
                                <i class="icon-signout"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
    <%--</div>--%>
</div>

<script>
    var region_top = {
        init: function(style){
            this.setStyle(style);
            this.dateName(system.getDate());
        },
        setStyle: function (style){
            if(style == "login"){
                document.getElementById("topLogin").style.display = "block";
                document.getElementById("topSystem").style.display = "none";
            } else{
                document.getElementById("topLogin").style.display = "none";
                document.getElementById("topSystem").style.display = "block";
            }
        },
        dateName: function(d){
            $("#dateName").html(d.year+"-"+d.month+"-"+d.date + " "+d.week)
        }
    };
</script>