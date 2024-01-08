package com.alvonellos.bug.controller.api;

import com.alvonellos.bug.annotations.ApiPrefixController;
import com.alvonellos.bug.dto.HitDTO;
import com.alvonellos.bug.service.HitService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log
@ApiPrefixController
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HitController {
    private final HitService hitService;

    /**
     * This is a GET all request. It is used to retrieve all data from the server, but not to modify it.
     *
     * @return all records in the database.
     */
    @GetMapping("hit")
    public ResponseEntity<List<HitDTO>> getAll() {
        log.entering(this.getClass().getName(), "getAll");
        val result = hitService.findAll();
        log.exiting(this.getClass().getName(), "getAll", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
