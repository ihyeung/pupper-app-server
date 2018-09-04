package com.utahmsd.pupper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/")
public class HealthController {

//    @RequestMapping(value="/swagger-ui.html")
//    public String swaggerUi() {
//        return "swagger-ui.html";
//    }

    @RequestMapping(path="api")
    public String swaggerApi() {
        return "redirect:swagger-ui.html";
    }

    @RequestMapping(path = "sla", method = RequestMethod.GET, produces = "application/xml")
    public @ResponseBody String slaHealthCheck() {
        return "<ShouldCall>true</ShouldCall>";
    }

}