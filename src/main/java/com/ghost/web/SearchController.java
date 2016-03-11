package com.ghost.web;

import com.ghost.config.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    private AppConfiguration configuration;

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
        modelMap.put("appName", configuration.getAppName());
        return "searchResult";
    }

}
