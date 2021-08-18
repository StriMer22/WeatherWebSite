var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

  app.controller('todayCtrl', ['$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {

      $scope.graph = $scope.$state.params.graph;
      $scope.day = $scope.$state.params.day;
      $scope.graphTitle = $scope.$state.params.graphTitle;
      $http.post('/get_weekly_weather_summary').then(function (response) {
          $scope.$parent.weekly_weather_summary = response.data;
      });

      $http.post('/get_astronomy').then(function (response) {
          $scope.$parent.moon_phase_index = response.data['moon_phase_index'];
          $scope.$parent.moon_phase_name = response.data['moon_phase_name'];
          $scope.$parent.astronomy = response.data;
      });

      $http.post('/get_detailed_forecast_today').then(function (response) {
          $scope.$parent.detailedTemp = response;
          if($rootScope.pageContent == undefined){
              $rootScope.updateLang();
          }
          setTimeout(function () {
              readyGet(response, [], $scope.local.typeTemp, 'today', $rootScope.pageContent.inGraphTitle, $scope.local.timeRange);
          },300);

      });

      var sendingTableRequest = {
          method: 'GET',
          url: '/get_dynamic_table_data',
          params: {numOfHours:3, numOfDays:1, pastWeather:false},
          headers: {
              'Content-Type': 'application/json; charset=utf-8'
          }
      }

      $http(sendingTableRequest).success(function (data) {
          $scope.$parent.dynamicTableData = data;
          console.log($scope.$parent.dynamicTableData)
      })


  }]);
