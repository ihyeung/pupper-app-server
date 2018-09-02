//package com.utahmsd.pupper.controller;
//
//import com.utahmsd.pupper.dto.UserAuthenticationRequest;
//import com.utahmsd.pupper.dto.UserAuthenticationResponse;
//import com.utahmsd.pupper.service.JwtAuthenticationService;
//import io.swagger.annotations.Api;
//
//import javax.inject.Inject;
//import javax.validation.Valid;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//@Path("/login")
//@Api("Jwt")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
//public class JwtController {
//
//    @Inject
//    JwtAuthenticationService jwtAuthenticationService;
//
//    @Path("/")
//    @POST
//    public UserAuthenticationResponse login(@Valid UserAuthenticationRequest request) {
//        return jwtAuthenticationService.authenticateUser(request);
//    }
//}
