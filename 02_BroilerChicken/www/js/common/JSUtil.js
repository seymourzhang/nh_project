function getTest(){
	// alert(AppData);
	//alert('Success');
}

/**
* description :自动补零函数
* paras:num-需要补零的数字，fill-结果字符串长度
* 
*/
function padNumber(num, fill) {
	var len = ('' + num).length;
	return (Array(
    	fill > len ? fill - len + 1 || 0 : 0
	).join(0) + num);
}

/**
* description :判断是否是数字
* paras:s
* 
*/
function app_IsNum(s)
{
    if (s!=null && s!="")
    {
        return !isNaN(s);
    }
    return false;
}
/**
* description :播放报警声音的函数
* paras:num-需要补零的数字，fill-结果字符串长度
* 
*/
var APP_Media = function(){
	var local_Media = {};
	var my_media = null;
	var isPlayed = false;
	var playAudio = function(src){
		
		if(my_media == null){
			my_media = new Media(src, onSuccess, onError);
		}
		if(!isPlayed){
			my_media.play();
		}
	};
	
	var onSuccess = function() {
		console.log("playAudio():Audio Success");
		// alert("playAudio():Audio Success");
	};
	var onError = function(error) {
		//alert('code: '    + error.code    + '\n' + 'message: ' + error.message + '\n');
	};
	
	var pauseAudio = function() {
		if (my_media) {
			my_media.pause();
		}
		// alert('pauseAudio');
	}
	var stopAudio = function() {
		if (my_media) {
			my_media.stop();
			my_media.release();
		}
		// alert('stopAudio');
	}
	
	local_Media.playAudio = playAudio;
	local_Media.pauseAudio = pauseAudio;
	local_Media.stopAudio = stopAudio;
	
	return local_Media;
}();

var APP_Notification = function(){
	var tId = 0;
	var addOneNotification = function(nType,title,text){
		tId ++ ;
		try{
			cordova.plugins.notification.local.schedule({
				id: tId,
				title: title,
				text: text,
				data: {type: nType }
			});
			cordova.plugins.notification.local.on("click", function (notification) {
				var jsonData = JSON.parse(notification.data);
				if(jsonData.type == 'alarm'){
					APP_Media.stopAudio();
				}
			});
		}catch(e){
			//console.log(e);
		}
		return tId;
	};
	
	var updateNotification = function(id,title,text){
		try{
			cordova.plugins.notification.local.update({
				id: id,
				title: title,
				text: text
			});
		}catch(e){
			console.log(e);
		}
		return id;
	};
	
	var cancleNotification = function(id) {
		try{
			cordova.plugins.notification.local.cancel(id, function () {
				// Notification was cancelled
				//alert('cancle');
			});
		}catch(e){
			//console.log(e);
		}
	};
	var local_Notification = {};
	local_Notification.addOneNotification = addOneNotification;
	local_Notification.updateNotification = updateNotification;
	local_Notification.cancleNotification = cancleNotification;
	return local_Notification;
}();

var getMediaURL = function(path) {

	var ua = navigator.userAgent.toLowerCase(); 
	if (/iphone|ipad|ipod/.test(ua)) {
		if(device.platform.toLowerCase() === "android") {
			return "file:///android_asset/www/" + path;	
		}else{
			return path;
		}
	} else if (/android/.test(ua)) {
		if(device.platform.toLowerCase() === "android") {
			return "file:///android_asset/www/" + path;	
		}else{
			return path;
		}
	}else{
		console.log("________________非手机平台无效。");
	}
}

function isOnLine_Wifi(){
	return navigator && navigator.connection && navigator.connection.type==Connection.WIFI;
}
function isOnLine(){
	return navigator && navigator.connection && navigator.connection.type!=Connection.NONE;
}

