package com.oplao.service;

import com.oplao.Utils.LanguageUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.oplao.service.SearchService.validCountryCodes;

@Service
public class LanguageService {

    public HashMap generateLanguageContent(String languageCode, String path, JSONObject currentCity) {

        if(languageCode == null || languageCode.equals("")){
             languageCode = "en";
            if(Arrays.asList(validCountryCodes).contains(currentCity.getString("country_code").toLowerCase())){
                languageCode = currentCity.getString("country_code").toLowerCase();
            }
        }
        Locale locale = new Locale(languageCode, LanguageUtil.getCountryCode(languageCode));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages_" + languageCode, locale);

        String city = currentCity.getString("name");
        String countryName = currentCity.getString("countryName");
        String[] parsedUrl = path.split("/");
        char hrIndex = 3;
        if(parsedUrl.length >=3) {
            hrIndex = parsedUrl[3].charAt(parsedUrl[3].length() - 1);
        }
        if (path.contains("widgets")) {
            return generateWidgetContent(resourceBundle);
        } else if (path.equals("/") || path.equals("%2F")||path.split("/").length == 4 && !path.contains("widgets")) {
            return generateFrontPageContent(resourceBundle, city, countryName, languageCode);
        } else if (path.contains("outlook")) {
            return generateOutlookContent(resourceBundle, city, countryName, languageCode);
        } else if (path.contains("today") || path.contains("tomorrow")) {
            return generateTodayTomorrowContent(resourceBundle, city, countryName, languageCode, path.contains("today"));
        } else if (path.contains("history")) {
            return generatePastWeatherContent(resourceBundle, city, countryName, languageCode, hrIndex);
        } else if (path.contains("hour-by-hour")) {
            return generateHourlyContent(resourceBundle, city, countryName, languageCode, hrIndex);
        } else if (path.contains("3") || path.contains("7") || path.contains("14")) {
            return generateUniversalDaysContent(resourceBundle, path.contains("3"), path.contains("7"), city, countryName, languageCode);
        } else if (path.contains("5") || path.contains("10")) {
            return generateNotUniversalDaysContent(resourceBundle, path.contains("5"), city, countryName, languageCode);
        } else if (path.contains("map")) {
            return generateTemperatureMapContent(resourceBundle, city, countryName, languageCode);
        }
        return generateFrontPageContent(resourceBundle, city, countryName, languageCode);
    }

    private HashMap<String, String> generateWidgetContent(ResourceBundle bundle) {
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("widgetsHeader", encode(bundle.getString("widgetsHeader")));
        map.put("chooseLocation", encode(bundle.getString("chooseLocation")));
        map.put("chooseWidgetSize", encode(bundle.getString("chooseWidgetSize")));
        map.put("selectLanguage", encode(bundle.getString("selectLanguage")));
        map.put("customizeParameters", encode(bundle.getString("customizeParameters")));
        map.put("addTheseNumbers", encode(bundle.getString("addTheseNumbers")));
        map.put("refreshNumbers", encode(bundle.getString("refreshNumbers")));
        map.put("copyCode", encode(bundle.getString("copyCode")));
        map.put("getTheCode", encode(bundle.getString("getTheCode")));
        map.put("adoptingTheTermsOfUse", encode(bundle.getString("adoptingTheTermsOfUse")));
        map.put("title", encode(bundle.getString("titleWidgets")));
        map.put("description", encode(bundle.getString("descrWidgets")));
        return map;
    }

    private HashMap generateHourlyContent(ResourceBundle bundle, String city, String country, String langCode, char hrIndex) {
        boolean isSlav = LanguageUtil.isSlav(langCode);
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("aboveGraph", encode(bundle.getString("aboveGraphHourly")));
        map.put("aboveTable", encode(bundle.getString("aboveTableHourly")));
        map.put("oneHour", encode(bundle.getString("oneHour")));
        map.put("threeHour", encode(bundle.getString("threeHour")));
        map.put("inGraphTitle", encode(bundle.getString("aboveGraphHourly")));
        map.put("title", formatLocation(bundle.getString("titleHourly"), city, country, langCode));
        map.put("description", !isSlav ? langCode.equals("fr") || langCode.equals("it")
                ? formatThreeLocations(bundle.getString("descrHourly"), city, country, langCode)
                : formatTwoLocations(bundle.getString("descrHourly"), city, country, langCode)
                : formatTwoLocations(bundle.getString("descrHourly" + hrIndex), city, country, langCode));
        map.put("canonical", hrIndex == '3' ? "https://oplao.com/en/forecast/hour-by-hour1/" : "");
        return map;
    }

