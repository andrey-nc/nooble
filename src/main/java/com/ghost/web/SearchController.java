package com.ghost.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.ghost.NoobleApplication;
import com.ghost.json.View;
import com.ghost.lucene.search.SearchDocument;
import com.ghost.lucene.search.SearchService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Locale;

@Controller
@Scope("session")
public class SearchController {

    @Autowired
    private Environment environment;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchForm(ModelMap map) {
        NoobleApplication.log.info("App name: {}", environment.getProperty("spring.application.name"));
        map.put("appName", environment.getProperty("spring.application.name"));
        return "search";
    }

    /**
     * Indexed docs search using lucene
     * @param query to search
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("q") String query,
                         ModelMap model,
                         Locale locale) {
        NoobleApplication.log.info("Search query: {}", query);
        searchService.getSentQueries().add(query);
        model.addAttribute("appName", environment.getProperty("spring.application.name"));
        model.addAttribute("query", query);
        try {
            searchService.search(query);
            model.addAttribute("pages", searchService.getResultDocs(0));
            model.addAttribute("resultCount", searchService.getTotalHits());
            model.addAttribute("searchTime", searchService.getSearchTimeString());
            model.addAttribute("start", searchService.getStart());
        } catch (ParseException e) {
            model.addAttribute("statusError", messageSource.getMessage("search.error.parse", null, locale));
        } catch (IOException e) {
            model.addAttribute("statusError", messageSource.getMessage("index.error.directory", null, locale));
        }
        return "searchResult";
    }

    /**
     * Processes ajax POST request for more results to show using JSON
     * @param start is the number of shown docs on search result page
     * @return JSON representation of SearchDocument Collection
     */
    @JsonView(View.Public.class)
    @RequestMapping(value = "/ajax", produces = "application/json")
    public @ResponseBody
    Collection<SearchDocument> searchList(@RequestBody Integer start) {
        if (start != null) {
            return searchService.getResultDocs(start);
        } else {
            NoobleApplication.log.error("Param 'start' is null!");
        }
        return null;
    }
}
