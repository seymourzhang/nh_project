/**
 * Created by laven on 2017/6/21.
 */
$(function(){
    //页面初始化后,获取当前页面存在的tab个数,初始化样式,使其两边对其
    var size = $("ul li").length;
    var isActive = 0;
    $("ul li").each(function(index, element){
        if(index == isActive){
            $(element).addClass("active");
        }
        $(element).attr("style","width:" + (99/size) + "%");
    });

    //初始化页面内容,对应tab页面按钮控制显示
    $(".tab-content .tab-pane").each(function(index, element){
        if(index == isActive){
            $(element).addClass("active");
            $(element).empty();
            var iframe = createIframe();
            $(element).append(iframe);
        }
    });

    //切换tab页面时做相应参数的处理。
    $("ul li").on('show', function (e) {
        var $tabPane = $("#tab_"+ $(e.target).attr("code"));
        $tabPane.empty();
        var iframe = createIframe();
        $tabPane.append(iframe);
    })
})

//创建一个iframe
function createIframe(){
    var url = "http://dev.agnet.com.cn:80/fr/ReportServer?reportlet=ifarm/temProfile.cpt&tarTFlag=0&tarHFlag=1&org_id=10041&user_id=1&row_count=25";
    //处理参数

    var iframe = document.createElement("iframe");
    $(iframe).attr("width","99.8%");
    $(iframe).attr("height",window.parent.mainFrameHeight);
    $(iframe).attr("frameborder","no");
    $(iframe).attr("src",url);
    return iframe;
}

