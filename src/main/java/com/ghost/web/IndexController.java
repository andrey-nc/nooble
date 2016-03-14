package com.ghost.web;

import com.ghost.lucene.index.IndexService;
import com.ghost.source.ConnectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private MessageSource messageSource;

/*
    @Autowired
    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }
*/

    @RequestMapping(method = RequestMethod.GET)
    public String indexForm() {
        System.out.println("IndexController - indexRequest");
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
//    @Scope("session")
    public String indexSubmit(@RequestParam("q") String uri,
                              @RequestParam int depth,
                              ModelMap map,
                              HttpServletResponse response,
                              Locale locale) {
        System.out.println("IndexController - index: " + uri);
        //request.getSession().setAttribute("pages", "dsfsf");
        System.out.println("response.getContentType = " + response.getContentType());
        try {
            if (ConnectionUtils.isAllowed(uri)) {
                if (indexService.isIndexed(new URL(uri))) {
                    map.put("INDEX_STATUS", messageSource.getMessage("index.exist", null, locale));
                    return "redirect:/index?q=" + uri;
                }
                indexService.setMaxIndexDepth(depth);
                indexService.index(new URL(uri));
                map.put("indexCount", indexService.getIndexCount());
                map.put("indexTime", indexService.getIndexTime());
                map.put("INDEX_STATUS", messageSource.getMessage("index.success", null, locale));
            } else {
                map.put("URL_STATUS", messageSource.getMessage("url.unreachable", null, locale));
                map.put("errorQuery", uri);
                return "redirect:/index";
            }
        } catch (MalformedURLException e) {
            map.put("URL_STATUS", messageSource.getMessage("url.malformed", null, locale));
            map.put("errorQuery", uri);
            return "redirect:/index";
        } catch (IOException e) {
            map.put("INDEX_STATUS", messageSource.getMessage("index.fail", null, locale));
            e.printStackTrace();
        }
        return "redirect:/index?q=" + uri;
    }
}