    private HashMap generatePastWeatherContent(ResourceBundle bundle, String city, String country, String langCode, char hrIndex) {
        boolean isSlav = LanguageUtil.isSlav(langCode);
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("aboveTable", encode(bundle.getString("aboveTablePastWeather")));
        map.put("aboveGraph", encode(bundle.getString("aboveGraphPastWeather")));
        map.put("inGraphTitle", encode(bundle.getString("aboveGraphPastWeather")));
        map.put("pickADate", encode(bundle.getString("pickDate")));
        map.put("oneHour", encode(bundle.getString("oneHour")));
        map.put("threeHour", encode(bundle.getString("threeHour")));
        map.put("title", formatLocation(bundle.getString("titleHistory".concat(isSlav || langCode.equals("de") ? String.valueOf(hrIndex) : "")), city, country, langCode));
        map.put("description", generateHistoryDescription(bundle, city, country, langCode, hrIndex, isSlav));
        map.put("canonical", hrIndex == '3' ? "https://oplao.com/en/weather/history1/" : "");

        return map;
    }

    private HashMap generateTemperatureMapContent(ResourceBundle bundle, String city, String country, String langCode) {
        HashMap map = generateMainContent(bundle);
        map.put("aboveTable", encode(bundle.getString("aboveTableTempMap")));
        map.put("title", encode(bundle.getString("titleMap")));
        map.put("description", formatLocation(bundle.getString("descrMap"), city, country, langCode));
        map.put("canonical", "https://oplao.com/en/weather/outlook/");
        return map;
    }

    private HashMap generateNotUniversalDaysContent(ResourceBundle bundle, boolean is5, String city, String country, String langCode) {
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("aboveTable", encode(is5 ? bundle.getString("aboveTable5Days") : bundle.getString("aboveTable10Days")));
        map.put("aboveGraph", encode(is5 ? bundle.getString("aboveGraph5Days") : bundle.getString("aboveGraph10Days")));
        map.put("inGraphTitle", encode(is5 ? bundle.getString("aboveGraph5Days") : bundle.getString("aboveGraph10Days")));
        map.put("title", formatLocation(is5 ? bundle.getString("title5Days") : bundle.getString("title10Days"), city, country, langCode));
        map.put("description", (!langCode.equals("fr") && !langCode.equals("it")) ?
                formatLocation(is5 ? bundle.getString("descr5Days") :
                        bundle.getString("descr10Days"), city, country, langCode) :
                formatTwoLocations(is5 ? bundle.getString("descr5Days") : bundle.getString("descr10Days"), city, country, langCode));
        map.put("canonical", "https://oplao.com/en/weather/outlook/");
        return map;
    }

    private HashMap generateUniversalDaysContent(ResourceBundle bundle, boolean is3, boolean is7, String city, String country, String langCode) {
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("aboveTable", encode(is3 ? bundle.getString("aboveTable3Days")
                : is7 ? bundle.getString("aboveTable7Days")
                : bundle.getString("aboveTable14Days")));
        map.put("aboveGraph", encode(is3 ? bundle.getString("aboveGraph3Days")
                : is7 ? bundle.getString("aboveGraph7Days")
                : bundle.getString("aboveGraph14Days")));
        map.put("inGraphTitle", encode(is3 ? bundle.getString("aboveGraph3Days")
                : is7 ? bundle.getString("aboveGraph7Days")
                : bundle.getString("aboveGraph14Days")));
        map.put("title", formatLocation(is3 ? bundle.getString("title3Days")
                : is7 ? bundle.getString("title7Days")
                : bundle.getString("title14Days"), city, country, langCode));
        map.put("description", generateUniversalDaysDescription(bundle, city, country, langCode, is3, is7));
        map.put("canonical", is3 || is7 ? "https://oplao.com/en/forecast/10/" : "https://oplao.com/en/weather/outlook/");
        return map;
    }

    private HashMap generateTodayTomorrowContent(ResourceBundle bundle, String city, String country, String langCode, boolean isToday) {
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("sunrise", encode(bundle.getString("sunrise")));
        map.put("sunset", encode(bundle.getString("sunset")));
        map.put("inGraphTitle", encode(bundle.getString("aboveGraphToday")));
        map.put("aboveTable", encode(isToday ? bundle.getString("aboveTableToday") : bundle.getString("aboveTableTomorrow")));
        map.put("aboveGraph", encode(isToday ? bundle.getString("aboveGraphToday") : ""));
        map.put("title", isToday ? formatLocation(bundle.getString("titleToday"), city, country, langCode) : formatLocation(bundle.getString("titleTomorrow"), city, country, langCode));
        map.put("description", isToday ? formatLocation(bundle.getString("descrToday"), city, country, langCode) : formatLocation(bundle.getString("descrTomorrow"), city, country, langCode));
        map.put("canonical", isToday ? "https://oplao.com/en/forecast/hour-by-hour1/" : "https://oplao.com/en/forecast/today/");
        return map;
    }

