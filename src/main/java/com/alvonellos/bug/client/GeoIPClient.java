package com.alvonellos.bug.client;

import com.alvonellos.bug.config.GeoIPClientURL;
import com.alvonellos.bug.dto.GeoIPResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@Log
@Service
@RequiredArgsConstructor
public class GeoIPClient {

    private final ClientProperties properties;

    private final RestTemplate template;

    private static final HashMap<String, GeoIPResponse> cache = new HashMap<>();

    public GeoIPResponse getIPLookup(String ip) {
        final GeoIPClientURL geoip = properties.getGeoIPClientURL();
        if (geoip == null) {
            throw new IllegalStateException("GeoIPClientURL is not configured");
        }

        if(cache.containsKey(ip))
            return cache.get(ip);

        URI url =
                UriComponentsBuilder.newInstance()
                        .scheme(geoip.getScheme())
                        .host(geoip.getBaseUrl())
                        .pathSegment(geoip.getApi())
                        .pathSegment(geoip.getLookup())
                        .queryParam("ip", ip)
                        .build()
                        .toUri();

        log.info("Getting geoip for " + geoip.getBaseUrl());
        try {
            GeoIPResponse response = template.exchange(url, HttpMethod.POST, null, GeoIPResponse.class).getBody();
            cache.put(ip, response);
            log.info("Country " + response.country());

            log.info("Got geoip : " + response);
            return response;
        } catch (Exception e) {
            cache.put(ip, null);
            log.info("Error while getting geoip from " + geoip.getBaseUrl());
            return null;
        }
    }
}
