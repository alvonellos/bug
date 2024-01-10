package com.alvonellos.bug.controller.web;

import com.alvonellos.bug.dto.notxml.XmlUrl;
import com.alvonellos.bug.dto.notxml.XmlUrlSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public XmlUrlSet main() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        create(xmlUrlSet, "/", XmlUrl.Priority.HIGH);
        create(xmlUrlSet, "/api/hit", XmlUrl.Priority.HIGH);
        create(xmlUrlSet, "/api/hit/bug.jpg", XmlUrl.Priority.HIGH);

        return xmlUrlSet;
    }

    private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority) {
        xmlUrlSet.addUrl(new XmlUrl("https://bug.home.alvonellos.com" + link, priority));
    }

}