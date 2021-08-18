package com.oplao.Controller;


import com.oplao.service.SearchService;
import com.oplao.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherForDaysController {


    @Autowired
    WeatherService weatherService;
    @Autowired
    SearchService searchService;


    @RequestMapping("/get_table_data_for_days")
    @ResponseBody
    public List getDynamicTableData(@CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                    HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "date", required = false) String date, @RequestParam("numOfHours") int numOfHours,
                                    @RequestParam("numOfDays") int numOfDays, @RequestParam("pastWeather") boolean pastWeather,
                                    @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){

        return weatherService.getTableDataForDays(searchService.findSelectedCity(request, response, currentCookieValue), numOfHours, numOfDays, pastWeather, date, langCode);
    }

    @RequestMapping("/get_not_universal_table_data/{langCode}")
    @ResponseBody

    public Map<Integer, Map<String,Map>> getWeeklyWeather(@PathVariable("langCode") String langCode, @RequestParam(value="numOfDays") int numOfDays, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue, HttpServletRequest request, HttpServletResponse response){

        return weatherService.getWeeklyWeatherReport(searchService.findSelectedCity(request, response, currentCookieValue), numOfDays, langCode);
    }

}
