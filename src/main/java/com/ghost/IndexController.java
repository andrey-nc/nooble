package com.ghost;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("/index")
    //@ResponseBody
    public String indexRequest(Map<String, Object> model) {
        System.out.println("IndexController - index");
        return "index";
    }

    @RequestMapping(path = "/index?q={URI}", method = RequestMethod.POST)
    public String index(@PathVariable String URI) {
        System.out.println("IndexController - index");
        return "index";
    }
}
