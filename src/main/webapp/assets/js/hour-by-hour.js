var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

app.controller('hour-by-hourCtrl',['$scope', '$http', '$state','$stateParams', '$rootScope', function($scope, $http, $state, $stateParams, $rootScope) {


    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
    $scope.selectedTab = 1;
    $scope.selectedTabGraph = 1;
    $scope.tabClass = $scope.$state.params.tabClass;
    $scope.hrs = $scope.$state.params.hrs;
    $scope.graphTitle = $scope.$state.params.graphTitle;
    if($scope.hrs === 1){
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
    var path = window.location.pathname;
    var url = path.split('/');
    $scope.refreshTableWithHours = function (hours) {
        $scope.hrs = hours;
        var path = window.location.pathname;
        var url = path.split('/');
        console.log(url)
        if(!path.includes('_')){
            window.location.pathname = path.replace(path.charAt(path.length-2), hours);
        }else{
             window.location.pathname = path.replace(parseInt(hours)==1?"hour-by-hour3":"hour-by-hour1", "hour-by-hour"+hours);
        }
    };
    $scope.getData = function () {

        var sendingTableRequest = {
            method: 'GET',
            url: '/get_dynamic_table_data',
            params: {
                numOfHours:$scope.hrs,
                numOfDays:$scope.$state.params.index,
                pastWeather:false
            },
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }

        $http(sendingTableRequest).success(function (data) {
            $scope.dynamicTableData = data;
        })
    }

    $scope.getDataForGraph = function () {

        var sendingGraphRequest = {
            method: 'GET',
            url: '/get_dynamic_table_data',
            params: {
                numOfHours:1,
                numOfDays:$scope.$state.params.index,
                pastWeather:false
            },
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }

        $http(sendingGraphRequest).success(function (data) {
            $scope.dynamicGraphData = data;
            var response=data[[[$scope.selectedTabGraph-1]]][0];
            readyGet(response, [], $scope.local.typeTemp, 'hour-by-hour', $rootScope.pageContent.inGraphTitle, $scope.local.timeRange)
        })
    }

    $scope.selectTab = function (index) {
        console.log(index);
        activateTab(index);
        $scope.selectedTab = index;
        $scope.getData();
    }

    $scope.selectTabGraph = function (index) {
        // console.log(index);
        // activateTab(index);
        $scope.selectedTabGraph = index;
        $scope.getDataForGraph();
    }

    $http.post('/get_weekly_weather_summary').then(function (response) {
        $scope.$parent.weekly_weather_summary = response.data;
    });
}]);