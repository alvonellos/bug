package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.service.HitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = HitController.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@NoArgsConstructor
public final class HitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HitService hitService;

    @InjectMocks
    private HitController hitController;
    @Test
    public void getBug_ShouldReturnAImageInJpegFormat() throws Exception {
        val res = mockMvc.perform(
                get("/api/hit/bug.gif")
                        .contentType(MediaType.IMAGE_JPEG)
                        .accept(MediaType.IMAGE_JPEG)
                ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                            .andReturn();
;
        assertNotNull(res);
    }

    @Test
public void post_withValidRequest_returnsCreatedStatusAndLocationHeader() throws Exception {
    // Arrange
    final String userAgent = "test-user-agent";
    final String method = "POST";
    final String url = "/test/url";
    final String host = "test-host";
    final String ip = "127.0.0.1";
    final HitDTO hitDTO = HitDTO.builder()
            .accessed(LocalDateTime.now())
            .userAgent(userAgent)
            .method(method)
            .url(url)
            .host(host)
            .ip(ip)
            .build();

    given(hitService.post(hitDTO)).willReturn(UUID.randomUUID());

    // Act
    final MvcResult result = mockMvc.perform(post("/hit")
            .header("User-Agent", userAgent)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(hitDTO)))
            .andExpect(status().isCreated())
            .andReturn();

    // Assert
    final String location = result.getResponse().getHeader("Location");
    assertThat(location).isNotNull();
    final URI locationURI = new URI(location);
    assertThat(locationURI.getPath()).isEqualTo("/hit/" + hitDTO.getId());
}

@Test
public void post_withInvalidRequest_returnsBadRequestStatus() throws Exception {
    // Arrange
    final HitDTO hitDTO = HitDTO.builder()
            .accessed(LocalDateTime.now())
            .build();

    given(hitService.post(hitDTO)).willThrow(new BadRequestException("Invalid request"));

    // Act
    final MvcResult result = mockMvc.perform(post("/hit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(hitDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();

    // Assert
    final String responseBody = result.getResponse().getContentAsString();
    assertThat(responseBody).isEqualTo("Invalid request");
}

@Test
public void post_withMissingRequiredParameter_returnsBadRequestStatus() throws Exception {
    // Arrange
    final HitDTO hitDTO = HitDTO.builder()
            .accessed(LocalDateTime.now())
            .userAgent("test-user-agent")
            .method("POST")
            .url("/test/url")
            .host("test-host")
            .build();

    given(hitService.post(hitDTO)).willThrow(new BadRequestException("Missing required parameter: ip"));

    // Act
    final MvcResult result = mockMvc.perform(post("/hit")
            .header("User-Agent", hitDTO.getUserAgent())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(hitDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();

    // Assert
    final String responseBody = result.getResponse().getContentAsString();
    assertThat(responseBody).isEqualTo("Missing required parameter: ip");
}
}