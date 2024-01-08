package com.alvonellos.website.repository;

import com.alvonellos.website.model.KVEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KVDatabase extends JpaRepository<KVEntity, Long> {
    KVEntity findByKVEntityKey(String key);

}
