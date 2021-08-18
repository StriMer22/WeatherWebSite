var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

// Enter page

app.controller('three-daysCtrl',['$scope', '$http', '$state','$stateParams','$rootScope', function($scope, $http, $state, $stateParams, $rootScope) {
    $scope.$state = $state;
    $scope.$stateParams = $stateParams;
    $scope.selectedTab = 1;
    $scope.graph = $scope.$state.params.graph;
    $scope.tabClass = $scope.$state.params.tabClass;
    $scope.page=$scope.$state.params.page;
    $scope.graphTitle = $scope.$state.params.graphTitle;

    $scope.getData = function () {
        var sendingTableRequest = {
            method: 'GET',
            url: '/get_table_data_for_days',
            params: {
                numOfHours:1,
                numOfDays:$scope.$state.params.index,
                pastWeather:false
            },
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        }
        $http(sendingTableRequest).success(function (data) {
            $scope.dynamicTableData = data;
            console.log($scope.dynamicTableData);

            if($scope.page=='fourteen-days'){
                setTimeout(function () {

                    $(function () {
                        if ($('.tb-slider').length) {
                            if ($(window).width() >= '881') {
                                try {
                                    $('.tb-slider').slick({
                                        infinite: false,
                                        //speed: 300,
                                        slide: 'li',
                                        slidesToShow: 7,
                                        slidesToScroll: 7,
                                        prevArrow: '<button type="button" class="slick-prev slick-arrow"><</button>',
                                        nextArrow: '<button type="button" class="slick-next slick-arrow">></button>'
                                    });

                                } catch (e) {
                                    console.log()
                                }
                            }
                        }
                    });
                    $(window).resize();
                }, 600);

                $(window).resize()
                setTimeout(function () {
                    $(".tb-tabs-header").css({"visibility" : "visible"});
                },600);

            }
            if($scope.$state.params.page === 'three-days') {
                readyGet($scope.dynamicTableData, [], $scope.local.typeTemp, $scope.$state.params.page, $rootScope.pageContent.inGraphTitle, $scope.local.timeRange)
            }
        })


    };

    if($scope.$state.params.page === 'seven-days' || $scope.$state.params.page === 'fourteen-days') {
        $http.post('/get_detailed_forecast').then(function (response) {
            readyGet(response, [], $scope.local.typeTemp, $scope.$state.params.page, $rootScope.pageContent.inGraphTitle, $scope.local.timeRange)
        });
    }
    $scope.selectTab = function (index) {
        activateTab(index);
        $scope.selectedTab = index;
        $scope.getData();
    }

    $http.post('/get_weekly_weather_summary').then(function (response) {
        $scope.$parent.weekly_weather_summary = response.data;
    });

}])