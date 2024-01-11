package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.repo.HitRepository;
import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@Slf4j
@AutoConfigureTestDatabase
public class HitServiceTest {

    @Mock
    private HitRepository hitRepository;

    @InjectMocks
    private HitService hitService;

    @Before
    public void prepare() {
        log.info("Preparing test environment");
        openMocks(this);
    }
    @Test
    public void findAll_andEmpty() {
        log.info("Running test findAll_andEmpty()");
        doReturn(List.of()).when(hitRepository).findAll();

        List<HitDTO> hits = hitService.findAll();

        assertEquals(0, hits.size());
        verify(hitRepository, times(0)).findAll();
    }

    @Test
    public void findAll_andSingle() {
        log.info("Running test findAll_andSingle");
        doReturn(1L).when(hitRepository).count();
        doReturn(new PageImpl<>(
                List.of(
                        HitEntity.builder()
                              .id(UUID.randomUUID())
                              .ip("127.0.0.1")
                              .url("https://www.google.com")
                              .host("www.google.com")
                              .referer("https://www.google.com")
                              .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_1")
                              .method(HttpMethod.GET.name())
                              .isPixelHit(true)
                              .build()
        ))).when(hitRepository).findAll((Pageable) any());

        List<HitDTO> hits = hitService.findAll();

        assertEquals(1, hits.size());
        verify(hitRepository, times(1)).findAll((Pageable) any());
    }


    @Test
    public void get() {
        log.info("Running test get() by ID");
        final HitEntity hit = HitEntity.builder()
                .id(UUID.randomUUID())
                .ip("127.0.0.1")
                .url("https://www.google.com")
                .host("www.google.com")
                .referer("https://www.google.com")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_1")
                .method(HttpMethod.GET.name())
                .isPixelHit(true)
                .build();
        doReturn(Optional.of(hit)).when(hitRepository).findById(any());

        HitDTO hit2 = hitService.get(hit.getId());

        assertEquals("Hit should equal", hit.getId(), hit2.getId());
        verify(hitRepository, times(1)).findById(any());
    }

    @Test
    public void getByPage() {
        log.info("Running test get() by Page");
        final HitEntity hit = HitEntity.builder()
                .id(UUID.randomUUID())
                .ip("127.0.0.1")
                .url("https://www.google.com")
                .host("www.google.com")
                .referer("https://www.google.com")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_1")
                .method(HttpMethod.GET.name())
                .isPixelHit(true)
                .build();

        doReturn(new PageImpl<>(List.of(hit))).when(hitRepository).findAll((Pageable) any());


        HitDTO hit2 = hitService.get(0, 1, Sort.Direction.ASC).toList().get(0);

        assertEquals("Hit should equal", hit.getId(), hit2.getId());
        verify(hitRepository, times(1)).findAll((Pageable) any());
    }

    @Test
    public void count_andZero() {
        log.info("Running count_andZero()");

        doReturn(0L).when(hitRepository).count();

        hitService.count();

        verify(hitRepository, times(1)).count();

    }

    @Test
    public void count_andOne() {
        log.info("Running count_andOne()");

        doReturn(1L).when(hitRepository).count();

        hitService.count();

        verify(hitRepository, times(1)).count();

    }

    @Test
    public void register() {
        log.info("Testing register");

        doNothing().when(hitRepository).save(any(HitEntity.class));

        hitService.register(HitDTO.builder().build());

        verify(hitRepository, times(1)).save(any(HitEntity.class));
    }

    @Test
    public void clear() {
        log.info("Testing clear()");

        doNothing().when(hitRepository).deleteAll();

        hitService.clear();

        verify(hitRepository, times(1)).deleteAll();
    }
}