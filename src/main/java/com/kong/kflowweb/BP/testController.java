package com.kong.kflowweb.BP;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2019-12-17 09:46
 **/
@RequestMapping(value = "/test")
@Controller
public class testController {
    @ResponseBody
    @RequestMapping("/hello")
    public String test(){
        return "when i was asleep,she told me in the dream that you were awake.";
    }
}