    private HashMap generateFrontPageContent(ResourceBundle bundle, String city, String country, String langCode) {

        HashMap<String, String> map = generateMainContent(bundle);
        map.put("viewMap", encode(bundle.getString("viewMap")));
        String locWeather = encode(bundle.getString("locationWeather"));
        map.put("locationWeather", MessageFormat.format(locWeather, LanguageUtil.validateSlavCurrentCode(country, langCode)));
        map.put("holidayWeather", encode(bundle.getString("holidayWeather")));
        map.put("topHolidayDestinations", encode(bundle.getString("topHolidayDestinations")));
        map.put("title", langCode.equals("en")?encode(bundle.getString("titleFront")):formatLocation(bundle.getString("titleFront"), city, country, langCode));
        map.put("description", langCode.equals("it") || langCode.equals("en") ? encode(bundle.getString("descrFront")) : formatLocation(bundle.getString("descrFront"), city, country, langCode));
        return map;
    }

    private HashMap<String, String> generateOutlookContent(ResourceBundle bundle, String city, String country, String langCode) {
        boolean isSlav = LanguageUtil.isSlav(langCode);
        HashMap<String, String> map = generateMainContent(bundle);
        map.put("inGraphTitle", isSlav ? encode(bundle.getString("aboveGraph14Days")) : encode(bundle.getString("aboveGraphOutlook")));
        map.put("longTermForecast", encode(bundle.getString("longTermForecast")));
        map.put("date", encode(bundle.getString("date")));
        map.put("aboveGraph", isSlav ? encode(bundle.getString("aboveGraph14Days")) : encode(bundle.getString("aboveGraphOutlook")));
        map.put("aboveTable", isSlav ? encode(bundle.getString("aboveTable14Days")) : encode(bundle.getString("aboveTableOutlook")));
        String climIn = encode(bundle.getString("climateInWeather"));
        map.put("climateIn", MessageFormat.format(climIn, LanguageUtil.validateSlavCurrentCode(city, langCode)));
        map.put("coordinates", encode(bundle.getString("coordinates")));
        map.put("last5YearWeatherData", encode(bundle.getString("last5YearWeatherData")));
        map.put("uvIndex", encode(bundle.getString("uvIndex")));
        map.put("uvIndex1", encode(bundle.getString("uvIndex.1")));
        map.put("uvIndex2", encode(bundle.getString("uvIndex.2")));
        map.put("uvIndex3", encode(bundle.getString("uvIndex.3")));
        map.put("uvIndex4", encode(bundle.getString("uvIndex.4")));
        map.put("uvIndex5", encode(bundle.getString("uvIndex.5")));
        map.put("title", formatLocation(bundle.getString("titleOutlook"), city, country, langCode));
        map.put("description", generateOutlookDescription(bundle, city, country, langCode, isSlav));

        return map;
    }

