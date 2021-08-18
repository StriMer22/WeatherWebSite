<div ng-controller="front-pageCtrl">
<section class="three-days">
		<div ng-repeat="elem in header_tabs track by $index" class="three-days-block three-days-{{$index +1}}">
			<div class="tdb-inner">
				<div class="tdb-top">
					{{elem[1].dayOfMonth}} {{elem[1].monthOfYear}}, {{elem[1].dayOfWeek}}
				</div>
				<div class="tdb-bot">
					<div class="tdb-min">
						{{pageContent.min}} <em>{{local.typeTemp=='C'?elem[1].mintempC:elem[1].mintempF}}° <span>{{local.typeTemp}}</span></em>
					</div>
					<div class="tdb-max">
						{{pageContent.max}} <em>{{local.typeTemp=='C'?elem[1].maxtempC:elem[1].maxtempF}}° <span>{{local.typeTemp}}</span></em>
					</div>
					<!-- <img src="images/svg-icons/cloud-sm.svg" alt=""> -->
					<img class="green_tabs_header_icon" ng-src="svg/wicons_svg/{{elem[1].weatherCode}}_night.svg" style="   text-align: center;    margin: auto;" ng-if="elem[1].isDay== false ">
					<img class="green_tabs_header_icon" ng-src="svg/wicons_svg/{{elem[1].weatherCode}}_day.svg" style="    text-align: center;    margin: auto;" ng-if="elem[1].isDay== true ">
				</div>
			</div>
		</div>

	</section><!-- end three-days -->

	<section id="popular-weather">
		<div class="container">
			<div class="weather-block-width-wrap">
				<div ng-repeat="elem in recent_tabs track by $index" class="weather-block-width w{{$index}}" ng-click="selectCity(elem.geonameId)">
					<div class="wb-wrap">
						<div class="wb-left">
							<img ng-src="svg/wicons_svg/{{elem.weatherCode}}_{{(elem.hours<=6 || elem.hours>=18)?'night':'day'}}.svg" style="width: 30px;">
							<div class="wbl-temp">{{local.typeTemp=='C'?elem.tempC:elem.tempF}}° <span>{{local.typeTemp}}</span></div>
						</div>
						<div class="wb-right">
							<a>
								<h3>{{elem.city}}</h3>
								<h4>{{elem.countryName}}</h4>
							</a>
						</div>
					</div>
				</div>

			</div><!-- weather-block-width-wrap -->
		</div><!-- end container -->
	</section><!-- end popular-weather -->

	<div class="container section-margin-top">
		<div class="wrapper">
			<div class="page-content">
				<section id="temperature-map">
					<div class="temperature-heading">
						<h2 ng-bind="pageContent.temperatureMap"></h2>
						<a href="/{{currentCountryCode}}/weather/map/{{selectedCity}}" class="t-view-map" ng-bind="pageContent.viewMap"></a>
					</div>
                    <figure id="map">
                      <div id="gmap"></div>
                      <map-marker
                        ng-repeat="location in locations"
                      </map-marker>
                    </figure>
				</section><!-- end temperature-map -->
			</div><!-- end page-content -->
			<aside class="sidebar">
				<!-- <div class="ad300-250 top62px ">300x250</div> -->
				<img src="https://placehold.it/300x250" alt="alt" class="top62px">

                <a href="https://play.google.com/store/apps/developer?id=Oplao" class="g-play" ng-include="'templates/html/google-play.html'"></a>

			</aside>

		</div><!-- end wrapper -->
	</div><!-- end container -->


	<div class="container section-margin-top">
		<div class="wrapper">
			<div class="page-content">
				<section id="country-weather-section">
					<h2 ng-bind="pageContent.locationWeather"></h2>
					<div class="country-weather">
						<div ng-repeat="elem in country_weather" class="weather-block">
							<div class="wb-left">
								<img ng-src="svg/wicons_svg/{{elem.weatherCode}}_night.svg" style="    width: 45px;    text-align: center;    margin: auto;" ng-if="elem.isDay== false ">
								<img ng-src="svg/wicons_svg/{{elem.weatherCode}}_day.svg" style="    width: 45px;    text-align: center;    margin: auto;" ng-if="elem.isDay== true ">
								<div class="wbl-temp">{{local.typeTemp=='C'?elem.temp_C:elem.temp_F}}° <span>{{local.typeTemp}}</span></div>
							</div>
							<div class="wb-right">
								<a  href="/{{currentCountryCode}}/weather/outlook/{{elem.name+'_'+elem.countryCode}}"><h3>{{elem.name}}</h3></a>
							</div>
						</div>

					</div>
				</section><!-- end country-weather-section -->

				<section id="holiday-weather-section">
                    <h3 ng-bind="pageContent.holidayWeather"></h3>
                    <div class="holiday-weather">
                        <div ng-repeat="elem in holidays_weather" class="weather-block">
                            <div class="wb-left">
                                <img ng-src="svg/wicons_svg/{{elem.weatherCode}}_night.svg" style="    width: 45px;    text-align: center;    margin: auto;" ng-if="elem.isDay== false ">
                                <img ng-src="svg/wicons_svg/{{elem.weatherCode}}_day.svg" style="    width: 45px;    text-align: center;    margin: auto;" ng-if="elem.isDay== true ">
                                <div class="wbl-temp">{{local.typeTemp=='C'?elem.temp_C:elem.temp_F}}° <span>{{local.typeTemp}}</span></div>
                            </div>
                            <div class="wb-right">
                                <a href="/{{currentCountryCode}}/weather/outlook/{{elem.name+'_'+elem.countryCode}}">
                                    <h3>{{elem.name}}</h3>
                                    <h4>{{elem.countryName}}</h4>
                                </a>
                            </div>
                        </div>

                    </div>
                </section><!-- end holiday-weather-section -->

                <section id="top-holiday-dest">
                        <h4 ng-bind="pageContent.topHolidayDestinations"></h4>
                        <ul>
                            <li ng-repeat="elem in top_holidays_destinations track by $index">
                                <a class = "destination{{$index}}" ng-click="select_holiday_destination($index)">{{elem}}<span>/</span></a></li>

                        </ul>
                </section><!-- end top-holiday-dest -->
			</div><!-- end page-content -->
			<aside class="sidebar">
				<!-- <div class="ad300-600">300x600</div> -->
				<img src="https://placehold.it/300x600" alt="alt" class="mob-hide">
			</aside>
		</div><!-- end wrapper -->
	</div><!-- end container -->


	<div class="container section-margin-top">
		<div class="wrapper">
			<div class="page-content">

			</div><!-- end page-content -->

		</div>
	</div><!-- end container -->


</div>
