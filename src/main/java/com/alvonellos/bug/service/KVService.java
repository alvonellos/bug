package com.alvonellos.bug.service;

import com.alvonellos.bug.dto.KVDTO;
import com.alvonellos.bug.error.exceptions.WebsiteAPIException;
import com.alvonellos.bug.error.exceptions.WebsiteIdNotFoundException;
import com.alvonellos.bug.error.exceptions.WebsiteIllegalArgumentException;
import com.alvonellos.bug.repo.dao.KVEntity;
import com.alvonellos.bug.repo.KVDatabase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Log
public class KVService {

    @Autowired
    private final KVDatabase kvDatabase;

    @Autowired
    private final ObjectMapper objectMapper;


    public KVDTO get(Long id) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "get", id);
        val result = kvDatabase.findById(id).orElseThrow(() -> new WebsiteIdNotFoundException(id));
        log.exiting(this.getClass().getName(), "get", result);
        return new KVDTO(result);
    }

    public Long post(String key, String value) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "post", key);
        val result = kvDatabase.save(new KVEntity(key, value));
        log.exiting(this.getClass().getName(), "post", result);
        return result.getKVEntityId().longValue();
    }

    public KVDTO put(KVDTO KVDTO) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "put", KVDTO);
        val result = kvDatabase
                .findById(KVDTO.getId())
                .map(kvEntity -> {
                    kvEntity.setKVEntityKey(KVDTO.getKey());
                    kvEntity.setKVEntityValue(KVDTO.getValue());
                    return kvDatabase.save(kvEntity);
                })
                .orElseThrow(() -> new WebsiteIdNotFoundException(KVDTO.getId()));
        log.exiting(this.getClass().getName(), "post", result);
        return new KVDTO(result);
    }

    public void delete(Long id) {
        log.entering(this.getClass().getName(), "delete", id);
        kvDatabase.deleteById(id);
        log.exiting(this.getClass().getName(), "delete");
    }

    public List<KVDTO> findAll() {
        return kvDatabase
                .findAll()
                .stream()
                .map(KVDTO::new)
                .collect(Collectors.toList());
    }

    public List<KVDTO> searchByKey(String key) {
        KVEntity kvEntity = new KVEntity(null, key, null);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("key", ExampleMatcher.GenericPropertyMatcher::contains)
                .withIgnorePaths("id", "value");
        Example<KVEntity> example = Example.of(kvEntity, matcher);
        return kvDatabase
                .findAll(example)
                .stream()
                .map(KVDTO::new)
                .collect(Collectors.toList());
    }

    public Page<KVDTO> findAll(int page, int size) {
        log.entering(this.getClass().getName(), "findAll", String.format("%d %d", page, size));

        Page<KVEntity> entityPage = kvDatabase.findAll(PageRequest.of(page, size));
        List<KVDTO> dtos = entityPage.getContent().stream()
                .map(KVDTO::new)
                .collect(Collectors.toList());
        log.exiting(this.getClass().getName(), "findAll", dtos.size());
        return new PageImpl<>(dtos, entityPage.getPageable(), entityPage.getTotalElements());


    }

    public List<String> getKeys() {
        log.entering(this.getClass().getName(), "getKeys");
        List<String> keys = kvDatabase.findAll().stream().map(KVEntity::getKVEntityKey).toList();
        kvDatabase.findAll().forEach(kvEntity -> log.info(kvEntity.getKVEntityKey()));
        log.exiting(this.getClass().getName(), "getKeys");
        return keys;
    }

    private KVDTO applyPatchToKV(
            JsonPatch patch, KVDTO target) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
        return objectMapper.treeToValue(patched, KVDTO.class);
    }
    public KVDTO patch(Long id, JsonPatch patch) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "patch", id);
        try {
            KVDTO KVDTO = get(id);
            KVDTO KVDTOPatched = applyPatchToKV(patch, KVDTO);
            KVDTOPatched.setId(id);
            put(KVDTOPatched);
            return KVDTOPatched;
            //could also Return 204 no content
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            throw new WebsiteIllegalArgumentException("Error patch, json");
        }  finally {
            log.exiting(this.getClass().getName(), "patch", id);
        }
    }


    public void deleteAll() {
        log.entering(this.getClass().getName(), "deleteAll");
        kvDatabase.deleteAll();
        log.exiting(this.getClass().getName(), "deleteAll");
    }
}