    private HashMap<String, String> generateMainContent(ResourceBundle bundle) {
        HashMap<String, String> map = new HashMap<>();
        map.put("searchTip", encode(bundle.getString("searchTip")));
        map.put("feelsLike", encode(bundle.getString("feelsLike")));
        map.put("humidity", encode(bundle.getString("humidity")));
        map.put("pressure", encode(bundle.getString("pressure")));
        map.put("wind", encode(bundle.getString("wind")));
        map.put("time", encode(bundle.getString("time")));
        map.put("precipChance", encode(bundle.getString("precipChance")));
        map.put("precip", encode(bundle.getString("precip")));
        map.put("gust", encode(bundle.getString("gust")));
        map.put("cloud", encode(bundle.getString("cloud")));
        map.put("temperature", encode(bundle.getString("temperature")));
        map.put("fullForecast", encode(bundle.getString("fullForecast")));
        map.put("androidApps", encode(bundle.getString("androidApps")));
        map.put("googleChromeWeatherExtension", encode(bundle.getString("googleChromeWeatherExtension")));
        map.put("firefoxWeatherExtension", encode(bundle.getString("firefoxWeatherExtension")));
        map.put("operaWeatherExtension", encode(bundle.getString("operaWeatherExtension")));
        map.put("about", encode(bundle.getString("about")));
        map.put("information", encode(bundle.getString("information")));
        map.put("blog", encode(bundle.getString("blog")));
        map.put("feedback", encode(bundle.getString("feedback")));
        map.put("oplaoWeatherExtension", encode(bundle.getString("oplaoWeatherExtension")));
        map.put("threeDaysWeatherForecast", encode(bundle.getString("3DaysWeatherForecast")));
        map.put("fiveDaysWeatherForecast", encode(bundle.getString("5DaysWeatherForecast")));
        map.put("sevenDaysWeatherForecast", encode(bundle.getString("7DaysWeatherForecast")));
        map.put("fourteenDaysWeatherForecast", encode(bundle.getString("14DaysWeatherForecast")));
        map.put("hourlyWeather", encode(bundle.getString("hourlyWeather")));
        map.put("applications", encode(bundle.getString("applications")));
        map.put("weather", encode(bundle.getString("weather")));
        map.put("weatherMap", encode(bundle.getString("weatherMap")));
        map.put("weatherHistory", encode(bundle.getString("weatherHistory")));
        map.put("widgets", encode(bundle.getString("widgets")));
        map.put("alerts", encode(bundle.getString("alerts")));
        map.put("outlook", encode(bundle.getString("outlook")));
        map.put("today", encode(bundle.getString("today")));
        map.put("tomorrow", encode(bundle.getString("tomorrow")));
        map.put("pastWeather", encode(bundle.getString("pastWeather")));
        map.put("temperatureMap", encode(bundle.getString("temperatureMap")));
        map.put("threeDay", encode(bundle.getString("3day")));
        map.put("fiveDay", encode(bundle.getString("5day")));
        map.put("sevenDay", encode(bundle.getString("7day")));
        map.put("tenDay", encode(bundle.getString("10day")));
        map.put("fourteenDay", encode(bundle.getString("14day")));
        map.put("hourByHour", encode(bundle.getString("hourByHour")));
        map.put("weeklyWeatherSummary", encode(bundle.getString("weeklyWeatherSummary")));
        map.put("maxTemperature", encode(bundle.getString("maxTemperature")));
        map.put("minTemperature", encode(bundle.getString("minTemperature")));
        map.put("next7Days", encode(bundle.getString("next7Days")));
        map.put("averageMaxTemperature", encode(bundle.getString("averageMaxTemperature")));
        map.put("averageMinTemperature", encode(bundle.getString("averageMinTemperature")));
        map.put("totalRainfall", encode(bundle.getString("totalRainfall")));
        map.put("windiest", encode(bundle.getString("windiest")));
        map.put("astronomy", encode(bundle.getString("astronomy")));
        map.put("onMap", encode(bundle.getString("onMap")));
        map.put("checkPastWeather", encode(bundle.getString("checkPastWeather")));
        map.put("min", encode(bundle.getString("min")));
        map.put("max", encode(bundle.getString("max")));
        map.put("precipitation", encode(bundle.getString("precipitation")));
        map.put("timezone", encode(bundle.getString("timezone")));
        map.put("visibility", encode(bundle.getString("visibility")));
        map.put("ms", encode(bundle.getString("speedScale.m/s")));
        map.put("mph", encode(bundle.getString("speedScale.mph")));
        map.put("hPa", encode(bundle.getString("pressureScale.hPa")));
        map.put("in", encode(bundle.getString("pressureScale.in")));
        map.put("mmDist", encode(bundle.getString("smallWidthScale.mm")));
        map.put("inDist", encode(bundle.getString("smallWidthScale.in")));
        map.put("settings", encode(bundle.getString("settings")));
        map.put("language", encode(bundle.getString("language")));
        map.put("unitsPreference", encode(bundle.getString("unitsPreference")));
        map.put("timeFormat", encode(bundle.getString("timeFormat")));
        map.put("twelveHours", encode(bundle.getString("timeFormat.1")));
        map.put("twentyFourHours", encode(bundle.getString("timeFormat.2")));
        map.put("feedbackShare", encode(bundle.getString("feedbackShare")));
        map.put("email", encode(bundle.getString("feedbackCommand.email.label")));
        map.put("send", encode(bundle.getString("send")));
        return map;
    }


