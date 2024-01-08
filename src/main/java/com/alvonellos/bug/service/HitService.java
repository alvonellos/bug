package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.dto.KVDTO;
import com.alvonellos.bug.repo.HitRepository;
import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository hitRepository;

    public Page<HitDTO> findAll(int page, int size) {
        log.trace(this.getClass().getName(), "findAll", String.format("%d %d", page, size));

        Page<HitEntity> entityPage = hitRepository.findAll(PageRequest.of(page, size));
        List<KVDTO> dtos = entityPage.getContent().stream()
                .map(com.alvonellos.website.dto.KVDTO::new)
                .collect(Collectors.toList());
        log.exiting(this.getClass().getName(), "findAll", dtos.size());
        return new PageImpl<>(dtos, entityPage.getPageable(), entityPage.getTotalElements());


    }
}
