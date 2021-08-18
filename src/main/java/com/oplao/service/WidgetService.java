package com.oplao.service;

import com.oplao.Utils.DateConstants;
import com.oplao.Utils.LanguageUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.oplao.service.WeatherService.EXT_STATES;
import static com.oplao.service.WeatherService.dayTimesHours;

@Service
public class WidgetService {


    public HashMap getInfoWidgets(String city, String lang, String langCode) {


        Locale locale = new Locale(lang, LanguageUtil.getCountryCode(lang));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+lang, locale);
        JSONObject currentCity = null;
        try {
            String url ="https://bd.oplao.com/geoLocation/find.json?lang="+LanguageUtil.validateOldCountryCodes(lang)+"&max=10&geonameId=" + URLEncoder.encode(city, "UTF-8");
            currentCity = SearchService.findByOccurences(url).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String location = currentCity.getString("name")+"_" + currentCity.getString("countryCode");
        String[] tabs = {"Outlook", "Today", "Tomorrow", "Past", "Temperature map", "3 day", "5 day", "7 day", "10 day", "14 day", "Hour by hour"};
        String[] hrefs = {"weather/outlook/"+location, "weather/today/"+location, "weather/tomorrow/"+location,
        "weather/history3/"+location, "weather/map/"+location, "weather/3/"+location, "weather/5/"+location, "weather/7/"+location,
                "weather/10/"+location, "weather/14/"+location, "weather/hour-by-hour3/"+location};

        int rand1  = (int)(Math.random() * tabs.length);
        HashMap<String, String> map1 = new HashMap<>();
        map1.put(tabs[rand1], hrefs[rand1]);
        map1.put("name", tabs[rand1]);
        tabs = removeArrayElement(tabs, tabs[rand1]);
        hrefs = removeArrayElement(hrefs, hrefs[rand1]);
        int rand2  = (int)(Math.random() * tabs.length-1);
        HashMap<String, String> map2 = new HashMap<>();
        map2.put(tabs[rand2], hrefs[rand2]);
        map2.put("name", tabs[rand2]);

        List links = new ArrayList();
        links.add(map1);
        links.add(map2);
        DateTime dateTime = new DateTime(DateTimeZone.forID((String) ((JSONObject) currentCity.get("timezone")).get("timeZoneId")));

        APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, "",
                false, true, 1, String.valueOf(currentCity.get("lat")), String.valueOf(currentCity.get("lng")));
        HashMap map = apiWeatherFinder.findWeatherByDate();

        HashMap<String, Object> res = new HashMap<>();
        HashMap currentConditions = ((HashMap) ((ArrayList) map.get("current_condition")).get(0));
        res.put("hours", dateTime.getHourOfDay());
        res.put("time", dateTime.getHourOfDay() + ":" + dateTime.getMinuteOfHour());
        res.put("date", dateTime.getDayOfMonth() + " " + LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)).toUpperCase());
        res.put("day", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)).substring(0,3).toLowerCase());
        res.put("city", currentCity.getString("name"));
        res.put("countryCode", currentCity.getString("countryCode"));
        res.put("feelsLikeC", currentConditions.get("FeelsLikeC"));
        res.put("feelsLikeF", currentConditions.get("FeelsLikeF"));
        res.put("weatherIconCode", EXT_STATES.get(Integer.parseInt("" + currentConditions.get("weatherCode"))));
        res.put("clarity", LanguageService.encode(bundle.getString("weatherState."+EXT_STATES.get(Integer.parseInt("" + currentConditions.get("weatherCode"))).toStringValue())));
        res.put("pressurehPa", currentConditions.get("pressure"));
        res.put("pressureInch", new BigDecimal(Integer.parseInt("" + currentConditions.get("pressure")) * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue());
        res.put("temp_c", currentConditions.get("temp_C"));
        res.put("temp_f", currentConditions.get("temp_F"));
        res.put("windMph", currentConditions.get("windspeedMiles"));
        res.put("windMs", (int) Math.round(Integer.parseInt("" + currentConditions.get("windspeedKmph")) * 0.27777777777778));
        res.put("windDegree", currentConditions.get("winddirDegree"));
        res.put("threeDays", getThreeDaysWeatherForWidgets(dateTime, currentCity, bundle));
        res.put("wholeDay", getWholeDayWeatherForWidgets(dateTime, currentCity, bundle));
        res.put("location", location);
        res.put("urls", links);
        res.put("feelsLikeName", LanguageService.encode(bundle.getString("feelsLike")));
        res.put("windName", LanguageService.encode(bundle.getString("wind")));
        res.put("pressureName", LanguageService.encode(bundle.getString("pressure")));
        return res;
    }

    private List<HashMap> getThreeDaysWeatherForWidgets(DateTime dateTime, JSONObject currentCity, ResourceBundle bundle) {

        List<HashMap> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, "",
                    false, true, 1, String.valueOf(currentCity.get("lat")), String.valueOf(currentCity.get("lng")));
            dateTime = dateTime.plusDays(1);
            HashMap map = apiWeatherFinder.findWeatherByDate();
            HashMap weather2Pm = ((HashMap) ((ArrayList) ((HashMap) ((ArrayList) map.get("weather")).get(0)).get("hourly")).get(14));
            HashMap<String, Object> res = new HashMap<>();
            res.put("date", dateTime.getDayOfMonth() + " " + LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)).toUpperCase());
            res.put("day", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)).substring(0,3).toLowerCase());
            res.put("icon", EXT_STATES.get(Integer.parseInt("" + weather2Pm.get("weatherCode"))));
            res.put("clarity", splitCamelCase(EXT_STATES.get(Integer.parseInt("" + weather2Pm.get("weatherCode"))).toStringValue()));
            res.put("temp_c", weather2Pm.get("tempC"));
            res.put("temp_f", weather2Pm.get("tempF"));
            result.add(res);
        }
        return result;

    }

    private List<HashMap> getWholeDayWeatherForWidgets(DateTime dateTime, JSONObject currentCity, ResourceBundle bundle) {
        List<HashMap> result = new ArrayList<>();
        APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, "",
                false, true, 1, String.valueOf(currentCity.get("lat")), String.valueOf(currentCity.get("lng")));
        dateTime = dateTime.plusDays(1);
        HashMap map = apiWeatherFinder.findWeatherByDate();
        String dayTimes[] = DateConstants.getDayTimes(bundle);
        List hourly = ((ArrayList) ((HashMap) ((ArrayList) map.get("weather")).get(0)).get("hourly"));
        for (int dayTime = 0; dayTime < dayTimes.length; dayTime++) {
            HashMap<String, Object> dayMap = new HashMap<>();
            HashMap elem = (HashMap) hourly.get(dayTimesHours[dayTime]);

            dayMap.put("name", dayTimes[dayTime]);

            dayMap.put("date", dateTime.getDayOfMonth() + " " + LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)).toUpperCase());
            dayMap.put("day", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)).substring(0,3).toLowerCase());
            dayMap.put("icon", EXT_STATES.get(Integer.parseInt("" + elem.get("weatherCode"))));
            dayMap.put("clarity", splitCamelCase(EXT_STATES.get(Integer.parseInt("" + elem.get("weatherCode"))).toStringValue()));
            dayMap.put("temp_c", elem.get("tempC"));
            dayMap.put("temp_f", elem.get("tempF"));
            result.add(dayMap);
        }
        return result;

    }

    private String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    private String[] removeArrayElement(String[] arr, String elem){

        List<String> list = new ArrayList<String>(Arrays.asList(arr));
        list.removeAll(Arrays.asList(elem));
        return list.toArray(arr);
    }
}
