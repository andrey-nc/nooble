package com.ghost.web;

import com.ghost.config.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PropertySource("classpath:application.properties")
public class SearchController {

    @Autowired
    private AppConfiguration configuration;

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchForm() {
        System.out.println("SearchController - searchForm: ");
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("q") String query, ModelMap modelMap) {
        System.out.println(SearchController.class);
        modelMap.put("searchResult", query);
        modelMap.put("resultCount", 45);
//        modelMap.put("appName", configuration.getAppName());
        modelMap.put("appName", environment.getProperty("spring.application.name"));
        return "searchResult";
    }

}
