package com.alvonellos.bug.repo;

import com.alvonellos.bug.repo.dao.HitEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HitRepository extends PagingAndSortingRepository<HitEntity, UUID> {
    @NotNull
    Page<HitEntity> findAll(@NotNull Pageable pageable);

    Long count();

    List<HitEntity> findAll();

    void save(@NotNull HitEntity hitEntity);

    Optional<HitEntity> findById(UUID id);

    void deleteAll();
}