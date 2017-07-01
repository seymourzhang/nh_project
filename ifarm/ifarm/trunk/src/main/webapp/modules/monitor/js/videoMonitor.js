/**
 * Created by Ants on 2016/8/25.
 */
var count0rg;
var num;

$(document).ready(function() {
//    $("#farmId").change(function () {
//        setHouseId();
//    });
//    $("#houseId").change(function () {
//        WebVideoCtrl.I_Stop();
//        setTimeout(houseLiveView(),3000);
//    });
//    setHouseId();
    houseLiveView();
});

function OrgSearch(count0rg,num){
	WebVideoCtrl.I_Stop();
	setTimeout(houseLiveView(),3000);
}

//function setHouseId() {
//    var param;
//    if ($("#farmId").val() == "") {
//        param = null;
//    } else {
//        param = {"farmId": $("#farmId").val()};
//    }
//    $.ajax({
//        type: "post",
//        url: path + "/monitor/getHouse",
//        data:param,
//        dataType: "json",
//        cache: false,
//        async: false,
//        success: function (result) {
//            var list = result.obj;
//            // alert(result);
//            // alert(list);
//            $("#houseId option").remove();
//            for (var i = 0; i < list.length; i++) {
//                $("#houseId").append("<option value=" + list[i].id + ">" + list[i].house_name + "</option>");
//            }
//            if (houseId != '') {
//                $("#houseId").val(houseId);
//            } else {
//                $("#houseId").val(list[0].id);
//            }
//        }
//    });
//}
