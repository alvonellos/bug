package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.annotations.ApiPrefixController;
import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.service.HitService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Log
@ApiPrefixController
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HitController {
    private final HitService hitService;
    @GetMapping("hit")
    public ResponseEntity<List<HitDTO>> getAll() {
        log.entering(this.getClass().getName(), "getAll");
        val result = hitService.findAll();
        log.exiting(this.getClass().getName(), "getAll", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("hit")
    public ResponseEntity<List<HitDTO>> post(HttpServletRequest request) {
        log.entering(this.getClass().getName(), "post", request);
        hitService.post(
                HitDTO.builder()
                        .ip(request.getRemoteHost())
        );
    }

    @PostMapping("statistics")
    public URI post(
            @RequestParam String userAgent, @RequestParam String method,
            @RequestParam String requestURI, @RequestParam String protocol,
            @RequestParam String remoteHost, @RequestParam String remoteAddr)
    {

        return new ResponseEntity<>(HitDTO.builder()
                        .accessed(LocalDateTime.now())
                        .userAgent(userAgent)
                        .method(method)
                        .requestURI(requestURI)
                        .requestProtocol(protocol)
                        .remoteHost(remoteHost)
                        .remoteAddr(remoteAddr).build()
        ), HttpStatus.OK);
    }
}
