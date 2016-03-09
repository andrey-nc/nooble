package com.ghost.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchForm() {
        System.out.println("SearchController - searchForm: ");
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("q") String query, ModelMap modelMap) {
        System.out.println("SearchController - searchForm - query: " + query);
        modelMap.put("searchResult", query);
        modelMap.put("resultCount", 45);
        return "searchResult";
    }
}
