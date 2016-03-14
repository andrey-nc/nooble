package com.ghost.web;

import com.ghost.lucene.exceptions.CreateDirectoryException;
import com.ghost.lucene.search.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Locale;

@Controller
@Scope("session")
@PropertySource("classpath:application.properties")
public class SearchController {

    @Autowired
    private Environment environment;

    @Autowired
    private Searcher searcher;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchForm() {
        System.out.println("SearchController - searchForm: ");
        return "search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam("q") String query, ModelMap map, Locale locale) {
        System.out.println(SearchController.class);
//        modelMap.put("appName", configuration.getAppName());
        map.put("appName", environment.getProperty("spring.application.name"));
        try {
            map.put("pages", searcher.search(query));
        } catch (ParseException e) {
            map.put("PARSE_ERROR", messageSource.getMessage("search.error.parse", null, locale));
        } catch (CreateDirectoryException e) {
            map.put("PATH_ERROR", messageSource.getMessage("search.error.path", null, locale));
        } catch (IOException e) {
            map.put("DIRECTORY_ERROR", messageSource.getMessage("search.error.directory", null, locale));
            e.printStackTrace();
        }
        map.put("resultCount", searcher.getTotalHits());
        return "searchResult";
    }

}
