package com.alvonellos.bug.repo.dao;

import com.alvonellos.bug.dto.HitDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "hit")
public class HitEntity {
    @Id
    @Column(name = "id", nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "url")
    private String url;

    @Column(name = "method")
    private String method;

    @Column(name = "host")
    private String host;

    @Column(name = "ip")
    private String ip;

    @Column(name = "userAgent")
    private String userAgent;

    @Column(name = "referer")
    private String referer;

    @Column(name = "accessed", nullable = false)
    @Builder.Default
    private LocalDateTime accessed = LocalDateTime.now();

    public HitEntity(HitDTO hitDTO) {
        this.url = hitDTO.getUrl();
        this.method = hitDTO.getMethod();
        this.ip = hitDTO.getIp();
        this.userAgent = hitDTO.getUserAgent();
        this.referer = hitDTO.getReferer();
    }
}
