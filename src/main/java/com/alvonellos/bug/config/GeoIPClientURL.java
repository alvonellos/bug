package com.alvonellos.bug.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class GeoIPClientURL {
    @NotBlank
    private String scheme;

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String api;

    @NotBlank
    private  String lookup;
}
