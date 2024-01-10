package com.alvonellos.bug.controller.web;

import com.alvonellos.bug.service.HitService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log
public class WebsiteController {

    private final HitService hitService;
    @RequestMapping({
            "/",
            "/index",
    })
    public String index(ModelMap modelMap) {
        modelMap.put("bugcount", hitService.count());
        return "html/index";
    }

    @GetMapping("/robots.txt")
    public String robotsTxt(ModelMap model) {
        model.put("sitemapUrl", "https://bug.home.alvonellos.com/sitemap.xml");
        return "text/robots"; // Return the text template name
    }

    @GetMapping("/favicon.ico")
    public String favicon() {
        log.entering(this.getClass().getName(), "favicon.ico");
        return "forward:/assets/img/favicon.ico";
    }
}