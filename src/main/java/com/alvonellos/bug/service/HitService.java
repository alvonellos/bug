package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.repo.HitRepository;
import com.alvonellos.bug.repo.dao.HitEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
                            (hitRepository.count().intValue()),
                            Sort.by(Sort.Direction.ASC, "accessed")
                    )
        ).stream()
                .map(HitDTO::new)
                .toList();

        log.trace("findAll returned " + dtos.size());
        return dtos;
    }

    public HitDTO get(UUID id) {
        log.trace(String.format("get(%s)", id));

        final HitEntity hitEntity = hitRepository.findById(id).orElseThrow();

        log.trace("get hit " + hitEntity.getId());
        return new HitDTO(hitEntity);
    }

    public Page<HitDTO> get(int pageNumber, int pageSize, Sort.Direction direction) {
        log.trace(String.format("get(%s, %s, %s)", pageNumber, pageSize, direction));

        final Page<HitEntity> hits = hitRepository
                .findAll(PageRequest.of(pageNumber, pageSize, direction, "accessed"));

        log.trace("get hits " + hits);
        return hits.map(HitDTO::new);
    }

    public Long count() {
        log.trace("count()");

        final Long count = hitRepository.count();

        log.trace("count returned " + count);
        return count;
    }

    public UUID register(HitDTO build) {
        log.trace("register(build)");

        final HitEntity hitEntity = new HitEntity(build);
        hitEntity.setId(UUID.randomUUID());
        hitRepository.save(hitEntity);

        log.trace("post saved " + hitEntity);
        return hitEntity.getId();
    }

    public void clear() {
        log.trace("clear()");

        hitRepository.deleteAll();

        log.trace("clear deleted all hits");
    }
}
