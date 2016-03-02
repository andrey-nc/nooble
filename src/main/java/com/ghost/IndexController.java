package com.ghost;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/index")
    //@ResponseBody
    public String index() {
        System.out.println("IndexController - index");
        return "index";
    }
}
