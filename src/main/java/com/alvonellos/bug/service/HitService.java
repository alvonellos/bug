package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.repo.HitRepository;
import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
                .map(hitEntity -> new HitDTO(hitEntity, cookies))
                .toList();

        log.trace("findAll returned " + dtos.size());
        return dtos;
    }

    public HitDTO get(UUID id) {
        log.trace(String.format("get(%s)", id));

        final HitEntity hitEntity = hitRepository.findById(id).orElseThrow();

        log.trace("get hit " + hitEntity.getId());
        return new HitDTO(hitEntity, cookies);
    }

    public Page<HitDTO> get(@NotNull Pageable pageable) {
        log.trace(String.format("get(%s)", pageable));

        final Page<HitEntity> hits = hitRepository.findAll(pageable);

        log.trace("get hits " + hits);
        return hits.map(hitEntity -> new HitDTO(hitEntity, cookies));
    }

    public Long count() {
        log.trace("count()");

        final Long count = hitRepository.count();

        log.trace("count returned " + count);
        return count;
    }

    public UUID post(HitDTO build) {
        log.trace("post(build)");

        final HitEntity hitEntity = new HitEntity(build);
        hitRepository.save(hitEntity);

        log.trace("post saved " + hitEntity);
        return hitEntity.getId();
    }
}
