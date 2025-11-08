package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Owner entity
 */
@RestController
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Get all owners
     *
     * @return List of owners
     */
    @GetMapping(value = "/owners")
    public ResponseEntity<List<Owner>> findAllOwners() {
        List<Owner> owners = ownerService.findAll();
        log.info("owners: " + owners);
        return ResponseEntity.ok(owners);
    }

    /**
     * Create owner
     *
     * @param owner Owner to create
     * @return Created owner
     */
    @PostMapping(value = "/owners")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Owner> create(@RequestBody Owner owner) {
        Owner newOwner = ownerService.create(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwner);
    }

    /**
     * Find owner by id
     *
     * @param id Owner id
     * @return Owner found
     */
    @GetMapping(value = "/owners/{id}")
    public ResponseEntity<Owner> findById(@PathVariable Long id) {
        try {
            Owner owner = ownerService.findById(id);
            return ResponseEntity.ok(owner);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update owner
     *
     * @param owner Owner to update
     * @param id    Owner id
     * @return Updated owner
     */
    @PutMapping(value = "/owners/{id}")
    public ResponseEntity<Owner> update(@RequestBody Owner owner, @PathVariable Long id) {
        try {
            Owner updateOwner = ownerService.findById(id);
            updateOwner.setFirstName(owner.getFirstName());
            updateOwner.setLastName(owner.getLastName());
            updateOwner.setAddress(owner.getAddress());
            updateOwner.setCity(owner.getCity());
            updateOwner.setTelephone(owner.getTelephone());
            ownerService.update(updateOwner);
            return ResponseEntity.ok(updateOwner);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete owner by id
     *
     * @param id Owner id
     * @return Response
     */
    @DeleteMapping(value = "/owners/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            ownerService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
