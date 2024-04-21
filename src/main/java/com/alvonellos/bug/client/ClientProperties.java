package com.alvonellos.bug.client;

import com.alvonellos.bug.config.GeoIPClientURL;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "geoip.client")
@Configuration
public class ClientProperties {
    private GeoIPClientURL geoIPClientURL;
}

