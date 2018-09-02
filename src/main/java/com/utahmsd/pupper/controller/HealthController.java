package com.utahmsd.pupper.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@RestController
@Api(value = "Health Controller")
@RequestMapping(value = "/")
public class HealthController {

    @RequestMapping(value ="api", method= RequestMethod.GET)
    public String swaggerApi() {
        return "redirect:/swagger-ui.html";
    }
    @RequestMapping(value = "sla", method = RequestMethod.GET)
    public @ResponseBody String slaHealthCheck() {
        return "redirect:/sla/GetStatus?depth=1";
    }


}