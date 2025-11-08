package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Specialty entity
 */
@RestController
@Slf4j
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    /**
     * Get all specialties
     *
     * @return List of specialties
     */
    @GetMapping(value = "/specialties")
    public ResponseEntity<List<Specialty>> findAllSpecialties() {
        List<Specialty> specialties = specialtyService.findAll();
        log.info("specialties: " + specialties);
        return ResponseEntity.ok(specialties);
    }

    /**
     * Create specialty
     *
     * @param specialty Specialty to create
     * @return Created specialty
     */
    @PostMapping(value = "/specialties")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Specialty> create(@RequestBody Specialty specialty) {
        Specialty newSpecialty = specialtyService.create(specialty);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialty);
    }

    /**
     * Find specialty by id
     *
     * @param id Specialty id
     * @return Specialty found
     */
    @GetMapping(value = "/specialties/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Integer id) {
        try {
            Specialty specialty = specialtyService.findById(id);
            return ResponseEntity.ok(specialty);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update specialty
     *
     * @param specialty Specialty to update
     * @param id        Specialty id
     * @return Updated specialty
     */
    @PutMapping(value = "/specialties/{id}")
    public ResponseEntity<Specialty> update(@RequestBody Specialty specialty, @PathVariable Integer id) {
        try {
            Specialty updateSpecialty = specialtyService.findById(id);
            updateSpecialty.setName(specialty.getName());
            specialtyService.update(updateSpecialty);
            return ResponseEntity.ok(updateSpecialty);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete specialty by id
     *
     * @param id Specialty id
     * @return Response
     */
    @DeleteMapping(value = "/specialties/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
