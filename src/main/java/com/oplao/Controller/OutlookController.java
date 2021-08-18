package com.oplao.Controller;

import com.oplao.model.*;
import com.oplao.service.SearchService;
import com.oplao.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.oplao.service.SearchService.langCookieCode;

@Controller
public class OutlookController {


    @Autowired
    WeatherService weatherService;
    @Autowired
    SearchService searchService;

    @RequestMapping("/get_coordinates")
    @ResponseBody
    public HashMap getCoordinates(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return weatherService.getCoordinates(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }
    @RequestMapping("/get_api_weather")
    @ResponseBody
    public HashMap getApiWeather(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){

        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String headerValue = CacheControl.maxAge(86400, TimeUnit.SECONDS)
                .getHeaderValue();
        response.addHeader("Cache-Control", headerValue);

        return weatherService.getRemoteData(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("/refresh_cookies")
    @ResponseBody
    public String refreshCookies(@CookieValue(value = SearchService.cookieName, defaultValue = "")String currentCookieValue){
        return currentCookieValue;
    }
    @RequestMapping("/get_astronomy")
    @ResponseBody
    public HashMap getAstronomy(@CookieValue(value = SearchService.cookieName, defaultValue = "")String currentCookieValue,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return weatherService.getAstronomy(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("/get_weekly_weather_summary")
    @ResponseBody
    public HashMap getWeeklySummary(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return weatherService.getWeeklyWeatherSummary(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("/get_detailed_forecast")
    @ResponseBody
    public List<DetailedForecastGraphMapping> getDetailedForecastMapping(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                                                         HttpServletRequest request,
                                                                         HttpServletResponse response,
                                                                         @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return weatherService.getDetailedForecastMapping(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }
    @RequestMapping("/get_weekly_weather/{langCode}")
    @ResponseBody

    public Map<Integer, Map<String,Map>> getWeeklyWeather(@PathVariable("langCode") String langCode,
                                                          @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return weatherService.getWeeklyWeatherReport(searchService.findSelectedCity(request, response, currentCookieValue), 7, langCode);
    }

    @RequestMapping("/get_year_summary")
    @ResponseBody
    public List<HashMap> getYearSummary(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,HttpServletRequest request,
                                        HttpServletResponse response,
                                        @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return weatherService.getYearSummary(searchService.findSelectedCity(request, response, currentCookieValue), langCode);

    }
    @RequestMapping("/get_five_years_average")
    @ResponseBody
    public List getFiveYearsAverage(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return weatherService.getFiveYearsAverage(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("/get_weekly_ultraviolet_index")
    @ResponseBody
    public List getWeeklyUltravioletIndex(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){

        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return weatherService.getWeeklyUltraviolet(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("get_recent_cities_tabs")
    @ResponseBody
    public List<HashMap> getRecentCitiesTabs(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                             @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return searchService.createRecentCitiesTabs(currentCookieValue, langCode);
    }

    @RequestMapping("/deleteCity/{geonameId}")
    @ResponseBody
    public HttpStatus deleteCity(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                 @PathVariable("geonameId") String geonameId,
                                 HttpServletRequest request, HttpServletResponse response){

            Cookie c = searchService.deleteCity(currentCookieValue, geonameId, request, response);

            if(c !=null){
                response.addCookie(c);
            }else {
                return HttpStatus.BAD_REQUEST;
            }

       return HttpStatus.OK;
    }

    @RequestMapping(value = "/get_top_holidays_destinations", produces = "application/json")
    @ResponseBody
    public List<String> getTopHolidaysDestinations(@CookieValue(value = "langCookieCode", defaultValue = "") String languageCookieCode) {

        return searchService.getTopHolidaysDestinations(23, languageCookieCode);


    }

    @RequestMapping("get_country_weather")
    @ResponseBody
    public List<HashMap> getCountryWeather(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           @CookieValue(value = "langCookieCode", defaultValue = "") String langCode) throws IOException {

        return searchService.getCountryWeather(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }
    @RequestMapping(value = "/get_holidays_weather", produces = "application/json")
    @ResponseBody
    public List<HashMap> getHolidaysWeather(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            @CookieValue(value = langCookieCode, defaultValue = "") String langCode) throws IOException {

        return searchService.getHolidaysWeather(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }
    }
