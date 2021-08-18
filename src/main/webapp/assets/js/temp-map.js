var app = angular.module('main', ['ui.router', 'oc.lazyLoad']);

  app.controller('tempMapCtrl', ['$scope', '$http', '$state', '$element', 'locationsModel', '$cookies', function($scope, $http, $state, $element, locationsModel, $cookies) {
      $scope.currentTabMap="";

      $scope.dayTrans = ["Day", "der Tag", "Giorno", "День", "Дзень", "Jour"];
      $scope.nightTrans = ["Night", "die Nacht", "Notte", "Ніч", "Ночь", "Ноч", "Nuit"];
   //   var slav = ["ua", "by", "ru"];
      // if(slav.includes(location.pathname.split("/")[1])){
      //     $scope.outTable = "slavTable";
      // }else{
      //     $scope.outTable = "enTable"
      // }

      $http.post('/get_4_days_tabs').success(function (response) {
          $scope.tabs=response;
          if($cookies.get('currentTabMap')==undefined){
              $cookies.put('currentTabMap', response[0].time);
              $scope.currentTabMap = response[0].time;
          }
          else if((response.some(function (value) { return $cookies.get('currentTabMap')==value.time}))==false) {
              $cookies.put('currentTabMap', response[0].time);
              $scope.currentTabMap = response[0].time;
          }
            else {
              $scope.currentTabMap = $cookies.get('currentTabMap');
          }

      }).error(function () {

      });
      $http.post('/get_weekly_weather/'+location.pathname.split("/")[1]).then(function (response) {
          $scope.$parent.temperatureWeekly = response;
      });;

      $scope.select_tab_map = function (time) {
          $scope.currentTabMap=time;
          $cookies.put('currentTabMap', time);
          $state.reload();
      };


      $http.post('/get_coordinates').then(function (response) {
          const mapEl = $element.find('div#gmap')[0]

          const mapOptions = {
              zoom: 5,
              minZoom: 3,
              maxZoom: 10,

              center: new google.maps.LatLng(response.data.latitude, response.data.longitude),

              styles: [{
                  "featureType": "water",
                  "elementType": "geometry",
                  "stylers": [{"color": "#e9e9e9"}, {"lightness": 17}]
              }, {
                  "featureType": "landscape",
                  "elementType": "geometry",
                  "stylers": [{"color": "#f5f5f5"}, {"lightness": 20}]
              }, {
                  "featureType": "road.highway",
                  "elementType": "geometry.fill",
                  "stylers": [{"color": "#ffffff"}, {"lightness": 17}]
              }, {
                  "featureType": "road.highway",
                  "elementType": "geometry.stroke",
                  "stylers": [{"color": "#ffffff"}, {"lightness": 29}, {"weight": 0.2}]
              }, {
                  "featureType": "road.arterial",
                  "elementType": "geometry",
                  "stylers": [{"color": "#ffffff"}, {"lightness": 18}]
              }, {
                  "featureType": "road.local",
                  "elementType": "geometry",
                  "stylers": [{"color": "#ffffff"}, {"lightness": 16}]
              }, {
                  "featureType": "poi",
                  "elementType": "geometry",
                  "stylers": [{"color": "#f5f5f5"}, {"lightness": 21}]
              }, {
                  "featureType": "poi.park",
                  "elementType": "geometry",
                  "stylers": [{"color": "#dedede"}, {"lightness": 21}]
              }, {
                  "elementType": "labels.text.stroke",
                  "stylers": [{"visibility": "on"}, {"color": "#ffffff"}, {"lightness": 16}]
              }, {
                  "elementType": "labels.text.fill",
                  "stylers": [{"saturation": 36}, {"color": "#333333"}, {"lightness": 40}]
              }, {"elementType": "labels.icon", "stylers": [{"visibility": "off"}]}, {
                  "featureType": "transit",
                  "elementType": "geometry",
                  "stylers": [{"color": "#f2f2f2"}, {"lightness": 19}]
              }, {
                  "featureType": "administrative",
                  "elementType": "geometry.fill",
                  "stylers": [{"color": "#fefefe"}, {"lightness": 20}]
              }, {
                  "featureType": "administrative",
                  "elementType": "geometry.stroke",
                  "stylers": [{"color": "#fefefe"}, {"lightness": 17}, {"weight": 1.2}]
              }]
          };
          const gmap = new google.maps.Map(mapEl, mapOptions);

          google.maps.event.addListener(gmap, 'bounds_changed', function() {

              var aNorth  =   gmap.getBounds().getNorthEast().lat();
              var aEast   =   gmap.getBounds().getNorthEast().lng();
              var aSouth  =   gmap.getBounds().getSouthWest().lat();
              var aWest   =   gmap.getBounds().getSouthWest().lng();

              var sendingTableRequest = {
                  method: 'POST',
                  url: '/get_map_weather',
                  params: {
                      max: 10,
                      north: aNorth,
                      west: aWest,
                      south: aSouth,
                      east: aEast,
                      time: $scope.currentTabMap
                  },
                  headers: {
                      'Content-Type': 'application/json; charset=utf-8'
                  }
              };

              $http(sendingTableRequest).success(function (data) {
                  $scope.gmap = gmap;
                  $scope.locations = data;
              }).error(function () {
                  $scope.gmap = gmap;
                  $scope.locations = locationsModel;
              })
          })
      });
  }])    .directive('mapMarker', function () {
      return {
          restrict: 'E',
          controller: 'MapMarkerCtrl'
      };
  })
      .factory('locationsModel', function() {
          const locationsModel = [{
              img: 'PartlyCloudy',
              temp_C: 6,
              temp_F: 90,
              day: "night",
              lat: 49.84,
              lng: 24.02
          },{
              img: 'Clear',
              temp_C: 10,
              temp_F: 60,
              day: "day",
              lat: 50.45,
              lng: 30.52
          }];

          return locationsModel;
      });
