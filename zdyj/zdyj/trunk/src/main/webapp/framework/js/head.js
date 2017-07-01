var remindStatusList = [];


$(document).ready(function() {
	lo();
	alarmIncoMsg();
    remindIncoMsg();
});

function remindIncoMsg(){
        /**获取提醒信息**/
        $.ajax({
            url: path+"/user/getRemindIncoMsg",
            data: {
            },
            type : "POST",
            dataType: "json",
            cache: false,
            success: function(result) {
                var list = eval(result.obj);
                var number=$("#head_msg_remind_currCount").html();
                var flag = false;
                if(remindStatusList.length != 0 && list.length!=0 && list.length == remindStatusList.length){
                	var m=0;
                	for(var k in remindStatusList){
                		if(remindStatusList[k].id == list[k].id && remindStatusList[k].status == list[k].approve_status){
                			m += 1;
						}
					}
					if(m!=remindStatusList.length){
                		flag = true;
					}
				}
                if((number!=list.length && list.length!=0) || (flag==true)){
                    $("#head_msg_remind_CurrList").empty();
                    $('#head_msg_remind_currCount').html(list.length);
                    var str="<li><p>有"+list.length+"条提醒信息</p></li>";
                    remindStatusList = [];
                    for(var i=0;i<list.length;i++){
                        str+="<li><a href='javascript:void(0);' onclick='siMenu(\"z302\",\"lm1\",\"se1\",\"op1\",\"物资管理\",\"/googs/googsView?tabId=3\")'> <span class='label label-success'><i class='icon-bolt'></i></span> ";
                        str+=list[i].farm_name+" - " + list[i].good_name +" - "+list[i].aprrove_status_name +"</a></li>";
                        var idValue = list[i].id;
                        var statusValue = list[i].approve_status;
                        remindStatusList.push({id: idValue, status:statusValue});
                    }
                    $("#head_msg_remind_CurrList").append(str);
                    /**闪动效果**/
                    $("#header_remind_bar").pulsate({
                        color: "#66bce6",
                        repeat: 5
                    });

                }
                if(list.length==0){
                    $('#head_msg_remind_currCount').html("");
                    $("#head_msg_remind_CurrList").empty();
                    remindStatusList = [];
                }
            }
        });
        setTimeout("javascript:remindIncoMsg();",5000); //5s刷新一次
}


function  alarmIncoMsg(){
    /**获取报警信息**/
	 $.ajax({
	     url: path+"/user/getAlarmIncoMsg",
	     data: {
	     },
	     type : "POST",
	     dataType: "json",
	     cache: false,
	     success: function(result) {
	         var list = eval(result.obj);
	         var number=$("#head_msg_currCount").html();
	         if(number!=list.length&&list.length!=0){
		         $("#head_msg_CurrList").empty();
		         $('#head_msg_currCount').html(list.length);
		         var str="<li><p>有"+list.length+"条报警信息</p></li>";
		         for(var i=0;i<list.length;i++){
		        	 str+="<li><a href='javascript:void(0);' onclick='siMenu(\"z102\",\"lm1\",\"se1\",\"op1\",\"实时报警\",\"/alarmCurr/showAlarmCurr\")'> <span class='label label-success'><i class='icon-bolt'></i></span> ";
		        	 str+=list[i].farm_name+" - "+list[i].house_name+	" - "+list[i].alarm_name +"</a></li>";
		         }
		         $("#head_msg_CurrList").append(str); 
		        	 /**闪动效果**/
		         $("#header_notification_bar").pulsate({
		                color: "#66bce6",
		                repeat: 5
		            });
		        
	         }
	         if(list.length==0){
	        	 $('#head_msg_currCount').html("");
	        	 $("#head_msg_CurrList").empty();
	         }
	     }
	 });
	 setTimeout("javascript:alarmIncoMsg();",5000); //5s刷新一次
}




function lo(){
	var d = new Date()//为日期命名
	var year = d.getFullYear();
	var month = d.getMonth() + 1;
	var date = d.getDate();

	var weekday = new Array(7);//建立一个星期的数组
	weekday[0] = "星期日";
	weekday[1] = "星期一";
	weekday[2] = "星期二";
	weekday[3] = "星期三";
	weekday[4] = "星期四";
	weekday[5] = "星期五";
	weekday[6] = "星期六";

	var week = weekday[d.getDay()];
	 $("#dateMassage").html(year+"-"+month+"-"+date+"</br> "+week)
	//document.getElementById('dateMassage').innerHTML =year+"-"+month+"-"+date+"</br> "+week;
}

