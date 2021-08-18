package com.oplao.Controller;

        import com.oplao.service.MapService;
        import com.oplao.service.SearchService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.CookieValue;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.ResponseBody;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.UnsupportedEncodingException;
        import java.net.URLDecoder;
        import java.util.HashMap;
        import java.util.List;

@Controller
public class MapController {
    @Autowired
    SearchService searchService;
    @Autowired
    MapService mapService;
    @RequestMapping("get_4_days_tabs")
    @ResponseBody
    public List<HashMap> get4DaysTabs(HttpServletRequest request, HttpServletResponse response,
                                      @CookieValue(name = SearchService.cookieName, defaultValue = "") String currentCookieValue,
                                      @CookieValue(value = "langCookieCode", defaultValue = "") String langCode){
        return mapService.create4DaysTabs(searchService.findSelectedCity(request, response, currentCookieValue), langCode);
    }

    @RequestMapping("get_map_weather")
    @ResponseBody
    public List<HashMap> getMapWeather(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @CookieValue(value = SearchService.cookieName, defaultValue = "")String currentCookieValue,
                                       @RequestParam("max") int max,
                                       @RequestParam("north") double north,
                                       @RequestParam("west") double west,
                                       @RequestParam("south") double south,
                                       @RequestParam("east") double east,
                                       @RequestParam(value = "time", required = false) String time,
                                       @CookieValue(name = SearchService.langCookieCode, defaultValue = "") String langCookieCode){

        try {
            currentCookieValue = URLDecoder.decode(currentCookieValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mapService.getMapWeather(max,north,west,south, east, time, searchService.findSelectedCity(request, response, currentCookieValue), langCookieCode);

    }



}
