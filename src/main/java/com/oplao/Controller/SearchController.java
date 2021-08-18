package com.oplao.Controller;

import com.oplao.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

import static com.oplao.service.SearchService.langCookieCode;

@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @RequestMapping(value = "find_occurences/{searchRequest:.+}", method = RequestMethod.POST)
    @ResponseBody
    public List<HashMap> findOccurences(@PathVariable("searchRequest") String searchRequest, @CookieValue(value = langCookieCode, defaultValue = "") String langCode) {

        return searchService.findSearchOccurences(searchRequest, langCode);
      }

    @RequestMapping(value = "get_selected_city")
    @ResponseBody
    public String getSelectedCity( @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue) {

        return searchService.getSelectedCity(currentCookieValue);
      }

    @RequestMapping(value = "get_current_city_object")
    @ResponseBody
    public HashMap getCurrentCityObject(HttpServletRequest request, HttpServletResponse response, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue) {

        return (HashMap) searchService.findSelectedCity(request, response, currentCookieValue).toMap();
    }

    @RequestMapping(value = "/select_city/{geonameId}", method = RequestMethod.POST)
    @ResponseBody
    public HashMap selectCity(@PathVariable("geonameId") int geonameId, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue, HttpServletRequest request,
                              HttpServletResponse response,
                              @CookieValue(value = "langCookieCode", defaultValue = "") String langCode) {
        return searchService.selectCity(geonameId, currentCookieValue, request, response, langCode);

    }

    @RequestMapping(value = "/generate_custom_request_weather")
    public void generateCustomRequestWeather(
                        @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                        HttpServletRequest request, HttpServletResponse response, @RequestParam("language") String language, @RequestParam("currentLocation") String currentLocation) {

        searchService.generateUrlRequestWeather(currentLocation, currentCookieValue, request, response, language);

    }
//    @RequestMapping(value = "/generate_meta_title")
//    @ResponseBody
//    public HashMap<String, String> generateMetaTitle(@RequestParam("path") String path, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue){
//        return searchService.generateMetaTitle(currentCookieValue, path);
//    }
}
