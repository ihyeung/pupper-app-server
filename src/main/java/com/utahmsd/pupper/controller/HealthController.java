package com.utahmsd.pupper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/")
public class HealthController {

    @GetMapping(path="api")
    public String swaggerApi() {
        return "redirect:swagger-ui.html";
    }

    @GetMapping(path = "sla", produces = "application/xml")
    public @ResponseBody String slaHealthCheck() {
        return "<ShouldCall>true</ShouldCall>";
    }

}