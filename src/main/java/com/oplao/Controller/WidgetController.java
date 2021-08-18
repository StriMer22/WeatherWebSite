package com.oplao.Controller;

import com.oplao.service.SearchService;
import com.oplao.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
public class WidgetController {

    @Autowired
    WidgetService widgetService;
    @Autowired
    SearchService searchService;
    @RequestMapping("get_info_widgets")
    @ResponseBody
    public HashMap getInfoWidgets(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @CookieValue(name = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                  @CookieValue(name = SearchService.langCookieCode, defaultValue = "") String langCookieCode,
                                  @RequestParam(value = "city") String city,
                                  @RequestParam(value = "lang", required = false) String lang){


        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");

       return widgetService.getInfoWidgets(city, lang, langCookieCode);

    }

}
