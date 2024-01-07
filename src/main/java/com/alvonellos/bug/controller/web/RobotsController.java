package com.alvonellos.bug.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class RobotsController {

    //this is one way to do it, but I also did it using thymeleaf.

    @GetMapping(value = "/dynamic/robots.txt", produces = "text/plain")
    public String getRobots(HttpServletRequest request, HttpServletResponse response) {

        return (Arrays.asList("bug.alvonellos.com", "www.alvonellos.com").contains(request.getServerName())) ?
                "robotsAllowed" : "robotsDisallowed";
    }
}
