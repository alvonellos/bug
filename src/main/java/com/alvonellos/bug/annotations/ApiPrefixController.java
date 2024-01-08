package com.alvonellos.bug.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@RequestMapping("/api")
//@CrossOrigin(allowedHeaders = "*", origins = "*")
//look so we could uncomment this, and then it would work,
//but check out how I did it in WebMvcConfig. Slick to do it globally.
public @interface ApiPrefixController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
