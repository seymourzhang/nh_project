angular.module('myApp.modifyUserInfo', 
    ['ionic','ngCordova','ngTouch',
     'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
     ])
//密码修改
.controller("modifyUserInfoCtrl",function($scope, $state, $http, AppData) {

  var oldPw;
  var newPw;
  var confirmPw;
  $scope.oldPassWordLostFocus = function(oldPassWord){
    oldPw = oldPassWord;
  }

  $scope.newPassWordLostFocus = function(newPassWord){
    newPw = newPassWord;
  }

  $scope.confirmPassWordLostFocus = function(confirmPassWord){
    confirmPw = confirmPassWord;
  }

  $scope.saveUpdate = function(){

    if (oldPw == null) {
      Sparraw.myNotice("请输入旧密码,谢谢。");
    }else if (newPw == null) {
      Sparraw.myNotice("请输入新密码,谢谢。");
    }else {
      if (newPw != confirmPw) {
        Sparraw.myNotice("两次密码输入不一致请确认后再进行提交,谢谢。");
      }else {

        if(newPw && /(?![0-9A-Z]+$)(?![0-9a-z]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{8,}$/.test(newPw)){
          //对的
          console.log("输入正确");
        } else{
          //不对
          return app_alert("密码至少由8个字符组成，并且必须包含至少1个大写字母、1个小写字母以及1个数字，请重新输入！");
        };

        var params = {
                    'user_id'   : sparraw_user.userinfo.id        ,
                    'user_code' : sparraw_user.userinfo.userCode  ,
                    'old_pw'    : oldPw                           ,
                    'new_pw'    : newPw                           
                 };

        Sparraw.ajaxPost('userMobile/updatePassword', params, function(data){
            if (data.ResponseDetail.Result == "Success") {
               Sparraw.myNotice("修改成功");
               sparraw_user.profile.user_State = true;
               $state.go("landingPage");
            }else {
               Sparraw.myNotice(data.ResponseDetail.Error);
            };
        });

      };
    };
    


  }

})