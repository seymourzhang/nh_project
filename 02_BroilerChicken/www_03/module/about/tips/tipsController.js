angular.module('myApp.tips', 
		['ionic','ngCordova','ngTouch',
		 'ui.grid','ui.grid.pinning','ui.grid.edit','ui.grid.cellNav', 'ui.grid.validate', 'ui.grid.grouping', 'ui.grid.selection','ui.grid.resizeColumns','ui.grid.autoResize'
		 ])
.controller("tipsCtrl",function($scope, $state, $http, AppData) {
	$scope.setUI = function(){
		document.getElementById("imgSlide").style.height = (DeviInfo.ScreenHeight * 0.8) + 'px';
		document.getElementById("helpImg1").style.height = (DeviInfo.ScreenHeight * 0.7) + 'px';
		document.getElementById("helpImg2").style.height = (DeviInfo.ScreenHeight * 0.7) + 'px';
	};
	
	setTimeout(function() {
		$scope.setUI();
	}, 1500);
})