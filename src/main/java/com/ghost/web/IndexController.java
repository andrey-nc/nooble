package com.ghost.web;

import com.ghost.NoobleApplication;
import com.ghost.lucene.index.IndexService;
import com.ghost.source.ConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@Controller
@Scope("session")
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.GET)
    public String indexForm(ModelMap map) {
        NoobleApplication.log.info("Index request");
        map.put("depth", indexService.getIndexDepth());
        map.put("depthMax", indexService.getMaxIndexDepth());
        return "index";
    }

    private Collection<URL> indexedLinks = new HashSet<>();

    @RequestMapping(method = RequestMethod.POST)
    public ModelMap indexSubmit(@RequestParam("q") String query,
                              @RequestParam int depth,
                              ModelMap map,
                              Locale locale) {
        NoobleApplication.log.info("Index request {}", query);
        map.put("query", query);
        try {
            URL url = new URL(query);
            if (ConnectionUtils.isAllowed(url)) {
                if (isIndexed(url)) {
                    map.put("statusError", messageSource.getMessage("index.exist", null, locale));
                    return new ModelMap("redirect:/index?q=" + query);
                }
                indexService.setIndexDepth(depth);
                indexService.index(url);
                addToIndex(url);
                map.put("indexCount", indexService.getIndexCount());
                map.put("indexTime", indexService.getIndexTime());
                map.put("statusSuccess", messageSource.getMessage("index.success", null, locale));
            } else {
                map.put("statusError", messageSource.getMessage("url.unreachable", null, locale));
                return new ModelMap("redirect:/index");
            }
        } catch (MalformedURLException e) {
            map.put("statusError", messageSource.getMessage("url.malformed", null, locale));
            return new ModelMap("redirect:/index");
        } catch (IOException e) {
            map.put("statusError", messageSource.getMessage("index.fail", null, locale));
            e.printStackTrace();
        }
        return new ModelMap("redirect:/index?q=" + query);
    }

    private boolean isIndexed(URL url) {
        return indexedLinks.contains(url);
    }

    private boolean addToIndex(URL url) {
        return indexedLinks.add(url);
    }
}
