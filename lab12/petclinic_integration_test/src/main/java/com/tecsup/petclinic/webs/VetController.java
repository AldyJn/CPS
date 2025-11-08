package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetController {

    private final VetService vetService;

    public VetController(VetService vetService) {
        this.vetService = vetService;
    }

    @GetMapping(value = "/vets")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Vet>> findAllVets() {
        List<Vet> vets = vetService.findAll();
        // Initialize lazy-loaded specialties
        vets.forEach(vet -> {
            if (vet.getSpecialties() != null) {
                vet.getSpecialties().size();
            }
        });
        log.info("vets: " + vets);
        return ResponseEntity.ok(vets);
    }

    @PostMapping(value = "/vets")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vet> create(@RequestBody Vet vet) {
        Vet newVet = vetService.create(vet);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVet);
    }

    @GetMapping(value = "/vets/{id}")
    public ResponseEntity<Vet> findById(@PathVariable Integer id) {
        try {
            Vet vet = vetService.findById(id);
            return ResponseEntity.ok(vet);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/vets/{id}")
    public ResponseEntity<Vet> update(@RequestBody Vet vet, @PathVariable Integer id) {
        try {
            Vet updateVet = vetService.findById(id);
            updateVet.setFirstName(vet.getFirstName());
            updateVet.setLastName(vet.getLastName());
            updateVet.setSpecialties(vet.getSpecialties());
            vetService.update(updateVet);
            return ResponseEntity.ok(updateVet);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/vets/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
