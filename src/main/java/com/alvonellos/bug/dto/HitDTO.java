package com.alvonellos.bug.dto;

import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.Cookie;
@Data
@Builder
@AllArgsConstructor
public class HitDTO {

    private final UUID id;

    private final String url;

    private final String method;

    private final String host;

    private final String ip;

    private final String userAgent;

    private final String referer;

    private final List<Cookie> cookies;

    private final LocalDateTime accessed;

    public HitDTO(HitEntity hitEntity) {
        this.id = hitEntity.getId();
        this.url = hitEntity.getUrl();
        this.method = hitEntity.getMethod();
        this.host = hitEntity.getHost();
        this.ip = hitEntity.getIp();
        this.userAgent = hitEntity.getUserAgent();
        this.referer = hitEntity.getReferer();
        this.accessed = hitEntity.getAccessed();
        this.cookies = hitEntity.getCookies();
    }
}
