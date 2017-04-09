app.controller('IndexController', function($scope, $window){
  $scope.pageClass = 'page-home'; /* Added this badboy */
  $scope.choice = {};
  $scope.choices = [
    { name: 'University of Massachusetts Amherst (UMASS)', detail: 'Amherst, MA', id: 'UMass_Amherst'},
    { name: 'Boston University (BU)', detail: 'Boston, MA'}
  ];
  function update(id, yLabel) {
    console.log("test");
    $window.location.href = '#school/' + id;
  }
  
  $scope.$watch('choice.selected', function(chosen) {
    if(chosen) update(chosen.id, chosen.detail);
  });
});