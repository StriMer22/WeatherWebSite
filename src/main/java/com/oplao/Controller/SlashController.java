package com.oplao.Controller;


import com.oplao.Utils.LanguageUtil;
import com.oplao.service.LanguageService;
import com.oplao.service.SearchService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
public class SlashController {


    @Autowired
    SearchService searchService;
    @Autowired
    LanguageService languageService;

    @RequestMapping("/")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response,
                       @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                       @CookieValue(value = "langCookieCode", defaultValue = "") String languageCookieCode){

        JSONObject generatedCity = searchService.findSelectedCity(request, response, currentCookieValue); //is done to generate location before the page is loaded
        String reqUrl = request.getRequestURI();
        String selectedLang = searchService.selectLanguage(reqUrl, request, response, languageCookieCode, generatedCity, currentCookieValue);
        ModelAndView modelAndView = new ModelAndView("main_jsp");

        Locale locale = new Locale(languageCookieCode, LanguageUtil.getCountryCode(languageCookieCode));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages_" + selectedLang, locale);

        HashMap hashMap = languageService.genHeaderByPageName(resourceBundle, generatedCity.getString("name"), generatedCity.getString("countryName"), languageCookieCode, "/", '3');

        modelAndView.addObject("details", hashMap);
        modelAndView.addObject("pageName", "/");


        return modelAndView;
    }

    @RequestMapping("/get_slash_data")
    @ResponseBody
    public HashMap getShashData(HttpServletRequest request, HttpServletResponse response,
                                @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                @CookieValue(value = "langCookieCode", defaultValue = "") String languageCookieCode){
        JSONObject city = searchService.findSelectedCity(request, response, currentCookieValue); //is done to generate location before the page is loaded
        String reqUrl = request.getRequestURI();
        String selectedLang = searchService.selectLanguage(reqUrl, request, response, languageCookieCode, city, currentCookieValue);
        HashMap map = new HashMap();
        map.put("langCode", selectedLang.toLowerCase());
        map.put("city", city.getString("asciiName"));
        map.put("countryCode", city.getString("countryCode"));
        return map;
    }

    @RequestMapping("/generate_language_content")
    @ResponseBody
    public HashMap generateLanguageContent(@RequestParam(value = "langCode", required = false) String langCode, @RequestParam(value = "path") String path, HttpServletRequest request, HttpServletResponse response, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue){

        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return languageService.generateLanguageContent(langCode, path, searchService.findSelectedCity(request, response, currentCookieValue));
    }

    @RequestMapping("/generate_lang_code")
    @ResponseBody
    public String generateLangCode(HttpServletRequest request, HttpServletResponse response, @CookieValue(value = SearchService.cookieName, defaultValue = "") String currentCookieValue){
        return  searchService.findSelectedCity(request, response, currentCookieValue).getString("countryCode").toLowerCase();
    }
}