function app_alert(message,title,buttonName,callback){
	if(title == null){
		title = "提示";
	}
	if(buttonName == null){
		buttonName = "确 定";
	}
	try{
		navigator.notification.alert(
		    message, 
		    callback, 
		    title,  
		    buttonName 
		);
	}catch(e){
		console.log(e);
		alert(message);
	}
}
function app_confirm(message,title,buttonLabels,onConfirm){
	/*
	navigator.notification.confirm(
	    'You are the winner!', // message
	     onConfirm,            // callback to invoke with index of button pressed
	    'Game Over',           // title
	    ['Restart','Exit']     // buttonLabels
	);
	*/
	if(title == null){
		title = "提示";
	}
	if(buttonLabels == null){
		buttonLabels = ['取 消','确 定'];
	}
	try{
		navigator.notification.confirm(
		    message, 
		    onConfirm, 
		    title,  
		    buttonLabels 
		);
	}catch(e){
		//console.log(e);
		var r = confirm(message);
		if (r==true){
		    onConfirm && onConfirm(2);
		}else{
			onConfirm && onConfirm(1);
	    }
	}
}

function NulltoZero(Obj){
	if (Obj === "" ||!Obj) {
		return 0;
	}else{
		return Obj;
	};
}

function ZerotoNull(Obj){
	if (Obj === 0) {
		return "";
	}else{
		return Obj;
	};
}

function isNull(Obj){
	if (Obj === "" ||!Obj) {
		return app_alert("尚有内容未填写...");
	}else{
		return true;
	};
}

function judgeDevice(){
	var ua = navigator.userAgent.toLowerCase();	
	if (/iphone|ipad|ipod/.test(ua)) {
		return "iphone";	
	} else if (/android/.test(ua)) {
		return "android";	
	}
	return 0;
}

function app_prompt(message,title,buttonLabels,onPrompt,defaultText){
	/*
	navigator.notification.prompt(
	    'Please enter your name',  // message
	    onPrompt,                  // callback to invoke
	    'Registration',            // title
	    ['Ok','Exit'],             // buttonLabels
	    'Jane Doe'                 // defaultText
	);
	*/
	if(title == null){
		title = "提示";
	}
	if(buttonLabels == null){
		buttonLabels = ['取 消','确 定'];
	}
	try{
		navigator.notification.prompt(
		    message, 
		    onPrompt, 
		    title,  
		    buttonLabels,
		    defaultText
		);
	}catch(e){
		console.log(e);
	}
}


function app_vibrate(paraArray){
	navigator.vibrate(paraArray);
}

function app_wakeLock(){
	try{
		window.powerManagement.acquire(function() {
		    // app_alert('Wakelock acquired');
		}, function() {
		    app_alert('Failed to acquire wakelock');
		});
	}catch(e){
		console.log(e);
	}
}

function app_wakeLockDim(){
	try{
		window.powerManagement.dim(function() {
		//    app_alert('Wakelock dim acquired');
		}, function() {
		    app_alert('Failed to acquire dim wakelock');
		});
	}catch(e){
		//console.log(e);
	}
}

function app_releaseWakeLock(){
	try{
		window.powerManagement.release(function() {
		    //app_alert('Wakelock released');
		}, function() {
		    app_alert('Failed to release wakelock');
		});
	}catch(e){
		console.log(e);
	}
}

function app_setReleaseOnPause(){
	try{
		window.powerManagement.setReleaseOnPause(false, function() {
		    //app_alert('Set successfully');
		}, function() {
		    app_alert('Failed to set');
		});
	}catch(e){
		console.log(e);
	}
}

function saveUserInfo(userInfo){
	if(userInfo != null){
		localStorage.userInfo = userInfo; //设置一个键值 
	}
}
function getUserInfo(){
	if(localStorage.userInfo){
		return JSON.parse(localStorage.userInfo) ;
	}else{
		return null;
	}
}
function getCurDate(){
	var myDate = new Date();
	var mYear = myDate.getFullYear();   	//获取完整的年份(4位,1970-????)
	var mMonth = myDate.getMonth()+1;       //获取当前月份(0-11,0代表1月)
    var mDate = myDate.getDate();        //获取当前日(1-31)

    mMonth = mMonth<10?'0'+ mMonth:mMonth;
    mDate = mDate<10?'0'+ mDate:mDate;

	return mYear + '-' + mMonth + '-' + mDate ;
}

