package com.ghost.web;

import com.ghost.NoobleApplication;
import com.ghost.json.JsonSerializer;
import com.ghost.lucene.search.SearchService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Locale;

@Controller
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
     * @param start the number of shown docs on search result page
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("q") String query,
                         @RequestParam(name = "start", required = false) Integer start,
                         ModelMap model,
                         Locale locale) {
        NoobleApplication.log.info("Search query: {}", query);
        searchService.getSentQueries().add(query);
        model.addAttribute("appName", environment.getProperty("spring.application.name"));
        model.addAttribute("query", query);

        if (start == null) {
            try {
                searchService.search(query);
                model.addAttribute("pages", searchService.getResultDocs(0));
                model.addAttribute("resultCount", searchService.getTotalHits());
                model.addAttribute("searchTime", searchService.getSearchTime());
                model.addAttribute("start", searchService.getDocsPerPage());
            } catch (ParseException e) {
                model.addAttribute("statusError", messageSource.getMessage("search.error.parse", null, locale));
            } catch (IOException e) {
                model.addAttribute("statusError", messageSource.getMessage("index.error.directory", null, locale));
            }
        }
        if (start != null) {
            // to JSON
            model.addAttribute("newPages", JsonSerializer.serialize(searchService.getResultDocs(start)));
            model.addAttribute("start", searchService.getDocsPerPage() + start);
            model.addAttribute("searchTime", searchService.getSearchTime());
            model.addAttribute("resultCount", searchService.getTotalHits());
        }
        return "searchResult";
    }
}
