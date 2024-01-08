package com.alvonellos.bug.repo;

import com.alvonellos.bug.repo.dao.KVEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KVDatabase extends JpaRepository<KVEntity, Long> {
    KVEntity findByKVEntityKey(String key);

}
