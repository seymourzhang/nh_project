$(document).ready(function(){
	region_top.setStyle("login");
});

var login = {
    // 触发系统登录方法
    enterSumbit: function(){
		var event=arguments.callee.caller.arguments[0]||window.event;//消除浏览器差异
		if (event.keyCode == 13){
			this.systemLogin();
    	}
	},
	// 登录成功时的方法
    loginSuccess: function(result) {
        if(result.msg=='1'){
            $("#userName").tips({
                side : 1,
                msg : "用户名或密码有误，请重新输入！",
                time : 3
            });
            $("#userName").focus();
        }else{
            window.location.href=system.path+"/user/index";
        }
    },
	// 系统登录方法
    systemLogin: function (){
		var param =$.serializeObject($('#loginForm'));
		if(param["user_code"] == ""){
			$("#userName").tips({
				side : 1,
				msg : '用户名不能为空，请重新输入!',
				time : 3
			});
			$("#userName").focus();
			return;
		}
		if(param["user_password"] == ""){
			$("#password").tips({
				side : 1,
				msg : '密码不能为空，请重新输入!',
				time : 3
			});
			$("#password").focus();
			return;
		}

		var config = {
			url: "/login/login",
			data: param
		};

		system.request(config, this.loginSuccess);
        //
		// $.ajax({
		// 	url: system.path + "/login/login",
		// 	data: param,
		// 	type : "POST",
		// 	dataType: "json",
		// 	success: function(result) {
        //
		// 		if(result.msg=='1'){
		// 			$("#userName").tips({
		// 				side : 1,
		// 				msg : "用户名或密码有误，请重新输入！",
		// 				time : 3
		// 			});
		// 			$("#userName").focus();
		// 		}else{
		// 			window.location.href=system.path+"/user/index";
		// 		}
		// 	}
		// });
	},


};


