package com.alvonellos.bug.controller.web;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log
public class WebsiteController {
    private final DataSource dataSource;

    @RequestMapping({
            "/",
            "/index",
    })
    public String index() {
        return "html/index";
    }

    @GetMapping("/robots.txt")
    public String robotsTxt(Model model) {
        model.addAttribute("sitemapUrl", "https://www.example.com/sitemap.xml");
        return "text/robots.txt.txt"; // Return the text template name
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        log.entering(this.getClass().getName(), "favicon.ico");
        return "forward:/assets/img/favicon.ico";
    }
}