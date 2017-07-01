var count0rg;
var num=2;
var allSearch = "true";
var orgLevel = 2;
var asyncFlag = false;

$(document).ready(function() {
    setTimeout("javascript:reflushAlarmCurr();",60000); //60s刷新一次
    initHelp();
});

function initHelp(){
    help.size = ['600px', '365px'];
    help.context = document.getElementById("helpContext").innerHTML;
}

function OrgSearch(count0rg,num){
	if("" != corporation_id){
		var select = document.getElementById("orgId"+(count0rg-2));
		if(null != select && "undefined" != select){
                for(var i=0; i<select.options.length; i++){
                    var corporation = select.options[i].value;
                    var strs= new Array();
                    strs = corporation.split(",");
                    if(strs[0] == corporation_id){
                        corporation_id="";
                        select.options[i].selected = true;
                        select.onchange();
                        break;
                    }
			}
		}
        corporation_id="";
	} else{
        if("" != farm_id){
            var select2 = document.getElementById("orgId"+(count0rg-1));
            if(null != select2 && "undefined" != select2) {
                for(var i=0; i<select2.options.length; i++) {
                    var farm = select2.options[i].value;
                    var strs2 = new Array();
                    strs2 = farm.split(",");
                    if (strs2[0] == farm_id) {
                        farm_id="";
                        select2.options[i].selected = true;
                        select2.onchange();
                        break;
                    }
                }
            }
            farm_id="";
        } else{
            if("" == corporation_id && "" == farm_id){
                reflushAlarmCurr();
            }
        }

    }

}

function formatUnit(unit){
    var rt = unit;
    if(unit.indexOf("/m3") >= 0){
        rt = unit.replace("/m3","/m<sup>" + 3 + "</sup>");
    }
    return rt;
}

function reflushAlarmCurr(){

	var param;
//	if($("#farmId").val()=="" && $("#houseId").val()==""){
//		param=null;
//	}else if($("#farmId").val()=="" && $("#houseId").val()!=""){
//		param = {"houseId":$("#houseId").val()};
//	}else if($("#farmId").val()!="" && $("#houseId").val()==""){
//		param = {"farmId":$("#farmId").val()};
//	}else{
    param = {
        "farmId":$("#orgId"+(count0rg-1)).val().split(",")[1],
        // "houseId":$("#orgId"+count0rg).val().split(",")[1]
    };

//	}

 $.ajax({
     // async: true,
     url: path+"/alarmCurr/reflushAlarmCurr",
     data: param,
     type : "POST",
     dataType: "json",
     cache: false,
     async: false,
     // timeout:50000,
     success: function(result) {
         var list = eval(result.obj);
         $("#tbodyAlarmCurrList tr").remove();
         for(var i=0;i<list.length;i++){
        	 var str='';
        	 str+="<tr align='center' style='height:30px' >";
              str+="<td style='height:30px;text-align: center;'>"+ list[i]["farm_name"]+"</td>";
              str+="<td style='height:30px;text-align: center;'>"+ list[i]["house_name"]+"</td>";
//              if(list[i]["alarm_code"]=='正常'){
//            	  str+="<td style='height:30px;text-align: center;'>"+ list[i]["alarm_code"]+"</td>";
//         	 }else{
//         		  str+="<td bgcolor='red' style='height:30px;text-align: center;'>"+ list[i]["alarm_code"]+"</td>";
//         	 }
        
              str+="<td style='height:30px;text-align: center;'>"+ list[i]["alarm_name"]+"</td>";
              str+="<td style='height:30px;text-align: center;'>"+ (list[i]["set_value"]+"").replace(".00","")+"</td>";
              str+="<td style='height:30px;text-align: center;'>"+ (list[i]["actual_value"]+"").replace(".00","")+"</td>";
//              if(list[i]["deal_status_name"]=='处理中'){
//            	  str+="<td style='color:#FF6600;height:30px;text-align: center;'>" + list[i]["deal_status_name"]+"</td>";
//              }else if(list[i]["deal_status_name"]=='已处理'){
//            	  str+="<td style='color:#00EC00;height:30px;text-align: center;'>" + list[i]["deal_status_name"]+"</td>";
//              }else if(list[i]["deal_status_name"]=='未处理'){
//            	  str+="<td style='color:#EA0000;height:30px;text-align: center;'>" + list[i]["deal_status_name"]+"</td>";
//              }
//            str+="<td style='height:30px;text-align: center;'>"+ list[i]["deal_status_name"]+"</td>";
              str+="<td style='height:30px;text-align: center;'>"+ list[i]["alarm_time"]+"</td>";
              if(list[i]["deal_time"] != null){
            	  str+="<td style='height:30px;text-align: center;'>"+list[i]["deal_time"] +"</td>";
              }else{
            	  str+="<td style='height:30px;text-align: center;color: #E83828;'>"+'未处理' +"</td>";
              }

              str+="<td style='height:30px;text-align: center;'>"+list[i]["continue_time"] +" 分钟</td>";
              str+="<td style='height:30px;text-align: center;'>"+(list[i]["user_name"] != null ? list[i]["user_name"]:'' )+"</td>";
//            str+="<td style='height:30px;text-align: center;'>"+ list[i]["deal_time"]+"</td>";
//            str+="<td style='height:30px;text-align: center;'>"+ list[i]["user_name"]+"</td>";
              $("#tbodyAlarmCurrList").append(str);    
         }
     // alert("reflush");

     }
 });
}

function reflushAlarmCurr2(){
	var param;
	if($("#farmId").val()==""){
		param=null;
	}else{
		param = {"farmId":$("#farmId").val()};
	}

	 $.ajax({
		 // async: true,
		 url: path+"/alarmCurr/reflushAlarmCurr2",
		 data: param,
		 type : "POST",
		 dataType: "json",
		 cache: false,
		 // timeout:50000,
		 success: function(result) {
			 var list = result.obj;
				$("#houseId option:gt(0)").remove();
				for (var i = 0; i < list.length; i++) {
					$("#houseId").append("<option value=" + list[i].id + ">" + list[i].house_name+ "</option>");
				}
				reflushAlarmCurr();
		 }
	 });

}

