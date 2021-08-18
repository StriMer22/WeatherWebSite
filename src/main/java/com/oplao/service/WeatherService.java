package com.oplao.service;

import com.oplao.Application;
import com.oplao.Utils.*;
import com.oplao.model.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    public static final Map<Integer, WeatherStateExt> EXT_STATES = new HashMap<>();
    public static int dayTimesHours[]= {2, 8, 14, 20};
    private static final double DURATION = 29.53059;

    static {
        EXT_STATES.put(113, WeatherStateExt.Clear);
        EXT_STATES.put(116, WeatherStateExt.PartlyCloudy);
        EXT_STATES.put(119, WeatherStateExt.Cloudy);
        EXT_STATES.put(122, WeatherStateExt.Overcast);
        EXT_STATES.put(143, WeatherStateExt.Mist);
        EXT_STATES.put(176, WeatherStateExt.ModeratePatchyRain);
        EXT_STATES.put(179, WeatherStateExt.ModeratePatchySnow);
        EXT_STATES.put(182, WeatherStateExt.PatchySleet);
        EXT_STATES.put(185, WeatherStateExt.PatchySleet);
        EXT_STATES.put(200, WeatherStateExt.PatchyStorm);
        EXT_STATES.put(227, WeatherStateExt.Blizzard);
        EXT_STATES.put(230, WeatherStateExt.Blizzard);
        EXT_STATES.put(248, WeatherStateExt.Fog);
        EXT_STATES.put(260, WeatherStateExt.FreezingFog);
        EXT_STATES.put(263, WeatherStateExt.LightPatchyRain);
        EXT_STATES.put(266, WeatherStateExt.LightRain);
        EXT_STATES.put(281, WeatherStateExt.LightSleet);
        EXT_STATES.put(284, WeatherStateExt.HeavySleet);
        EXT_STATES.put(293, WeatherStateExt.LightPatchyRain);
        EXT_STATES.put(296, WeatherStateExt.LightRain);
        EXT_STATES.put(299, WeatherStateExt.ModeratePatchyRain);
        EXT_STATES.put(302, WeatherStateExt.ModerateRain);
        EXT_STATES.put(305, WeatherStateExt.HeavyPatchyRain);
        EXT_STATES.put(308, WeatherStateExt.HeavyRain);
        EXT_STATES.put(311, WeatherStateExt.FreezingRain);
        EXT_STATES.put(314, WeatherStateExt.HeavyFreezingRain);
        EXT_STATES.put(317, WeatherStateExt.LightSleet);
        EXT_STATES.put(320, WeatherStateExt.HeavySleet);
        EXT_STATES.put(323, WeatherStateExt.LightPatchySnow);
        EXT_STATES.put(326, WeatherStateExt.LightSnow);
        EXT_STATES.put(329, WeatherStateExt.ModeratePatchySnow);
        EXT_STATES.put(332, WeatherStateExt.ModerateSnow);
        EXT_STATES.put(335, WeatherStateExt.HeavyPatchySnow);
        EXT_STATES.put(338, WeatherStateExt.HeavySnow);
        EXT_STATES.put(350, WeatherStateExt.IcePellets);
        EXT_STATES.put(353, WeatherStateExt.LightRain);
        EXT_STATES.put(356, WeatherStateExt.RainShower);
        EXT_STATES.put(359, WeatherStateExt.RainShower);
        EXT_STATES.put(362, WeatherStateExt.PatchySleet);
        EXT_STATES.put(365, WeatherStateExt.PatchySleet);
        EXT_STATES.put(368, WeatherStateExt.LightPatchySnow);
        EXT_STATES.put(371, WeatherStateExt.ModeratePatchySnow);
        EXT_STATES.put(374, WeatherStateExt.IcePelletsShowers);
        EXT_STATES.put(377, WeatherStateExt.HeavyIcePelletsShowers);
        EXT_STATES.put(386, WeatherStateExt.PatchyStorm);
        EXT_STATES.put(389, WeatherStateExt.HeavyStorm);
        EXT_STATES.put(392, WeatherStateExt.LightSnowStorm);
        EXT_STATES.put(395, WeatherStateExt.HeavySnowStorm);
    }

    public static String getWindSpeedColor(int speed) {
        if (speed >= 54) {
            return "#660000";
        } else if (speed >= 35) {
            return "#970000";
        } else if (speed >= 17) {
            return "#DBA000";
        } else {
            return "";
        }
    }

    public static String getPrecipColor(double precip){
        String res = "";

        if (precip >= 8) {
            res = res.concat("#660000");
        }
            if (precip > 16) {
               res = res.concat("; font-weight:bold;");
            }

            return res;
        }


    public List<HashMap> getYearSummary(JSONObject city, String langCode){

        JSONObject jsonObject = null;

        try{
            jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))) + "&cc=no&fx=yes&num_of_days=1&tp=24&showlocaltime=no");
        }catch (IOException e){
            e.printStackTrace();
        }

        HashMap map = (HashMap) jsonObject.toMap().get("data");
        List months = ((ArrayList)((HashMap)((ArrayList)map.get("ClimateAverages")).get(0)).get("month"));
        List<HashMap> result = new ArrayList<>();
        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        for (int i = 0; i < months.size(); i++) {
            HashMap res = new HashMap();
            HashMap elem = ((HashMap)months.get(i));
            res.put("active", dateTime.getMonthOfYear() - 1 == i);
            res.put("month", "" + String.valueOf(elem.get("name")).substring(0,3));
            res.put("fullMonthName", String.valueOf(elem.get("name")));
            res.put("maxtempC", elem.get("avgMaxTemp"));
            res.put("maxtempF", elem.get("avgMaxTemp_F"));
            res.put("mintempC", elem.get("avgMinTemp"));
            res.put("mintempF", elem.get("avgMinTemp_F"));
            res.put("precipMM", elem.get("avgMonthlyRainfall"));
            res.put("precipInch", elem.get("avgMonthlyRainfall_inch"));
            result.add(res);
        }

        return result;

    }
    List<Integer> dayTimeValues = Arrays.asList(1200, 1300, 1400, 1500, 1600, 1700, 1800);
    List<Integer> nightTimeValues = Arrays.asList(0,100,200,300,400,500,600);
    List<String> enCountries = Arrays.asList("en", "fr", "it", "de", "us");


    public HashMap<Integer, Map<String,Map>> getWeeklyWeatherReport(JSONObject city, int numOfDays, String countryCode){

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));

        Locale locale = new Locale(countryCode, LanguageUtil.getCountryCode(countryCode));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages_"+countryCode, locale);
       HashMap<Integer, Map<String,Map>> result = new HashMap<>();

        for (int day = dateTime.getDayOfMonth(), count = 0; day < dateTime.getDayOfMonth()+numOfDays ; day++, count++) {
            result.put(count, getOneDayReportMapping(dateTime.plusDays(
                    count),
                    resourceBundle, countryCode, city.getString("lat"), city.getString("lng")));
        }


        return result;
    }

    private Map<String, Map> getOneDayReportMapping(DateTime dateTime, ResourceBundle bundle, String countryCode, String lat, String lng){
        APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, "",
                false, true, 1, lat, lng);
        HashMap map = apiWeatherFinder.findWeatherByDate();
        apiWeatherFinder = new APIWeatherFinder(dateTime.plusDays(1), "",
                false, true, 1, lat, lng);
        HashMap tomorrowMap = apiWeatherFinder.findWeatherByDate();



        ArrayList<HashMap> weather = (ArrayList<HashMap>)map.get("weather");
        HashMap weatherData = weather.get(0);
        ArrayList<HashMap> hourly = (ArrayList<HashMap>)weatherData.get("hourly");


        ArrayList<HashMap> weatherTomorrow = (ArrayList<HashMap>)tomorrowMap.get("weather");
        HashMap weatherDataTomorrow = weatherTomorrow.get(0);
        ArrayList<HashMap> hourlyTomorrow = (ArrayList<HashMap>)weatherDataTomorrow.get("hourly");
        //boolean countrySpecific = enCountries.contains(countryCode);
        int avgNightC = getAVGNightIntParam(hourly, hourlyTomorrow, "tempC");
        int avgNightF = getAVGNightIntParam(hourly, hourlyTomorrow,"tempF");
        int maxFeelLikeNightC = getAVGNightIntParam(hourly, hourlyTomorrow,"FeelsLikeC");
        int maxFeelLikeNightF = getAVGNightIntParam(hourly, hourlyTomorrow,"FeelsLikeF");
        int precipeChanceNight = getAVGNightIntParam(hourly, hourlyTomorrow,"chanceofrain");
        double precipNightMM = new BigDecimal(getNightSumDoubleParam(hourly, hourlyTomorrow,"precipMM")).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        int avgWindMNight = getAVGNightIntParam(hourly, hourlyTomorrow,"windspeedMiles");
        int avgWindKmhNight = getAVGNightIntParam(hourly, hourlyTomorrow,"windspeedKmph");
        int maxGustMNight = getAVGNightIntParam(hourly, hourlyTomorrow, "WindGustMiles");
        int maxGustKmhNight = getAVGNightIntParam(hourly, hourlyTomorrow, "WindGustKmph");
        int avgPressureNight = getAVGNightIntParam(hourly, hourlyTomorrow,"pressure");
        double avgPressureInchNight = new BigDecimal(avgPressureNight * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue();  //convert pressure from PA to inches
        int windDegreeNight = getAVGNightIntParam(hourly, hourlyTomorrow,"winddirDegree");
        String nightWeatherCode = "" + EXT_STATES.get(parseInt(hourly.get(2).get("weatherCode")));
        int avgWindMsNight = (int)Math.round(avgWindKmhNight * 0.27777777777778);
        int maxGustMsNight = (int)Math.round(maxGustKmhNight * 0.27777777777778);
        double precipNightIn = new BigDecimal(precipNightMM * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        String winddirNight = "" + hourly.get(2).get("winddir16Point");
        String windSpeedColorNight = getWindSpeedColor(avgWindKmhNight);
        String boldNightSpeed = !windSpeedColorNight.equals("") ?"bold":"";
        String windGustColorNight = getWindSpeedColor(maxGustKmhNight);
        String boldNightGust = !windGustColorNight.equals("") ?"bold":"";

        int maxWholeDayC = parseInt(((HashMap)((ArrayList)map.get("weather")).get(0)).get("maxtempC"));
        int maxWholeDayF = parseInt(((HashMap)((ArrayList)map.get("weather")).get(0)).get("maxtempF"));
        int minWholeDayC =  parseInt(((HashMap)((ArrayList)map.get("weather")).get(0)).get("mintempC"));
        int minWholeDayF =  parseInt(((HashMap)((ArrayList)map.get("weather")).get(0)).get("mintempF"));
        int avgDayC = getAVGIntParam(hourly, "tempC", dayTimeValues);
        int avgDayF = getAVGIntParam(hourly, "tempF", dayTimeValues);
        int maxFeelLikeDayC = getAVGIntParam(hourly,"FeelsLikeC", dayTimeValues);
        int maxFeelLikeDayF = getAVGIntParam(hourly,"FeelsLikeF", dayTimeValues);
        int precipeChanceDay = getAVGIntParam(hourly, "chanceofrain",dayTimeValues);
        double precipDayMM  = new BigDecimal(getSumDoubleParam(hourly,"precipMM", dayTimeValues)).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        int avgWindMDay = getAVGIntParam(hourly,"windspeedMiles", dayTimeValues);
        int avgWindKmhDay = getAVGIntParam(hourly,"windspeedKmph", dayTimeValues);
        int maxGustMDay = getAVGIntParam(hourly, "WindGustMiles", dayTimeValues);
        int maxGustKmhDay = getAVGIntParam(hourly,"WindGustKmph", dayTimeValues);
        int avgPressureDay = getAVGIntParam(hourly,"pressure", dayTimeValues);
        String weatherCode = "" + EXT_STATES.get(parseInt(hourly.get(12).get("weatherCode")));
        String dayWeatherCode = "" + EXT_STATES.get(parseInt(hourly.get(14).get("weatherCode")));
        int windDegreeDay = getAVGIntParam(hourly, "winddirDegree", dayTimeValues) + 40 + 180;
        double avgPressureInchDay = new BigDecimal(avgPressureDay * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue();  //convert pressure from PA to inches
        int avgWindMsDay = (int)Math.round(avgWindKmhDay*0.27777777777778);
        int maxGustMsDay = (int)Math.round(maxGustKmhDay * 0.27777777777778);
        double precipDayIn = new BigDecimal(precipDayMM * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        String winddirDay = "" + hourly.get(14).get("winddir16Point");
        String windSpeedColorDay = getWindSpeedColor(avgWindKmhDay);
        String boldDaySpeed = !windSpeedColorDay.equals("") ?"bold":"";
        String windGustColorDay = getWindSpeedColor(maxGustKmhDay);
        String boldDayGust = !windGustColorDay.equals("") ?"bold":"";
        Map<String, Map> result = null;
        Map<String, Object> dayMap = null;
        Map<String, Object> nightMap = null;

        Map<String, Object> wholeDayMap = null;
//        if(enCountries.contains(countryCode)){
            result = new TreeMap<>();
            dayMap = new TreeMap<>();
            nightMap = new TreeMap<>();
            wholeDayMap = new TreeMap<>();
//        }else{
//            result = new HashMap<>();
//            dayMap = new HashMap<>();
//            nightMap = new HashMap<>();
//            wholeDayMap = new HashMap<>();
//        }
        wholeDayMap.put("dayOfMonth", dateTime.getDayOfMonth());
        wholeDayMap.put("monthOfYear", LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)));
        wholeDayMap.put("dayOfWeek", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)));
        wholeDayMap.put("weatherCode", weatherCode);
        wholeDayMap.put("maxTemperatureC", maxWholeDayC);
        wholeDayMap.put("maxTemperatureF", maxWholeDayF);
        wholeDayMap.put("minTemperatureC", minWholeDayC);
        wholeDayMap.put("minTemperatureF", minWholeDayF);

        dayMap.put("avgTempC", avgDayC);
        dayMap.put("avgTempF", avgDayF);
        dayMap.put("time", LanguageService.encode(bundle.getString("day")));
        dayMap.put("feelsLikeC", maxFeelLikeDayC);
        dayMap.put("feelsLikeF", maxFeelLikeDayF);
        dayMap.put("precipChance", precipeChanceDay);
        dayMap.put("precipMm", precipDayMM);
        dayMap.put("precipIn", precipDayIn);
        dayMap.put("windMph", avgWindMDay);
        dayMap.put("windMs", avgWindMsDay);
        dayMap.put("gustMph", maxGustMDay);
        dayMap.put("gustMs", maxGustMsDay);
        dayMap.put("pressurehPa", avgPressureDay);
        dayMap.put("pressureInch", avgPressureInchDay);
        dayMap.put("windDegree", windDegreeDay);
        dayMap.put("weatherCode", dayWeatherCode);
        dayMap.put("winddir", winddirDay);
        dayMap.put("windspeedColor", windSpeedColorDay);
        dayMap.put("windgustColor", windGustColorDay);
        dayMap.put("boldSpeed", boldDaySpeed);
        dayMap.put("boldGust", boldDayGust);

        nightMap.put("time", LanguageService.encode(bundle.getString("night")));
        nightMap.put("avgTempC", avgNightC);
        nightMap.put("avgTempF", avgNightF);
        nightMap.put("feelsLikeC", maxFeelLikeNightC);
        nightMap.put("feelsLikeF", maxFeelLikeNightF);
        nightMap.put("precipChance", precipeChanceNight);
        nightMap.put("precipMm", precipNightMM);
        nightMap.put("precipIn", precipNightIn);
        nightMap.put("windMph", avgWindMNight);
        nightMap.put("windMs", avgWindMsNight);
        nightMap.put("gustMs", maxGustMsNight);
        nightMap.put("gustMph", maxGustMNight);
        nightMap.put("pressurehPa", avgPressureNight);
        nightMap.put("pressureInch", avgPressureInchNight);
        nightMap.put("windDegree", windDegreeNight);
        nightMap.put("weatherCode", nightWeatherCode);
        nightMap.put("winddir", winddirNight);
        nightMap.put("windspeedColor", windSpeedColorNight);
        nightMap.put("windgustColor", windGustColorNight);
        nightMap.put("boldSpeed", boldNightSpeed);
        nightMap.put("boldGust", boldNightGust);
        result.put("wholeDay", wholeDayMap);
        result.put("Day", dayMap);
        result.put("Night", nightMap);

        return result;
    }



    private int calcJulianDate(int d, int m, int y) {

        int mm, yy;
        int k1, k2, k3;
        int j;

        yy = y - (int) ((12 - m) / 10);
        mm = m + 9;
        if (mm >= 12) {
            mm = mm - 12;
        }
        k1 = (int) (365.25 * (yy + 4712));
        k2 = (int) (30.6001 * mm + 0.5);
        k3 = (int) ((int) ((yy / 100) + 49) * 0.75) - 38;
        // 'j' for dates in Julian calendar:
        j = k1 + k2 + d + 59;
        if (j > 2299160) {
            // For Gregorian calendar:
            j = j - k3; // 'j' is the Julian date at 12h UT (Universal Time)
        }
        return j;
    }

    private double calcMoonAge(int d, int m, int y) {

        int j = calcJulianDate(d, m, y);
        // Calculate the approximate phase of the moon
        double ip = (j + 4.867) / DURATION;
        ip = ip - Math.floor(ip);
        // After several trials I've seen to add the following lines,
        // which gave the result was not bad
        double age;
        if (ip < 0.5) {
            age = ip * DURATION + DURATION / 2.0;
        } else {
            age = ip * DURATION - DURATION / 2.0;
        }
        // Moon's age in days
        age = Math.floor(age) + 1.0;
        return age / DURATION;
    }

    private double calcMoonAge(DateTime dateTime) {

        return calcMoonAge(dateTime.getDayOfMonth(), dateTime.getMonthOfYear(), dateTime.getYear());
    }

    private HashMap getMoonData(ResourceBundle bundle) {

        HashMap map = new HashMap();
        DateTime dateTime = new DateTime();
        double age = calcMoonAge(dateTime);
        double moonPhaseImgStep = 1.0 / (double) 28; //28 - moon phases number
        int index = 1 + (int)(((age + moonPhaseImgStep) % 1.0) / moonPhaseImgStep);
        if(index == 1){
            index = 8; //8
        }else if(index >=1 && index<=6){
            index = 1;//1
        }else if(index>=7 && index<=9){
            index = 2;//2
        }else if(index>=10 && index<=14){
            index = 3;//3
        }else if(index==15){
            index = 4; //4
        }else if(index >=16 && index<=20){
            index = 5; //5
        }else if(index>=21 && index<=23){
            index=6; //6
        }else if(index>=24&&index<=28){
            index = 7; //7
        }
        map.put("index", index);
        map.put("state", age < 0.5 - moonPhaseImgStep ? LanguageService.encode(bundle.getString("moonState.waxing")) :LanguageService.encode(bundle.getString("moonState.waning")));
        return map;
    }


    private double getSumDoubleParam(List<HashMap> hourly, String paramName, List<Integer> dayTimeValues){

        hourly = hourly.stream().filter(hashMap ->
                dayTimeValues.contains(parseInt(hashMap.get("time"))))
                .collect(Collectors.toList());

        DoubleSummaryStatistics params = hourly.stream().mapToDouble((value) ->
                parseDouble(value.get(paramName))).summaryStatistics();
        return params.getSum();

    }
    private Integer getAVGIntParam(List<HashMap> hourly, String paramName, List<Integer> dayTimeValues){

        hourly = hourly.stream().filter(hashMap ->
                dayTimeValues.contains(parseInt(hashMap.get("time"))))
                .collect(Collectors.toList());


        IntSummaryStatistics params = hourly.stream().mapToInt((value) ->
                parseInt(value.get(paramName))).summaryStatistics();
        return (int)Math.round(params.getAverage());
    }

    private double getNightSumDoubleParam(List<HashMap> hourly, List<HashMap> tomorrowHourly, String paramName){

        List<HashMap> list = new ArrayList<>();
        for (int i = 20; i < 24; i++) {
            list.add(hourly.get(i));
        }
        for (int i = 0; i < 3; i++) {
            list.add(tomorrowHourly.get(i));
        }

        DoubleSummaryStatistics params = hourly.stream().mapToDouble((value) ->
                parseDouble(value.get(paramName))).summaryStatistics();
        return params.getSum();

    }
        private Integer getAVGNightIntParam(List<HashMap> hourly, List<HashMap> tomorrowHourly,  String paramName){
        List<HashMap> list = new ArrayList<>();
            for (int i = 20; i < 24; i++) {
                list.add(hourly.get(i));
            }
            for (int i = 0; i < 3; i++) {
                list.add(tomorrowHourly.get(i));
            }


                IntSummaryStatistics params = list.stream().mapToInt((value) ->
                        parseInt(value.get(paramName))).summaryStatistics();
                return (int)Math.round(params.getAverage());
            }

    private Integer parseInt(Object o){
        return Integer.parseInt(String.valueOf(o));
    }
    private Double parseDouble(Object o){
        return Double.parseDouble(String.valueOf(o));
    }




    private float roundFloat(float num){
        String pattern = "##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return Float.parseFloat(decimalFormat.format(num).replace(',', '.'));
    }
    public HashMap getCoordinates(JSONObject city, String langCode){

        String cityName = validateCityName((String)city.get("name"));
        ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
        LocalDateTime ldt = LocalDateTime.of(zdt.getYear(),
                zdt.getMonth(), zdt.getDayOfMonth(),
                zdt.getHour(), zdt.getMinute());

        String hoursBetween = "" + ChronoUnit.HOURS.between(ldt, ZonedDateTime.now
                (ZoneId.of((String) ((JSONObject)city.get("timezone")).get("timeZoneId"))));

        if(Integer.parseInt(hoursBetween)>0){
            hoursBetween = "+ " + hoursBetween;
        }

        HashMap result = new HashMap();

        result.put("city", cityName.replaceAll("%20", " "));
        result.put("country", city.getString("countryName"));
        result.put("latitude", roundFloat((float) city.getDouble("lat")));
        result.put("longitude", roundFloat((float) city.getDouble("lng")));
        result.put("hours_between", hoursBetween);
        return result;
    }

    private String validateCityName(String cityName){
        if(cityName.contains("'")){
            cityName = cityName.replace("'", "");
        }

        cityName = cityName.replaceAll(" ", "%20");
        return cityName;
    }
    public HashMap getAstronomy(JSONObject city, String langCode){

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        JSONObject jsonObject = null;
        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
        try{
           jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=6&date="+dateTime.getYear()+"-" + dateTime.getMonthOfYear() + "-" +dateTime.getDayOfMonth() + "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
        }catch (IOException e){
            Application.log.warning("Astronomy request error");
            e.printStackTrace();
        }
        HashMap map = (HashMap)jsonObject.toMap().get("data");
        HashMap result = new HashMap<>();


        HashMap dateMap = new HashMap();
        HashMap moonData = getMoonData(bundle);

        dateMap.put("dayOfMonth", dateTime.getDayOfMonth());
        dateMap.put("monthOfYear", LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)));
        dateMap.put("year", dateTime.getYear());
        result.put("date", dateMap);
        result.put("sunrise", ((HashMap)((ArrayList)((HashMap)((ArrayList)map.get("weather")).get(0)).get("astronomy")).get(0)).get("sunrise"));
        result.put("sunset", ((HashMap)((ArrayList)((HashMap)((ArrayList)map.get("weather")).get(0)).get("astronomy")).get(0)).get("sunset"));
        result.put("moon_phase_index", moonData.get("index"));
    //    result.put("moon_phase_name", convertMoonPhaseIndexToName(moonPhaseIndex+1));
        result.put("moon_phase_state", moonData.get("state"));
        return result;
    }

    private int getMoonPhase(JSONObject city){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        return new MoonPhase(calendar).getPhaseIndex()-1;
    }

    private String getMoonState(JSONObject city){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        MoonPhase moonPhase = new MoonPhase(calendar);
        double waningIndex = moonPhase.getPhase();
        return waningIndex<0?"Waning":"Waxing";
    }
        public HashMap getRemoteData(JSONObject city, String langCode){

            String cityName = validateCityName((String)city.get("name"));
            String countryCode = city.getString("countryCode").toLowerCase();
            if(langCode == null || langCode.equals("")){
                langCode = Arrays.asList(SearchService.validCountryCodes).contains(countryCode)?countryCode:"en";
            }
            DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
            List<String> slavCodes = Arrays.asList("ua", "by", "ru");
            JSONObject jsonObject = null;
        try {
          jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=1&date=" + dateTime.getYear() + "-" + dateTime.getMonthOfYear() + "-" + dateTime.getDayOfMonth() + "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
        }catch (IOException e){
            Application.log.warning("Header data request error");
            e.printStackTrace();
        }
            HashMap map = (HashMap)jsonObject.toMap().get("data");
            ArrayList<HashMap> weather = (ArrayList<HashMap>)map.get("weather");
            HashMap weatherData = weather.get(0);
            HashMap currentConditions = ((HashMap)((ArrayList)map.get("current_condition")).get(0));

            Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
            ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
            HashMap<String, Object> result = new HashMap<>();

            String locWeather = LanguageService.encode(bundle.getString("locationWeather"));

        result.put("cityWeather",  MessageFormat.format(locWeather, LanguageUtil.validateSlavCurrentCode(cityName.replaceAll("%20", " "), langCode)));
        result.put("country", city.get("countryName"));
        result.put("countryCode", Arrays.asList(SearchService.validCountryCodes).contains(countryCode)?countryCode:"en");
        result.put("month", LanguageService.encode(DateConstants.convertMonthOfYear(dateTime.getMonthOfYear(), bundle)));
        result.put("day", dateTime.getDayOfMonth());
        result.put("dayOfWeek", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)));
        result.put("hours", dateTime.getHourOfDay());
        result.put("minutes", dateTime.getMinuteOfHour());
        result.put("temp_c", currentConditions.get("temp_C"));
        result.put("temp_f", currentConditions.get("temp_F"));
        result.put("feelsLikeC", currentConditions.get("FeelsLikeC"));
        result.put("feelsLikeF", currentConditions.get("FeelsLikeF"));
        result.put("humidity", currentConditions.get("humidity"));
        result.put("pressurehPa", currentConditions.get("pressure"));
        result.put("pressureInch", new BigDecimal(parseInt(currentConditions.get("pressure")) * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue());
        result.put("windMph",  currentConditions.get("windspeedMiles"));
        result.put("windMs", (int)Math.round(parseInt(currentConditions.get("windspeedKmph"))*0.27777777777778));
        result.put("direction", currentConditions.get("winddir16Point"));
        result.put("windDegree", parseInt(currentConditions.get("winddirDegree"))+180);
        result.put("sunrise", ((HashMap)((ArrayList)weatherData.get("astronomy")).get(0)).get("sunrise"));
        result.put("sunset", ((HashMap)((ArrayList)weatherData.get("astronomy")).get(0)).get("sunset"));
        result.put("weatherIconCode", ""+(EXT_STATES.get(parseInt(currentConditions.get("weatherCode")))));
        result.put("geonameId", city.getInt("geonameId"));
        result.put("langCode", langCode);
        result.put("city", cityName);

        return result;
    }

    public List<HashMap> getDetailedForecastForPast(JSONObject city, boolean past, String date){
        JSONObject jsonObject = null;
        DateTime dateTime = null;
        if(date == null){
            dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        }else{
            dateTime = new DateTime(date);
        }
        try {
            jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/".concat(past?"past-weather":"weather")+".ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=1&date="+dateTime.getYear()+"-" + dateTime.getMonthOfYear() + "-" +dateTime.getDayOfMonth() + "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
        }catch (IOException e){
            e.printStackTrace();
            Application.log.warning("Detailed forecast request error");

        }
        HashMap map = (HashMap)jsonObject.toMap().get("data");
        ArrayList<HashMap> hourly = ((ArrayList)((HashMap)((ArrayList)(map.get("weather"))).get(0)).get("hourly"));

        String month = dateTime.getMonthOfYear() > 9? "" + dateTime.getMonthOfYear():"0" + dateTime.getMonthOfYear();
        String day = dateTime.getDayOfMonth() > 9 ? "" + dateTime.getDayOfMonth():"0" + dateTime.getDayOfMonth();
        String date1 = dateTime.getYear() + "-" +
                month + "-" +
                day;



        return fulfilHourly(hourly, date1);
    }

    private List<HashMap> fulfilHourly(ArrayList<HashMap> hourly, String date){
        List<HashMap> result = new ArrayList<>();
        for (HashMap aHourly : hourly) {
            HashMap m = new HashMap();
            m.put("tempC", aHourly.get("tempC"));
            m.put("tempF", aHourly.get("tempF"));
            m.put("date", date);
            m.put("precipMM", aHourly.get("precipMM"));
            m.put("precipInch", new BigDecimal(parseDouble(aHourly.get("precipMM")) * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue());
            m.put("weatherIcon",  "" + EXT_STATES.get(parseInt(aHourly.get("weatherCode"))));
            result.add(m);
        }
        return result;
    }
    public List<HashMap> getDetailedForecastForToday(JSONObject city){
        JSONObject jsonObject = null;
        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        try {
            jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=1&date="+dateTime.getYear()+"-" + dateTime.getMonthOfYear() + "-" +dateTime.getDayOfMonth() + "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
        }catch (IOException e){
            e.printStackTrace();
            Application.log.warning("Detailed forecast request error");

        }
        HashMap map = (HashMap)jsonObject.toMap().get("data");
        ArrayList<HashMap> hourly = ((ArrayList)((HashMap)((ArrayList)(map.get("weather"))).get(0)).get("hourly"));

        String month = dateTime.getMonthOfYear() > 9? "" + dateTime.getMonthOfYear():"0" + dateTime.getMonthOfYear();
        String day = dateTime.getDayOfMonth() > 9 ? "" + dateTime.getDayOfMonth():"0" + dateTime.getDayOfMonth();
        String date = dateTime.getYear() + "-" +
                month + "-" +
                day;


        return fulfilHourly(hourly, date);
        }

    public List<DetailedForecastGraphMapping> getDetailedForecastMapping(JSONObject city, String langCode){
            List<DetailedForecastGraphMapping> result = new ArrayList<>();

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        for (int day = 0; day < 14; day++) {
            result.add(getSingleDetailedForecastMapping(dateTime.plusDays(day), city, langCode));
        }
        return result;
    }
    private DetailedForecastGraphMapping getSingleDetailedForecastMapping(DateTime dateTime, JSONObject city, String langCode){
        
        JSONObject jsonObject = null;
        try {
            jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=1&date="+dateTime.getYear()+"-" + dateTime.getMonthOfYear() + "-" +dateTime.getDayOfMonth() + "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
        }catch (IOException e){
            e.printStackTrace();
            Application.log.warning("Detailed forecast request error");
        }
        HashMap map = (HashMap)jsonObject.toMap().get("data");
        ArrayList<HashMap> hourly2PmData = ((ArrayList)((HashMap)((ArrayList)(map.get("weather"))).get(0)).get("hourly"));


        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);

        int tempC = parseInt(hourly2PmData.get(14).get("tempC"));
        int tempF = parseInt(hourly2PmData.get(14).get("tempF"));
        double precipMM = getOneDayTotalRainfall(hourly2PmData).getSum();
        double precipInch = new BigDecimal(precipMM * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        String weatherIconCode = "" + EXT_STATES.get(parseInt(hourly2PmData.get(14).get("weatherCode")));

        String month = dateTime.getMonthOfYear() > 9? "" + dateTime.getMonthOfYear():"0" + dateTime.getMonthOfYear();
        String day = dateTime.getDayOfMonth() > 9 ? "" + dateTime.getDayOfMonth():"0" + dateTime.getDayOfMonth();
        String date = dateTime.getYear() + "-" +
                month + "-" +
                day;

            return new DetailedForecastGraphMapping(tempC,tempF,date, precipMM, precipInch,  weatherIconCode);
    }

    public List<HashMap> getWeeklyUltraviolet(JSONObject city, String langCode){

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        List<HashMap> list = new ArrayList<>();
        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
        for (int day = 0; day < 5; day++) {
            list.add(getSingleUltravioletIndex(dateTime.plusDays(day), city, bundle));
        }
        return list;
    }
    private HashMap getSingleUltravioletIndex(DateTime dateTime, JSONObject city, ResourceBundle bundle){

        JSONObject jsonObject = null;

            try {
                jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=24&date="+dateTime.getYear()+"-" + dateTime.getMonthOfYear() + "-" +dateTime.getDayOfMonth() + "&q="+ String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
            }catch (IOException e){
                e.printStackTrace();
                Application.log.warning("Uv index request error");
            }
        HashMap map = (HashMap)jsonObject.toMap().get("data");

        HashMap res = new HashMap();
        res.put("index",
                ((HashMap)((ArrayList)map.get("weather")).get(0)).get("uvIndex"));
        res.put("date", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(),bundle)).substring(0,3)+" " +
                dateTime.getDayOfMonth()+" "
                + LanguageService.encode(DateConstants.convertMonthOfYearShort(dateTime.getMonthOfYear(), bundle)));
        return res;
    }

    public List getFiveYearsAverage(JSONObject city, String langCode){

        String cityName = validateCityName((String)city.get("name"));

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));

        ArrayList output = new ArrayList();

        for (int i = 0; i < 6; i++) {
            APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime.minusYears(i), cityName,
                     i > 0, false, 1, String.valueOf(city.get("lat")), String.valueOf(city.get("lng")));
            HashMap map = apiWeatherFinder.findWeatherByDate();

            ArrayList param = (ArrayList) MyJsonHelper.getParam(map, "weather", "hourly");

            int tempC = parseInt(((HashMap) param.get(14)).get("tempC"));
            int tempF = parseInt(((HashMap) param.get(14)).get("tempF"));
            String weatherCode = ""+EXT_STATES.get(Integer.parseInt((String)((HashMap) param.get(14)).get("weatherCode")));
            HashMap result = new HashMap();
            result.put("year", dateTime.minusYears(i).getYear());
            result.put("tempC", tempC);
            result.put("tempF", tempF);
            result.put("weatherCode", weatherCode);

            output.add(result);

        }
        return output;
    }

        public static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
    }        public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONArray json = new JSONArray(jsonText);
                return json;
            } finally {
                is.close();
            }
    }

    public HashMap getWeeklyWeatherSummary(JSONObject city, String langCode){

        DateTime dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        DateTime dt1 = null;
        List<HashMap> week = new ArrayList<>();
        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
        for (int i = 0; i < 7; i++) {
            if(i > 0){
                dt1 = dt1.plusDays(i - (i-1));
            }else{
                dt1 = dateTime;
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = readJsonFromUrl("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=gwad8rsbfr57wcbvwghcps26&format=json&show_comments=no&mca=no&cc=yes&tp=1&date="+ dt1.getYear() + "-" + dt1.getMonthOfYear() + "-" + dt1.getDayOfMonth()+ "&q=" + String.valueOf(city.get("lat")+ "," + String.valueOf(city.get("lng"))));
                week.add(((HashMap)(((ArrayList)((JSONObject)jsonObject.get("data")).toMap().get("weather")).get(0))));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        IntSummaryStatistics maxtempCavges = week.stream().mapToInt((value) ->
                parseInt(value.get("maxtempC"))).summaryStatistics();

        IntSummaryStatistics maxtempFavges = week.stream().mapToInt((value) ->
                parseInt(value.get("maxtempF"))).summaryStatistics();

        IntSummaryStatistics mintempFavges = week.stream().mapToInt((value) ->
                parseInt(value.get("mintempF"))).summaryStatistics();

        IntSummaryStatistics mintempCavges = week.stream().mapToInt((value) ->
                parseInt(value.get("mintempC"))).summaryStatistics();

        int maxTempC = maxtempCavges.getMax();
        int avgMaxTempC = (int)maxtempCavges.getAverage();
        int maxTempF = maxtempFavges.getMax();
        int avgMaxTempF = (int)maxtempFavges.getAverage();
        DateTime maxTempDateTime = new DateTime(week.stream().filter(hashMap -> parseInt(hashMap.get("maxtempC"))==maxTempC).findAny().get().get("date"));
        String maxTempDay = LanguageService.encode(DateConstants.convertMonthOfYearShort(maxTempDateTime.getMonthOfYear(), bundle) + "."+maxTempDateTime.getDayOfMonth()).toUpperCase() ;
        int minTempC = mintempCavges.getMin();
        int avgMinTempC = (int)mintempCavges.getAverage();
        int minTempF = mintempFavges.getMin();
        int avgMinTempF = (int)mintempFavges.getAverage();
        DateTime minTempDateTime = new DateTime(week.stream().filter(hashMap -> parseInt(hashMap.get("mintempC"))==minTempC).findAny().get().get("date"));
        String minTempDay = LanguageService.encode(DateConstants.convertMonthOfYearShort(minTempDateTime.getMonthOfYear(), bundle) + "."+minTempDateTime.getDayOfMonth()).toUpperCase();
        double totalRainfallMM = getTotalRainfall(week);
        double totalRainfallInch = new BigDecimal(totalRainfallMM * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue();
        int windiestMiles = getWindiestMiles(week);
        int windiestMS = (int)Math.round(getWindiestKmph(week)*0.27777777777778);
        String foundWindiest = (String)getWindiestDay(week, windiestMiles).get("date");
        DateTime windiestDateTime = new DateTime(foundWindiest);
        String windiestDay = LanguageService.encode(DateConstants.convertMonthOfYearShort(windiestDateTime.getMonthOfYear(), bundle) + "."+windiestDateTime.getDayOfMonth()).toUpperCase();

       HashMap<String, Object> results = new HashMap<>();

       results.put("maxTempC", maxTempC);
       results.put("maxTempF", maxTempF);
       results.put("avgMaxTempC", avgMaxTempC);
       results.put("avgMaxTempF", avgMaxTempF);
       results.put("minTempC", minTempC);
       results.put("minTempF", minTempF);
       results.put("avgMinTempC", avgMinTempC);
       results.put("avgMinTempF", avgMinTempF);
       results.put("totalRainfallMM", totalRainfallMM);
       results.put("totalRainfallInch", totalRainfallInch);
       results.put("windiestMiles", windiestMiles);
       results.put("windiestMS", windiestMS);
       results.put("maxTempDay", maxTempDay);
       results.put("minTempDay", minTempDay);
       results.put("windiestDay", windiestDay);
       return results;
    }

    private double getTotalRainfall(List<HashMap> week){
        double total = 0;
        for (int i = 0; i < week.size(); i++) {
            ArrayList<HashMap> hourly = (ArrayList<HashMap>) week.get(i).get("hourly");
            DoubleSummaryStatistics maxPrecipMMs = getOneDayTotalRainfall(hourly);
            total+=maxPrecipMMs.getSum();

        }
        return total;
    }

    DoubleSummaryStatistics getOneDayTotalRainfall(ArrayList<HashMap> hourly){
        return  hourly.stream().mapToDouble((value) ->
                parseDouble(value.get("precipMM"))).summaryStatistics();
    }

    private HashMap getWindiestDay(List<HashMap> week, int windiestValue){
        for (int i = 0; i < week.size(); i++) {
            List<HashMap> hourlylist = new ArrayList<>();
            for (int j = 0; j < 24; j++) {
             hourlylist.add((HashMap)((ArrayList)week.get(i).get("hourly")).get(j));
            }
            try {
                HashMap elem = hourlylist.stream().filter(hashMap -> hashMap.get("windspeedMiles").toString().equals(String.valueOf(windiestValue))).findAny().get();
                if(week.get(i).get("hourly").equals(hourlylist)){
                    return week.get(i);
                }
            }catch (NoSuchElementException e){
                continue;
            }
        }
        return null;
    }
    private int getWindiestKmph(List<HashMap> week){
        int theWindiestKmph = 0;
        for (int i = 0; i < week.size(); i++) {
            ArrayList<HashMap> hourly = (ArrayList<HashMap>) week.get(i).get("hourly");
            IntSummaryStatistics maxWindiestKmphs = hourly.stream().mapToInt((value) ->
                    parseInt(value.get("windspeedKmph"))).summaryStatistics();
            int windiestInDay = maxWindiestKmphs.getMax();
            if(windiestInDay > theWindiestKmph){
                theWindiestKmph = windiestInDay;
            }
        }
        return theWindiestKmph;
    }

    private int getWindiestMiles(List<HashMap> week){
        int theWindiestMiles = 0;
        for (int i = 0; i < week.size(); i++) {
            ArrayList<HashMap> hourly = (ArrayList<HashMap>) week.get(i).get("hourly");
            IntSummaryStatistics maxWindiestMMs = hourly.stream().mapToInt((value) ->
                    parseInt(value.get("windspeedMiles"))).summaryStatistics();

            int windiestInDay = maxWindiestMMs.getMax();

            if(windiestInDay> theWindiestMiles){
                theWindiestMiles = windiestInDay;
            }

        }
        return theWindiestMiles;
    }

    public List getDynamicTableData(JSONObject city, int numOfHours, int numOfDays, boolean pastWeather, String date, String langCode, boolean forTomorrow){

        String cityName = validateCityName((String)city.get("name"));
        DateTime dateTime = null;
        if(date == null){
           dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        }else{
            dateTime = new DateTime(date);
        }

        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);

        List list = new ArrayList();
        if(forTomorrow) {
            dateTime = dateTime.plusDays(1);
        }
        for (int i = 0; i <numOfDays; i++) {
            APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, cityName,
                    pastWeather, true, numOfHours, String.valueOf(city.get("lat")), String.valueOf(city.get("lng")));

            HashMap data = apiWeatherFinder.findWeatherByDate();
            HashMap weather = ((HashMap)((ArrayList)data.get("weather")).get(0));
            ArrayList hourly = (ArrayList)weather.get("hourly");
            List results = new ArrayList();

            HashMap<String, Object> wholeDayMap = new HashMap<>();

            wholeDayMap.put("dayOfMonth", dateTime.getDayOfMonth());
            wholeDayMap.put("monthOfYear", LanguageService.encode(DateConstants.convertMonthOfYear(dateTime.getMonthOfYear(), bundle)));
            wholeDayMap.put("year", dateTime.getYear());
            wholeDayMap.put("dayOfWeek", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)));
            wholeDayMap.put("todaySign", i==0?LanguageService.encode(bundle.getString("today")):
                    LanguageService.encode(bundle.getString("tomorrow")));
            wholeDayMap.put("maxtempC", weather.get("maxtempC"));
            wholeDayMap.put("mintempC", weather.get("mintempC"));
            wholeDayMap.put("maxtempF", weather.get("maxtempF"));
            wholeDayMap.put("mintempF", weather.get("mintempF"));
            wholeDayMap.put("sunrise", ((HashMap)((ArrayList)weather.get("astronomy")).get(0)).get("sunrise"));
            wholeDayMap.put("sunset", ((HashMap)((ArrayList)weather.get("astronomy")).get(0)).get("sunset"));
            wholeDayMap.put("weatherCode", "" + EXT_STATES.get(parseInt(hourly.size()==8?(((HashMap)hourly.get(5)).get("weatherCode")):(((HashMap)hourly.get(15)).get("weatherCode")))));
            wholeDayMap.put("isDay", dateTime.getHourOfDay()>6 && dateTime.getHourOfDay()<18);

            List<HashMap> oneWholeDayData = new ArrayList<>();

            for (int j = 0; j < hourly.size(); j++) {
                HashMap<String, Object> dayMap = new HashMap<>();
                HashMap elem = (HashMap)hourly.get(j);

                String windSpeedColorDay = getWindSpeedColor(parseInt(elem.get("windspeedKmph")));
                String windGustColorDay = getWindSpeedColor(parseInt(elem.get("WindGustKmph")));
                String windSpeedBoldDay = !windSpeedColorDay.equals("") ?"bold":"";
                String windGustBoldDay = !windGustColorDay.equals("") ?"bold":"";
                dayMap.put("time", DateConstants.convertTimeToAmPm(parseInt(elem.get("time"))));
                dayMap.put("weatherCode", "" + EXT_STATES.get(parseInt(elem.get("weatherCode"))));
                dayMap.put("tempC", elem.get("tempC"));
                dayMap.put("tempF", elem.get("tempF"));
                dayMap.put("feelsLikeC", elem.get("FeelsLikeC"));
                dayMap.put("feelsLikeF", elem.get("FeelsLikeF"));
                dayMap.put("precipChance", pastWeather?"0":elem.get("chanceofrain"));
                dayMap.put("precipMM", elem.get("precipMM"));
                dayMap.put("precipInch", new BigDecimal(parseDouble(elem.get("precipMM")) * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue());
                dayMap.put("windMph", elem.get("windspeedMiles"));
                dayMap.put("windMs", (int)Math.round(parseInt(elem.get("windspeedKmph"))*0.27777777777778));
                dayMap.put("winddir", elem.get("winddir16Point"));
                dayMap.put("windDegree", parseInt(elem.get("winddirDegree")) + 40 + 180);
                dayMap.put("gustMph", elem.get("WindGustMiles"));
                dayMap.put("gustMs", (int)Math.round(parseInt(elem.get("WindGustKmph"))*0.27777777777778));
                dayMap.put("cloudCover", elem.get("cloudcover"));
                dayMap.put("humidity", elem.get("humidity"));
                dayMap.put("pressurehPa", elem.get("pressure"));
                dayMap.put("pressureInch", new BigDecimal(parseInt(elem.get("pressure")) * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue());
                dayMap.put("windspeedColor", windSpeedColorDay);
                dayMap.put("windgustColor", windGustColorDay);
                dayMap.put("isDay", parseInt(elem.get("time")) >= 600 && parseInt(elem.get("time")) < 1800);
                dayMap.put("boldSpeed", windSpeedBoldDay);
                dayMap.put("boldGust", windGustBoldDay);
                dayMap.put("precipStyle", getPrecipColor(parseDouble(elem.get("precipMM"))));
                oneWholeDayData.add(dayMap);
            }
            results.add(oneWholeDayData);
            results.add(wholeDayMap);

            list.add(results);

            dateTime = dateTime.plusDays(1);
        }


        return list;
    }

    public List getTableDataForDays(JSONObject city, int numOfHours, int numOfDays, boolean pastWeather, String date, String langCode){

        String cityName = validateCityName((String)city.get("name"));
        DateTime dateTime = null;
        if(date == null){
            dateTime = new DateTime(DateTimeZone.forID((String)((JSONObject)city.get("timezone")).get("timeZoneId")));
        }else{
            dateTime = new DateTime(date);
        }

        String countryCode = city.getString("countryCode").toLowerCase();
        if(langCode.equals("")){
                langCode = Arrays.asList(SearchService.validCountryCodes).contains(countryCode) ? countryCode : "en";
        }
        Locale locale = new Locale(langCode, LanguageUtil.getCountryCode(langCode));
        ResourceBundle bundle = ResourceBundle.getBundle("messages_"+langCode, locale);
        List list = new ArrayList();
        for (int i = 0; i < numOfDays; i++) {
            if(i > 0){
                dateTime = dateTime.plusDays(i - (i-1));
            }
            APIWeatherFinder apiWeatherFinder = new APIWeatherFinder(dateTime, cityName,
                    pastWeather, true, numOfHours, String.valueOf(city.get("lat")), String.valueOf(city.get("lng")));

            HashMap data = apiWeatherFinder.findWeatherByDate();
            HashMap weather = ((HashMap)((ArrayList)data.get("weather")).get(0));
           // HashMap currentCondition = (HashMap) ((ArrayList)(data).get("current_condition")).get(0);
            ArrayList hourly = (ArrayList)weather.get("hourly");

            List results = new ArrayList();

            HashMap<String, Object> wholeDayMap = new HashMap<>();

            wholeDayMap.put("dayOfMonth", dateTime.getDayOfMonth());
            wholeDayMap.put("monthOfYear", LanguageService.encode(DateConstants.convertMonthOfYear(dateTime.getMonthOfYear(), bundle)).substring(0,3));
            wholeDayMap.put("dayOfWeek", LanguageService.encode(DateConstants.convertDayOfWeek(dateTime.getDayOfWeek(), bundle)).substring(0,3));
            wholeDayMap.put("maxtempC", weather.get("maxtempC"));
            wholeDayMap.put("mintempC", weather.get("mintempC"));
            wholeDayMap.put("maxtempF", weather.get("maxtempF"));
            wholeDayMap.put("mintempF", weather.get("mintempF"));
            wholeDayMap.put("weatherCode", "" + EXT_STATES.get(parseInt(((HashMap)hourly.get(8)).get("weatherCode"))));
            wholeDayMap.put("isDay", dateTime.getHourOfDay()>6 && dateTime.getHourOfDay()<18);

            List<HashMap> oneWholeDayData = new ArrayList<>();
            String dayTimes[] = DateConstants.getDayTimes(bundle);
            for (int dayTime = 0; dayTime < dayTimes.length; dayTime++) {
                HashMap<String, Object> dayMap = new HashMap<>();
                HashMap elem = (HashMap)hourly.get(dayTimesHours[dayTime]);

                String windSpeedColorDay = getWindSpeedColor(parseInt(elem.get("windspeedKmph")));
                String windGustColorDay = getWindSpeedColor(parseInt(elem.get("WindGustKmph")));
                String boldDaySpeed = !windSpeedColorDay.equals("") ?"bold":"";
                String boldDayGust = !windGustColorDay.equals("") ?"bold":"";
                dayMap.put("time", dayTimes[dayTime]);
                dayMap.put("weatherCode", "" + EXT_STATES.get(parseInt(elem.get("weatherCode"))));
                dayMap.put("tempC", elem.get("tempC"));
                dayMap.put("tempF", elem.get("tempF"));
                dayMap.put("feelsLikeC", elem.get("FeelsLikeC"));
                dayMap.put("feelsLikeF", elem.get("FeelsLikeF"));
                dayMap.put("precipChance", elem.get("chanceofrain"));
                dayMap.put("precipMM", elem.get("precipMM"));
                dayMap.put("precipInch", new BigDecimal(parseDouble(elem.get("precipMM")) * 0.0393700787).setScale(2, BigDecimal.ROUND_UP).doubleValue());
                dayMap.put("windMph", elem.get("windspeedMiles"));
                dayMap.put("windMs", (int)Math.round(parseInt(elem.get("windspeedKmph"))*0.27777777777778));
                dayMap.put("winddir", elem.get("winddir16Point"));
                dayMap.put("windDegree", parseInt(elem.get("winddirDegree")) + 40 + 180);
                dayMap.put("gustMph", elem.get("WindGustMiles"));
                dayMap.put("gustMs", (int)Math.round(parseInt(elem.get("WindGustKmph"))*0.27777777777778));
                dayMap.put("pressurehPa", elem.get("pressure"));
                dayMap.put("pressureInch", new BigDecimal(parseInt(elem.get("pressure")) * 0.000296133971008484).setScale(2, BigDecimal.ROUND_UP).doubleValue());
                dayMap.put("isDay", dayTimesHours[dayTime] == 14 || dayTimesHours[dayTime] == 8);
                dayMap.put("windspeedColor", windSpeedColorDay);
                dayMap.put("windgustColor", windGustColorDay);
                dayMap.put("boldSpeed", boldDaySpeed);
                dayMap.put("boldGust", boldDayGust);
                oneWholeDayData.add(dayMap);
            }
            results.add(oneWholeDayData);
            results.add(wholeDayMap);

            list.add(results);
        }


        return list;

    }
}
