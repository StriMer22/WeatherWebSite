var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

app.controller('past-weatherCtrl', ['$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {

    $scope.past = localStorage.getItem("date")==null?false:new Date(localStorage.getItem("date"))<new Date();
    $scope.graph = $scope.$state.params.graph;
    $scope.day = $scope.$state.params.day;
    $scope.graphTitle = $scope.$state.params.graphTitle;
    $scope.showWeatherForDate = function (date) {
        var currentDate = new Date(date);
        $scope.past = currentDate < new Date();
        localStorage.setItem("date", date)
        $scope.sendRequest($scope.$state.params.hrs, 1, $scope.past, localStorage.getItem("date"));
        $scope.sendGraphRequest(1, 1, $scope.past, localStorage.getItem("date"));
    };

    if($scope.$state.params.hrs === 1){
        document.getElementById('hr-selector3').className="";
        document.getElementById('hr-selector1').className="active";
        document.getElementById('h-tab-2').className = "tab-hour-content";
        document.getElementById('h-tab-1').className = document.getElementById('h-tab-1').className+ " active";

    }else {
        document.getElementById('h-tab-1').className = "tab-hour-content";
        document.getElementById('h-tab-2').className = document.getElementById('h-tab-2').className+ " active";
        document.getElementById('hr-selector1').className="";
        document.getElementById('hr-selector3').className="active";
    }

    $scope.refreshTableWithHours = function (hours) {
        var path = window.location.pathname;

        if(!path.includes('_')){
            window.location.pathname = path.replace(path.charAt(path.length-2), hours);
        }else{
            window.location.pathname = path.replace(path.charAt(19), hours);
        }
    };
    var sendingTableRequest = {
        method: 'GET',
        url: '/get_dynamic_table_data',
        params: {
            numOfHours:$scope.$state.params.hrs,
            numOfDays:1,
            pastWeather:$scope.past,
            date:localStorage.getItem("date")},
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        }
    }

    $http(sendingTableRequest).success(function (data) {
        $scope.dynamicTableData = data;
    })

    var sendingGraphRequest = {
        method: 'GET',
        url: '/get_detailed_forecast_for_past',
        params: {
            numOfHours:1,
            numOfDays:1,
            pastWeather:$scope.past,
            date:localStorage.getItem("date")},
        headers: {
            'Content-Type': 'application/json; charset=utf-8'
        }
    }

    $http(sendingGraphRequest).then(function (response) {
        $scope.$parent.detailedTemp = response;
        if($rootScope.pageContent == undefined){
            $rootScope.updateLang();
        }
        setTimeout(function () {
            readyGet(response, [], $scope.local.typeTemp, 'today', $rootScope.pageContent.inGraphTitle, $scope.local.timeRange);
        },300);
    })

    $scope.sendRequest = function (numOfHrs, days, pastWeath, date) {
        var sendingTableRequest = {
            method: 'GET',
            url: '/get_dynamic_table_data',
            params: {
                numOfHours:numOfHrs,
                numOfDays:days,
                pastWeather:pastWeath,
                date:date},
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }

        $http(sendingTableRequest).success(function (data) {
            $scope.dynamicTableData = data;
        })

    };

    $scope.sendGraphRequest = function (numOfHrs, days, pastWeath, date) {
        var sendingGraphRequest = {
            method: 'GET',
            url: '/get_detailed_forecast_for_past',
            params: {
                numOfHours:numOfHrs,
                numOfDays:days,
                pastWeather:pastWeath,
                date:date},
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }

        $http(sendingGraphRequest).then(function (response) {
            $scope.$parent.detailedTemp = response;
            if($rootScope.pageContent == undefined){
                $rootScope.updateLang();
            }
            setTimeout(function () {
                readyGet(response, [], $scope.local.typeTemp, 'today', $rootScope.pageContent.inGraphTitle, $scope.local.timeRange);
            },300);
        })

    };
}]);