    public static String encode(String value) {

        try {
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String formatLocation(String content, String city, String country, String langCode) {
        String pattern = encode(content);
        String arg = LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country;
        return MessageFormat.format(pattern.replaceAll("'", ""), arg).replace('+', ' ');
    }

    private String formatTwoLocations(String content, String city, String country, String langCode) {
        return MessageFormat.format(encode(content).replaceAll("'", ""), LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country, LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country);
    }

    private String formatThreeLocations(String content, String city, String country, String langCode) {
        return MessageFormat.format(encode(content).replaceAll("'", ""), LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country, LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country, LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country);
    }

    private String formatWithHourIndex(String content, String city, String country, String langCode, char hrIndex) {
        return MessageFormat.format(encode(content).replaceAll("'", ""), LanguageUtil.validateSlavCurrentCode(city, langCode) + ", " + country, hrIndex);
    }

    private String generateHistoryDescription(ResourceBundle bundle, String city, String country, String langCode, char hrIndex, boolean isSlav) {
        String descr = "";
        if (isSlav) {
            if (langCode.equals("ua") || langCode.equals("ru")) {
                if (hrIndex == '3') {
                    descr = formatTwoLocations(bundle.getString("descrHistory3"), city, country, langCode);
                } else if (hrIndex == '1') {
                    descr = formatLocation(bundle.getString("descrHistory1"), city, country, langCode);
                }
            } else if (langCode.equals("by")) {
                descr = formatThreeLocations(bundle.getString("descrHistory" + hrIndex), city, country, langCode);
            }
        } else if (langCode.equals("fr") || langCode.equals("it")) {
            descr = formatThreeLocations(bundle.getString("descrHistory"), city, country, langCode);
        } else if (langCode.equals("en")) {
            descr = formatWithHourIndex(bundle.getString("descrHistory"), city, country, langCode, hrIndex);
        } else {
            descr = formatLocation(bundle.getString("descrHistory"), city, country, langCode);
        }
        return descr;
    }


    private String generateUniversalDaysDescription(ResourceBundle bundle, String city, String country, String langCode, boolean is3, boolean is7) {
        String desc = "";
        if (langCode.equals("by") || langCode.equals("fr") || langCode.equals("it")) {
            desc = formatTwoLocations(is3 ? bundle.getString("descr3Days")
                    : is7 ? bundle.getString("descr7Days") : bundle.getString("descr14Days"), city, country, langCode);
        } else if (langCode.equals("ua") || langCode.equals("ru")) {
            if (is3 || is7) {
                desc = formatLocation(is3 ? bundle.getString("descr3Days")
                        : bundle.getString("descr7Days"), city, country, langCode);
            } else {
                desc = formatTwoLocations(bundle.getString("descr14Days"), city, country, langCode);
            }
        } else if (langCode.equals("de") || langCode.equals("en")) {
            desc = formatLocation(is3 ? bundle.getString("descr3Days") : is7 ? bundle.getString("descr7Days") : bundle.getString("descr14Days"), city, country, langCode);
        }
        return desc;
    }

    private String generateOutlookDescription(ResourceBundle bundle, String city, String country, String langCode, boolean isSlav) {
        String desc = "";
        if (isSlav || langCode.equals("en") || langCode.equals("de")) {
            desc = formatTwoLocations(bundle.getString("descrOutlook"), city, country, langCode);
        } else if (langCode.equals("fr")) {
            desc = formatLocation(bundle.getString("descrOutlook"), city, country, langCode);
        } else if (langCode.equals("it")) {
            desc = encode(bundle.getString("descrOutlook"));
        }
        return desc;
    }

    public HashMap genHeaderByPageName(ResourceBundle resourceBundle, String city, String countryName, String languageCode, String path, char hrIndex){
        if (path.contains("widgets")) {
            return generateWidgetContent(resourceBundle);
        } else if (path.equals("/") || path.equals("%2F")||path.split("/").length == 4 && !path.contains("widgets")) {
            return generateFrontPageContent(resourceBundle, city, countryName, languageCode);
        } else if (path.contains("outlook")) {
            return generateOutlookContent(resourceBundle, city, countryName, languageCode);
        } else if (path.contains("today") || path.contains("tomorrow")) {
            return generateTodayTomorrowContent(resourceBundle, city, countryName, languageCode, path.contains("today"));
        } else if (path.contains("history")) {
            return generatePastWeatherContent(resourceBundle, city, countryName, languageCode, hrIndex);
        } else if (path.contains("hour-by-hour")) {
            return generateHourlyContent(resourceBundle, city, countryName, languageCode, hrIndex);
        } else if (path.contains("3") || path.contains("7") || path.contains("14")) {
            return generateUniversalDaysContent(resourceBundle, path.contains("3"), path.contains("7"), city, countryName, languageCode);
        } else if (path.contains("5") || path.contains("10")) {
            return generateNotUniversalDaysContent(resourceBundle, path.contains("5"), city, countryName, languageCode);
        } else if (path.contains("map")) {
            return generateTemperatureMapContent(resourceBundle, city, countryName, languageCode);
        }

        return null;
    }
}
