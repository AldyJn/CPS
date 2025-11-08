package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for PetType entity
 */
@RestController
@Slf4j
public class PetTypeController {

    private final PetTypeService petTypeService;

    public PetTypeController(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    /**
     * Get all pet types
     *
     * @return List of pet types
     */
    @GetMapping(value = "/types")
    public ResponseEntity<List<PetType>> findAllPetTypes() {
        List<PetType> petTypes = petTypeService.findAll();
        log.info("petTypes: " + petTypes);
        return ResponseEntity.ok(petTypes);
    }

    /**
     * Create pet type
     *
     * @param petType PetType to create
     * @return Created pet type
     */
    @PostMapping(value = "/types")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PetType> create(@RequestBody PetType petType) {
        PetType newPetType = petTypeService.create(petType);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPetType);
    }

    /**
     * Find pet type by id
     *
     * @param id PetType id
     * @return PetType found
     */
    @GetMapping(value = "/types/{id}")
    public ResponseEntity<PetType> findById(@PathVariable Integer id) {
        try {
            PetType petType = petTypeService.findById(id);
            return ResponseEntity.ok(petType);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update pet type
     *
     * @param petType PetType to update
     * @param id      PetType id
     * @return Updated pet type
     */
    @PutMapping(value = "/types/{id}")
    public ResponseEntity<PetType> update(@RequestBody PetType petType, @PathVariable Integer id) {
        try {
            PetType updatePetType = petTypeService.findById(id);
            updatePetType.setName(petType.getName());
            petTypeService.update(updatePetType);
            return ResponseEntity.ok(updatePetType);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete pet type by id
     *
     * @param id PetType id
     * @return Response
     */
    @DeleteMapping(value = "/types/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            petTypeService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
