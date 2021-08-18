package com.oplao.Controller;

import com.oplao.service.SearchService;
import com.oplao.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
public class TodayController {

    @Autowired
    WeatherService weatherService;
    @Autowired
    SearchService searchService;

    @RequestMapping("/get_detailed_forecast_today")
    @ResponseBody
    public List<HashMap> getDetailedForecastMapping(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue, HttpServletRequest request, HttpServletResponse response){
        return weatherService.getDetailedForecastForToday(searchService.findSelectedCity(request, response, currentCookieValue));
    }

    @RequestMapping("/get_detailed_forecast_for_past")
    @ResponseBody
    public List<HashMap> getDetailedForecastForPast(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue, HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "date", required = false) String date,@RequestParam("pastWeather") boolean pastWeather){
        return weatherService.getDetailedForecastForPast(searchService.findSelectedCity(request, response, currentCookieValue), pastWeather, date);
    }
    @RequestMapping("/get_dynamic_table_data")
    @ResponseBody
    public List getDynamicTableData(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                             HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "date", required = false) String date,@RequestParam("numOfHours") int numOfHours,
                                             @RequestParam("numOfDays") int numOfDays, @RequestParam("pastWeather") boolean pastWeather, @CookieValue(value = "langCookieCode", defaultValue = "") String langCode, @RequestParam(value = "forTomorrow", required = false) boolean forTomorrow){

        return weatherService.getDynamicTableData(searchService.findSelectedCity(request, response, currentCookieValue), numOfHours, numOfDays, pastWeather, date, langCode, forTomorrow);
    }
}
