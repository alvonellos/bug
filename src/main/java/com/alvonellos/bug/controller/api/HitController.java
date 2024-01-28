package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.annotations.ApiPrefixController;
import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.service.HitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hibernate.internal.util.NullnessHelper.coalesce;

@Log
@ApiPrefixController
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HitController {
    private final HitService hitService;

    private final Clock clock;
    private static final SecureRandom RANDOM = new SecureRandom();
    @GetMapping(value = "hits", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HitDTO>> getAll() {
        log.entering(this.getClass().getName(), "getAll");

        val result = hitService.findAll();

        log.exiting(this.getClass().getName(), "getAll", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("hit/{id}")
    public ResponseEntity<HitDTO> get(@PathVariable UUID id) {
        log.entering(this.getClass().getName(), "get", id);

        val result = hitService.get(id);

        log.exiting(this.getClass().getName(), "get", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "hit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URI> hit(HttpServletRequest request) {
        log.entering(this.getClass().getName(), "hit", request);

        final UUID id = hitService.register(
                HitDTO.builder()
                        .id(UUID.randomUUID())
                        .url(request.getRequestURI())
                        .method(HttpMethod.GET.toString())
                        .host(request.getRemoteHost())
                        .ip(request.getHeader("X-FORWARDED-FOR"))
                        .userAgent(request.getRemoteUser())
                        .referer(request.getHeader("Referer"))
                        .isPixelHit(false)
                        .accessed(LocalDateTime.now(clock))
                        .build());

        log.exiting(this.getClass().getName(), "post");
        return ResponseEntity.created(URI.create("/hit/" + id)).build();
    }


    @GetMapping(value = "hit/bug.*", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getBug(HttpServletRequest request, HttpServletResponse response) {
        log.entering(this.getClass().getName(), "getBug");

        //casting necessary because a byte in java is signed,
        // and these are unsigned values. ranging 0 256, not
        // -128 to 127
        final byte[] pixel = {
                0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x02, 0x00, 0x02, 0x00, (byte) 0x80, (byte) 0xff, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x21, (byte) 0xf9, 0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x2c, 0x00,
                0x00, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00, 0x02, 0x02, 0x44, 0x01, 0x00, 0x3b
        };

        // Generate a random byte and replace a non-critical byte in the pixel array
        // Choosing a non-critical byte (you can experiment with different indices)
        pixel[13] = (byte) RANDOM.nextInt(0, 256);

        final UUID hitId = hitService.register(HitDTO.builder()
                .id(UUID.randomUUID())
                .url(request.getRequestURI())
                .method(HttpMethod.GET.toString())
                .host(request.getRemoteHost())
                .ip(coalesce(request.getHeader("X-FORWARDED-FOR"), request.getHeader("x-forwarded-for"), request.getHeader("X-Forwarded-For"), request.getHeader("X-Real-IP"), request.getRemoteAddr()))
                .userAgent(request.getRemoteUser())
                .referer(request.getHeader("Referer"))
                .isPixelHit(true)
                .accessed(LocalDateTime.now(clock))
                .build());

        response.setHeader("Location", "/hit/" + hitId);

        return new ResponseEntity<>(pixel, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Long> count() {
        log.entering(this.getClass().getName(), "count");

        val result = hitService.count();

        log.exiting(this.getClass().getName(), "count", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("clear")
    public void clear() {
        log.entering(this.getClass().getName(), "clear");

        hitService.clear();

        log.exiting(this.getClass().getName(), "clear");
    }
}