function appUpGrade($ionicLoading,$cordovaFileTransfer,$cordovaFileOpener2,$timeout,appVersion){
	
	var apkName = "nht_broiler_pro_"+appVersion+".apk"; 

	var url = API_Host + apkName; //可以从服务端获取更新APP的路径

	// var targetPath = "/storage/emulated/0/" + apkName ;  //APP下载存放的路径，可以使用cordova file插件进行相关配置
	var targetPath = cordova.file.externalRootDirectory + apkName;
	var trustHosts = true;
	var options = {};

	$cordovaFileTransfer.download(url, targetPath, options, trustHosts).then(function (result) {
		
		//打开下载下来的APP
        $cordovaFileOpener2.open(targetPath, 'application/vnd.android.package-archive'
        ).then(function () {
                // 成功
        }, function (err) {
            // 错误
        });
        
	}, function (err) {
		targetPath = "/storage/sdcard0/" + apkName ; 
		$cordovaFileTransfer.download(url, targetPath, options, trustHosts).then(function (result22) {
			
			// 打开下载下来的APP
	        $cordovaFileOpener2.open(targetPath, 'application/vnd.android.package-archive'
	        ).then(function () {
	                // 成功
	        }, function (err22) {
	            // 错误
	        });
	        
		}, function (err22) {
			app_alert('更新失败,失败原因：' + JSON.stringify(err22));
		}, function (progress22) {
			//进度，这里使用文字显示下载百分比
	        $timeout(function () {
	            var downloadProgress22 = (progress22.loaded / progress22.total) * 100;
	            $ionicLoading.show({
	                template: "已经下载：" + Math.floor(downloadProgress22) + "%"
	            });
	            if (downloadProgress22 > 99) {
	                $ionicLoading.hide();
	            }
	        })
		});
	}, function (progress) {
		//进度，这里使用文字显示下载百分比
        $timeout(function () {
            var downloadProgress = (progress.loaded / progress.total) * 100;
            $ionicLoading.show({
                template: "已经下载：" + Math.floor(downloadProgress) + "%"
            });
            if (downloadProgress > 99) {
                $ionicLoading.hide();
            }
        })
	});
}

function barScan(succCallBack,failCallBack){
   try{
		cordova.plugins.barcodeScanner.scan(
	      function (result) {
	      	// alert("We got a barcode\n" +
	       //          "Result: " + result.text + "\n" +
	       //          "Format: " + result.format + "\n" +
	       //          "Cancelled: " + result.cancelled);
	      	succCallBack && succCallBack(result);
	      }, 
	      function (error) {
	        // alert("Scanning failed: " + error);
	        failCallBack && failCallBack(error);
	      }
	    );
	}catch(e){
		console.log(e);
	}
};





/*function app_lockOrientation(orientation){
	// portrait-保持竖屏;landscape-横屏
	console.log("请求屏幕方向" + orientation);
	console.log("当前屏幕方向" + screen.orientation);
	try{
		screen.lockOrientation(orientation);
	}catch(e){
		console.log(e);
	}
}

function setLandscape(setiOS,setAndroid){
	var ua = navigator.userAgent.toLowerCase();	
	if (/iphone|ipad|ipod/.test(ua)) {
		console.log("ios");
		if (setiOS == false) {

		}else{
			setTimeout(
				function (){
			app_lockOrientation('landscape');//进入时横屏
		}
	,1000);
		}
	} else if (/android/.test(ua)) {
		console.log("android");
		if (setAndroid == false) {

		}else{
			setTimeout(
				function (){
					app_lockOrientation('landscape');//进入时横屏
				}
			,1000);
		}
	}else{
		console.log("________________非手机平台旋转无效。");
	}
}

function setPortrait(setiOS,setAndroid){
	var ua = navigator.userAgent.toLowerCase();	
	if (/iphone|ipad|ipod/.test(ua)) {
		console.log("ios");
		if (setiOS == false) {

		}else{
			setTimeout(
				function (){
					app_lockOrientation('portrait');//进入时竖屏
				}
			,1000);
		}
	} else if (/android/.test(ua)) {
		console.log("android");
		if (setAndroid == false) {

		}else{
			setTimeout(
				function (){
					app_lockOrientation('portrait');//进入时竖屏
				}
			,1000);
		}
	}else{
		console.log("________________非手机平台旋转无效。");
	}
}*/


