package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.dto.KVDTO;
import com.alvonellos.bug.repo.HitRepository;
import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitService {
    private final HitRepository hitRepository;

    public List<HitDTO> findAll() {
        log.trace("findAll()");

        if(hitRepository.count() == 0) {
            return List.of();
        }

        //Query the database, find all hits, and convert them to DTOs.
        final List<HitDTO> dtos = hitRepository.findAll(
                PageRequest
                    .of(0,
                            (int) (hitRepository.count() - 1),
                            Sort.by(Sort.Direction.ASC, "accessed")
                    )
        ).stream()
                .map(HitDTO::new)
                .toList();

        log.trace("findAll returned " + dtos.size());
        return dtos;
    }

    public Long count() {
        log.trace("count()");

        final Long count = hitRepository.count();

        log.trace("count returned " + count);
        return count;
    }
}
