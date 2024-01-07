package com.alvonellos.bug.controller.web;

import com.alvonellos.bug.dto.notxml.XmlUrl;
import com.alvonellos.bug.dto.notxml.XmlUrlSet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SitemapController {

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    @ResponseBody
    public XmlUrlSet main() {
        XmlUrlSet xmlUrlSet = new XmlUrlSet();
        create(xmlUrlSet, "/fortune", XmlUrl.Priority.HIGH);

        return xmlUrlSet;
    }

    private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority) {
        xmlUrlSet.addUrl(new XmlUrl("http://fortune.home.alvonellos.com" + link, priority));
    }

}