package com.alvonellos.bug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafConfig {

    public static final String CLASSPATH_TEMPLATES = "classpath:/templates/";

    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.addTemplateResolver(textTemplateResolver());
        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        SpringResourceTemplateResolver htmlTemplateResolver = new SpringResourceTemplateResolver();
        htmlTemplateResolver.setPrefix(CLASSPATH_TEMPLATES);
        htmlTemplateResolver.setSuffix(".html");
        htmlTemplateResolver.setTemplateMode("HTML5");
        htmlTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return htmlTemplateResolver;
    }

    @Bean
    public SpringResourceTemplateResolver textTemplateResolver() {
        SpringResourceTemplateResolver textTemplateResolver = new SpringResourceTemplateResolver();
        textTemplateResolver.setPrefix(CLASSPATH_TEMPLATES);
        textTemplateResolver.setSuffix(".txt");
        textTemplateResolver.setTemplateMode(TemplateMode.TEXT);
        textTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return textTemplateResolver;
    }
}