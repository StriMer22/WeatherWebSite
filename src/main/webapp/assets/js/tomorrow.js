var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

app.controller('tomorrowCtrl', function($scope, $http) {


    var sendingTableRequest = {
        method: 'GET',
        url: '/get_dynamic_table_data',
        params: {numOfHours:3, numOfDays:2, pastWeather:false, forTomorrow:true},
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        }
    }

    $http(sendingTableRequest).success(function (data) {
        $scope.$parent.dynamicTableData = data;
        console.log($scope.$parent.dynamicTableData)
    })


})