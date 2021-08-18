var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

app.controller('not-universal-daysCtrl',['$scope', '$http', '$state','$stateParams', '$rootScope', function($scope, $http, $state, $stateParams, $rootScope) {
    $scope.graph = $scope.$state.params.graph;
    $scope.page = $scope.$state.params.page;
    $scope.graphTitle = $scope.$state.params.graphTitle;
    $scope.dayTrans = ["Day", "der Tag", "Giorno", "День", "Дзень", "Jour"];
    $scope.nightTrans = ["Night", "die Nacht", "Notte", "Ніч", "Ночь", "Ноч", "Nuit"];
    $scope.dayTransSlav = [];
    $scope.nightTransSlav = [];
    var slav = ["ua", "by", "ru"];
    if(slav.includes(location.pathname.split("/")[1])){
        $scope.outTable = "slavTable";
    }else{
        $scope.outTable = "enTable"
    }
    $scope.getData = function () {
        var sendingTableRequest = {
            method: 'POST',
            url: '/get_not_universal_table_data/'+ location.pathname.split("/")[1],
            params: {
                numOfDays:$scope.$state.params.index
            },
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        };

        $http(sendingTableRequest).then(function (response) {
            console.log(response);
            $scope.$parent.temperatureWeekly = response;
        })
    }

    if($scope.$state.params.page === 'five-days'){
        $scope.getDataGraph = function () {
            var sendingTableRequest = {
                method: 'GET',
                url: '/get_table_data_for_days',
                params: {
                    numOfHours:1,
                    numOfDays:5,
                    pastWeather:false
                },
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                }
            }

            $http(sendingTableRequest).success(function (data) {
                console.log(1)
                readyGet(data, [], $scope.local.typeTemp, $scope.$state.params.page, $rootScope.pageContent.inGraphTitle, $scope.local.timeRange)
            })
        }
    } else if($scope.$state.params.page === 'ten-days') {
        $http.post('/get_detailed_forecast').then(function (response) {
            readyGet(response, [], $scope.local.typeTemp, $scope.$state.params.page, $rootScope.pageContent.inGraphTitle, $scope.local.timeRange)
        });
    }

    $http.post('/get_weekly_weather_summary').then(function (response) {
        $scope.$parent.weekly_weather_summary = response.data;
    });


}]);