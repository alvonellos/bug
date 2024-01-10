package com.alvonellos.bug.dto;

import com.alvonellos.bug.repo.dao.HitEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@JsonInclude
public class HitDTO {

    @JsonProperty("id")
    private final UUID id;

    @JsonProperty("url")
    private final String url;

    @JsonProperty("method")
    private final String method;

    @JsonProperty("host")
    private final String host;

    @JsonProperty("ip")
    private final String ip;

    @JsonProperty("userAgent")
    private final String userAgent;

    @JsonProperty("referer")
    private final String referer;

    @JsonProperty("isPixelHit")
    private final Boolean isPixelHit;

    @JsonProperty("accessed")
    private final LocalDateTime accessed;

    public HitDTO(HitEntity hitEntity) {
        this.id = hitEntity.getId();
        this.url = hitEntity.getUrl();
        this.method = hitEntity.getMethod();
        this.host = hitEntity.getHost();
        this.ip = hitEntity.getIp();
        this.userAgent = hitEntity.getUserAgent();
        this.referer = hitEntity.getReferer();
        this.isPixelHit = hitEntity.getIsPixelHit();
        this.accessed = hitEntity.getAccessed();
    }
}
