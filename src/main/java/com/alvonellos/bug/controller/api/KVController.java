package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.annotations.ApiPrefixController;
import com.alvonellos.bug.dto.KVDTO;
import com.alvonellos.bug.error.exceptions.WebsiteAPIException;
import com.alvonellos.bug.service.KVService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@ApiPrefixController
@RestController  // Because of SpringBoot ResponseBody implementation. We are not returning a view.
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KVController {
   
    private final KVService kvService;

    private final ObjectMapper objectMapper;

    /**
     * Retrieve a key value pair from the database
     *
     * @param id The key to retrieve from the database.
     * @return the value associated with the key.
     */
    @GetMapping("kv/{id}")
    // Could use @GetMapping instead, but this is more explicit.
    public ResponseEntity<KVDTO> get(HttpServletRequest request, @PathVariable Long id) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "get", id);
        val result = kvService.get(id);
        log.exiting(this.getClass().getName(), "get", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This is a GET all request. It is used to retrieve all data from the server, but not to modify it.
     *
     * @return all records in the database.
     */
    @GetMapping("kv")
    public ResponseEntity<List<KV>> getAll() {
        log.entering(this.getClass().getName(), "getAll");
        val result = kvService.findAll();
        log.exiting(this.getClass().getName(), "getAll", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This is a GET request with pagination. It is used to retrieve all data from the server, but not to modify it.
     * @return all records in the database, paginated.
     */
    @GetMapping("kv/{size}/{page}")
    public ResponseEntity<Page<KV>> getAll(HttpServletRequest request, @PathVariable int size, @PathVariable int page) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), String.format("getAll(%d, %d)", page, size));
        val result = kvService.getAll(page, size);
        log.exiting(this.getClass().getName(), String.format("getAll(%d, %d) = %d", page, size, (int) result.stream().count()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This is a simple SEARCH request, without pagination. It's used to retrieve all data from the server.
     * @return all records in the database
     */
    @GetMapping("kv/search")
    public ResponseEntity<List<KV>> searchByKey(HttpServletRequest request, @RequestParam String key) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "searchByKey", key);
        val result = kvService.searchByKey(key);
        log.exiting(this.getClass().getName(), "searchByKey", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get a list of all distinct keys in the database
     * @return a list of all distinct keys in the database
     */
    @GetMapping(value = "kv/keys", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getKeys() {
        log.entering(this.getClass().getName(), "getKeys");
        val result = kvService.getKeys();
        log.exiting(this.getClass().getName(), "getKeys", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This is a POST request. It is used to create a whole new record for example, customer information, file upload, etc.
     *
     * @param key   The key to store in the database.
     * @param value The value to store in the database.
     * @return the value associated with the key.
     */
    @PostMapping("kv")
    // Could use @PostMapping instead, but this is more explicit.
    public ResponseEntity<Long> post(@RequestParam String key, @RequestParam String value) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "post", key);
        val result = kvService.post(key, value);
        log.exiting(this.getClass().getName(), "post", result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * This is a PUT request. It is used to update an existing record.
     *
     * @param kv The entity to update
     * @return the value associated with the key.
     */
    @PutMapping("kv/{id}")
    // Could use @PutMapping instead, but this is more explicit.
    public ResponseEntity<Void> put(@RequestParam("id") Long id, @RequestBody @Valid KV kv) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "put", kv);
        kvService.put(kv);
        log.exiting(this.getClass().getName(), "put");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    /**
     * This is a DELETE request. It is used to delete an existing record.
     *
     * @param id The key to delete from the database.
     * @return the value associated with the key.
     */
    @DeleteMapping("kv/{id}")
    // Could use @DeleteMapping instead, but this is more explicit.
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "delete", id);
        kvService.delete(id);
        log.exiting(this.getClass().getName(), "delete");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("kv")
    // Could use @DeleteMapping instead, but this is more explicit.
    public ResponseEntity<Void> deleteAll() throws WebsiteAPIException {
        log.entering(this.getClass().getName(), "deleteAll");
        kvService.deleteAll();
        log.exiting(this.getClass().getName(), "deleteAll");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("kv/{id}")
    public ResponseEntity<Void> patch(@PathVariable("id") Long id, @RequestBody @Valid JsonPatch patch) throws WebsiteAPIException, JsonPatchException {
        log.entering(this.getClass().getName(), "patch", id);
        kvService.patch(id, patch);
        log.exiting(this.getClass().getName(), "patch");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