function isAndroid(){
	if(judgeDevice() == 'android'){
		return true;
	}else{
		return false;
	}
}


function isIOS(){
	if(judgeDevice() == 'iphone'){
		return true;
	}else{
		return false;
	}
}

var initJpushUI = function() {
    try {
        window.plugins.jPushPlugin.init();
		getRegistrationID();

		if(isIOS()){
		  window.plugins.jPushPlugin.setDebugModeFromIos();
		  window.plugins.jPushPlugin.setApplicationIconBadgeNumber(0);
		}else if(isAndroid()){
		  window.plugins.jPushPlugin.setDebugMode(false);
		  window.plugins.jPushPlugin.setStatisticsOpen(true);
		}
		var onTagsWithAlias = function(event) {
		  try {
		     console.log("onTagsWithAlias");    
		     var result = "result code:"+event.resultCode + " ";
		     result += "tags:" + event.tags + " ";
		     result += "alias:" + event.alias + " ";
		  } catch(exception) {
		      console.log(exception);
		  }
		}
		document.addEventListener("jpush.setTagsWithAlias", onTagsWithAlias, false);

        if (isIOS()) {
            window.plugins.jPushPlugin.setDebugModeFromIos();
            window.plugins.jPushPlugin.setApplicationIconBadgeNumber(0);
        } else if(isAndroid()){
            window.plugins.jPushPlugin.setDebugMode(true);
            window.plugins.jPushPlugin.setStatisticsOpen(true);
        }
    } catch (exception) {
        //console.log(exception);
    }
};
var JpushTags = ["mtc_tag_0"];
var JpushAlias = "mtc_alias_0";
var app_setTagsWithAlias = function(tags,alias) {
    try {
    	JpushTags = tags;
    	JpushAlias = alias ;
        window.plugins.jPushPlugin.setTagsWithAlias(JpushTags, JpushAlias);
    } catch (exception) {
        //console.log(exception);
    }
};

var getRegistrationID = function() {
    window.plugins.jPushPlugin.getRegistrationID(onGetRegistrationID);
};

var onGetRegistrationID = function(data) {
    try {
        if (data.length == 0) {
            var t1 = window.setTimeout(getRegistrationID, 1000);
        }else{
        	app_setTagsWithAlias(JpushTags, JpushAlias);
        }
    } catch (exception) {
        console.log(exception);
    }
};

// 向本地存储存放数据
function saveObjectToLocalStorage(key,obj) {
	try{
		if(key != null && obj != null){
			localStorage[key] = (obj); //设置一个键值 
		}
	}catch(e){
		throw e;
	}
	
}
// 从本地存储获取数据
function getObjectFromLocalStorage(key){
	try{
		if(key){
			var obj = localStorage[key];
			return obj;
		}else{
			return null;
		}
	}catch(e){
		throw e;
	}
}

// 向本地存储删除数据
function deleteObjectFromLocalStorage(key){
	try{
		if(key != null){
			delete localStorage[key];
		}
	}catch(e){
		throw e;
	}
}

// 设置device的Imei 
function setDeviceImei(){
	try{
		window.plugins.imeiplugin.getImei(setImei);
	    function setImei(imei) {
	        ANDROID_IMEI = imei;
	        console.log("My Android IMEI :" + ANDROID_IMEI);
	    }
	    function onDeviceReady() {
			    UUID = device.uuid;
				MODELNAME = device.model;
				VERSION = device.version;
				PLATFORM = device.platform;
		}
	    document.addEventListener("deviceready", onDeviceReady, false);
	}catch(exception){
		console.log(exception);
		ANDROID_IMEI = 'null';
	}
}