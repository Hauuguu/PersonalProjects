var app = angular.module('UniLol', ['ui.select', 'ngRoute', 'ngAnimate']);

app.config(function($routeProvider) { 
  $routeProvider 
    .when('/', { 
      templateUrl: 'views/index.html',
      controller: 'IndexController'
    })
    .when('/school/:schoolID', {
      templateUrl: 'views/school.html',
      controller: 'SchoolController'
    })
    .when('/sign-in', {
      templateUrl: 'views/sign-in.html',
      controller: 'Sign-InController'
    })
    .otherwise({ 
      redirectTo: '/' 
    });
});