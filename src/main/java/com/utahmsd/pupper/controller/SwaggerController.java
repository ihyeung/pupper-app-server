package com.utahmsd.pupper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@RestController
public class SwaggerController {

    @GetMapping(path="/api")
    public String swaggerApi() {
        return "redirect:swagger-ui.html";
    }

    @GetMapping(path = "/sla", produces = "application/xml")
    public @ResponseBody String slaHealthCheck() {
        return "<ShouldCall>true</ShouldCall>";
    }
}