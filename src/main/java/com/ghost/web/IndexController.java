package com.ghost.web;

import com.ghost.lucene.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping(method = RequestMethod.GET)
    public String indexForm() {
        System.out.println("IndexController - indexRequest");
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String indexSubmit(@RequestParam("q") String uri) {
        System.out.println("IndexController - index: " + uri);
        // TODO: request URI indexing using Lucene API
        indexService.index();
        return "redirect:/index?q=" + uri;
    }
}
