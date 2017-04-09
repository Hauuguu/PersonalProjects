app.controller('Sign-InController', function($scope, $routeParams, $http){
	function validateEmail(email) {
    	var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    	return re.test(email);
	}
	$scope.verify = function() {
		if($scope.username && $scope.ign && $scope.email && $scope.password) {
			if(!validateEmail($scope.email)) {
				$scope.msg = $scope.email + " is not a valid email!";
				$scope.$apply;
			}
			else if(($scope.email).substr(($scope.email).length - 4) != ".edu") {
				$scope.msg = $scope.email + " is not a .edu email!";
				$scope.$apply;
			}
			else {
				$http({
				  method: 'GET',
				  url: '/send/' + $scope.email + '/' + $scope.username + '/' + $scope.password + '/' + $scope.ign
				}).then(function successCallback(response) {
					if(response.data == 'error') {
						$scope.msg = "Email was invalid!";
				    	$scope.$apply;
					}
					else if(response.data == 'conflict') {
						$scope.msg = "Username, ign, or email has already been reserved!";
				    	$scope.$apply;
					}
					else {
						$scope.msg = "Email was sent!";
						$scope.$apply;
					}
				}, function errorCallback(response) {
				    $scope.msg = "Oops! Something went wrong contacting the server!";
				    $scope.$apply;
				});
			}
		}
		else {
			$scope.msg = "Make sure that all fields are filled in!";
			$scope.$apply;
		}
	};
	$scope.update = function() {
		console.log("test");
		$http({
		  method: 'PUT',
		  url: '/api/user/' + $scope.upUsername + '/' + $scope.upPassword + '/' + $scope.role + '/' + $scope.champ + '/' + $scope.status
		}).then(function successCallback(response) {
			if(response.data == 'error') {
				$scope.msg2 = "Invalid user/password combination";
		    	$scope.$apply;
		    }
			else {
				$scope.msg2 = "Account was updated!";
				$scope.$apply;
			}
		}, function errorCallback(response) {
		    $scope.msg = "Oops! Something went wrong contacting the server!";
		    $scope.$apply;
		});
	};
});