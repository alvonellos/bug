package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.service.HitService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureTestDatabase
class HitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HitService hitService;

    private Clock clock;
    private HitController hitController;

    @BeforeEach
    void setUp() {
        log.info("HitControllerTest.setUp()");
        MockitoAnnotations.initMocks(this);
        clock = Clock.systemDefaultZone();
        hitController = new HitController(hitService, clock);
        mockMvc = MockMvcBuilders.standaloneSetup(hitController).build();
    }

    @Test
    void getAll() throws Exception {
        log.info("HitControllerTest.getAll()");
        List<HitDTO> hits = new ArrayList<>();
        hits.add(new HitDTO(UUID.randomUUID(), "/test1", "GET", "localhost", "127.0.0.1", "user-agent", null, false, LocalDateTime.now(clock)));
        hits.add(new HitDTO(UUID.randomUUID(), "/test2", "POST", "localhost", "127.0.0.1", "user-agent", null, false, LocalDateTime.now(clock)));

        when(hitService.findAll()).thenReturn(hits);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/hits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].url").value("/test1"))
                .andExpect(jsonPath("$[1].url").value("/test2"));
    }

    @Test
    void get() throws Exception {
        log.info("HitControllerTest.get()");
        UUID id = UUID.randomUUID();
        HitDTO hit = new HitDTO(id, "/test", "GET", "localhost", "127.0.0.1", "user-agent", null, false, LocalDateTime.now(clock));

        when(hitService.get((UUID) any())).thenReturn(hit);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/hit/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("/test"));
    }

    @Test
    void hit() throws Exception {
        log.info("HitControllerTest.hit()");
        when(hitService.register(any(HitDTO.class))).thenReturn(UUID.randomUUID());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/hit"))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void getBug() throws Exception {
        log.info("HitControllerTest.getBug()");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/hit/bug.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andReturn();

        byte[] expected = {
                0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x02, 0x00, 0x02, 0x00, (byte) 0x80, (byte) 0xff, 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x21, (byte) 0xf9, 0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x2c, 0x00,
                0x00, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00, 0x02, 0x02, 0x44, 0x01, 0x00, 0x3b
        };
        byte[] actual = mvcResult.getResponse().getContentAsByteArray();

        assertEquals(expected.length, actual.length);
    }

    @Test
    void count() throws Exception {
        log.info("HitControllerTest.count()");
        when(hitService.count()).thenReturn(10L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/count"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(10));
    }

    @Test
    void clear() throws Exception {
        log.info("HitControllerTest.clear()");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clear"))
                .andExpect(status().isOk());

        // Verify that the service's clear method was called
        verify(hitService, times(1)).clear();
    }
}