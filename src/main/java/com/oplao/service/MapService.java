package com.oplao.service;


import com.oplao.Utils.DateConstants;
import com.oplao.Utils.LanguageUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.oplao.service.WeatherService.EXT_STATES;

@Service
public class MapService {

    public List<HashMap> create4DaysTabs(JSONObject city, String langCode){
        List<HashMap> res = new ArrayList<>();

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
        String now = dateTime.getDayOfMonth()+"_"+dateTime.getMonthOfYear()+"_"+dateTime.getYear() +"_now";
        HashMap<String, String> nowMap = new HashMap<>();
        nowMap.put("time", now);
        nowMap.put("name", LanguageService.encode(bundle.getString("now")));
        String today = dateTime.getDayOfMonth()+"_"+dateTime.getMonthOfYear()+"_"+dateTime.getYear();
        HashMap<String, String> todayMap = new HashMap<>();
        todayMap.put("time", today);
        todayMap.put("name", LanguageService.encode(bundle.getString("today")));
        dateTime = dateTime.plusDays(1);
        String tomorrow = dateTime.getDayOfMonth()+"_"+dateTime.getMonthOfYear()+"_"+dateTime.getYear();
        HashMap<String, String> tomorrowMap = new HashMap<>();
        tomorrowMap.put("time", tomorrow);
        tomorrowMap.put("name", LanguageService.encode(bundle.getString("tomorrow")));
        dateTime = dateTime.plusDays(1);
        String aftTomorrow = dateTime.getDayOfMonth()+"_"+dateTime.getMonthOfYear()+"_"+dateTime.getYear();
        HashMap<String, String> aftTomorrowMap = new HashMap<>();
        aftTomorrowMap.put("time", aftTomorrow);
        aftTomorrowMap.put("name", dateTime.getDayOfMonth() +" " + LanguageService.encode(DateConstants.convertMonthOfYear(dateTime.getMonthOfYear(), bundle)).substring(0,3).toUpperCase() +
                " " + LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)).substring(0,3).toUpperCase());
        res.add(nowMap);
        res.add(todayMap);
        res.add(tomorrowMap);
        res.add(aftTomorrowMap);

        return res;
    }

    public List<HashMap> getMapWeather(int max, double north, double west, double south, double east, String time, JSONObject city, String lang){

        List<JSONObject> list= new ArrayList<>();
        DateTime dateTime = null;
        boolean timeCondition = time == null || time.contains("now");
        if(timeCondition){
            dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        }else {
            try {
                dateTime = new DateTime(new SimpleDateFormat("dd_MM_yyyy").parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            list = SearchService.findByOccurences("https://bd.oplao.com/geoLocation/find.json?lang="+lang +
                    "&max="+max+"&north="+north+"&west="+west+"&south="+south+"&east="+east+"&minppl=10000");
        } catch (IOException e) {
            e.printStackTrace();
        }
List<HashMap> res = new ArrayList<>();
        for (JSONObject elem : list) {
            if (timeCondition) {
                res.add(findCurrentConditionForMap(dateTime, elem));
            }else{
                res.add(find2PmWeatherForMap(dateTime, elem));
            }
        }

        return res;
    }


    private HashMap findCurrentConditionForMap(DateTime dateTime, JSONObject elem){
        APIWeatherFinder weatherFinder = new APIWeatherFinder(dateTime, "", false, true, 6, elem.getString("lat"), elem.getString("lng"));
        HashMap weather = ((HashMap) ((ArrayList) weatherFinder.findWeatherByDate().get("current_condition")).get(0));
        HashMap<String, Object> data = new HashMap<>();
        data.put("temp_C", Integer.parseInt(""+weather.get("temp_C")));
        data.put("temp_F", Integer.parseInt("" + weather.get("temp_F")));
        data.put("day", dateTime.getHourOfDay()>6 && dateTime.getHourOfDay()<18?"day":"night");
        data.put("img", "" + EXT_STATES.get(Integer.parseInt("" + weather.get("weatherCode"))));
        data.put("lat", "" + elem.getString("lat"));
        data.put("lng", "" + elem.getString("lng"));
        data.put("city", elem.getString("name"));
        return data;
    }

    private HashMap find2PmWeatherForMap(DateTime dateTime, JSONObject elem){
        APIWeatherFinder weatherFinder = new APIWeatherFinder(dateTime, "", false, true, 1, elem.getString("lat"), elem.getString("lng"));
        HashMap weather = ((HashMap)((ArrayList)((HashMap)((ArrayList)weatherFinder.findWeatherByDate().get("weather")).get(0)).get("hourly")).get(14));
        HashMap<String, Object> data = new HashMap<>();
        data.put("temp_C", Integer.parseInt(""+weather.get("tempC")));
        data.put("temp_F", Integer.parseInt(""+weather.get("tempF")));
        data.put("day", "day");
        data.put("img", "" + EXT_STATES.get(Integer.parseInt("" + weather.get("weatherCode"))));
        data.put("lat", "" + elem.getString("lat"));
        data.put("lng", "" + elem.getString("lng"));
        data.put("city", elem.getString("name"));
        return data;
    }
}
