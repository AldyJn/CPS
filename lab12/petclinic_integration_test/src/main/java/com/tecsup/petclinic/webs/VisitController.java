package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Pet;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.PetNotFoundException;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.PetService;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Visit entity
 */
@RestController
@Slf4j
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    /**
     * Get all visits
     *
     * @return List of visits
     */
    @GetMapping(value = "/visits")
    public ResponseEntity<List<VisitDTO>> findAllVisits() {
        List<Visit> visits = visitService.findAll();
        List<VisitDTO> visitDTOs = visits.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        log.info("visits: " + visits);
        return ResponseEntity.ok(visitDTOs);
    }

    /**
     * Create visit
     *
     * @param visitDTO Visit to create
     * @return Created visit
     */
    @PostMapping(value = "/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visitDTO) {
        try {
            Visit visit = convertToEntity(visitDTO);
            Visit newVisit = visitService.create(visit);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(newVisit));
        } catch (PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Find visit by id
     *
     * @param id Visit id
     * @return Visit found
     */
    @GetMapping(value = "/visits/{id}")
    public ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
        try {
            Visit visit = visitService.findById(id);
            return ResponseEntity.ok(convertToDTO(visit));
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update visit
     *
     * @param visitDTO Visit to update
     * @param id       Visit id
     * @return Updated visit
     */
    @PutMapping(value = "/visits/{id}")
    public ResponseEntity<VisitDTO> update(@RequestBody VisitDTO visitDTO, @PathVariable Long id) {
        try {
            Visit updateVisit = visitService.findById(id);
            updateVisit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
            updateVisit.setDescription(visitDTO.getDescription());
            if (visitDTO.getPetId() != null) {
                Pet pet = petService.findPetById(visitDTO.getPetId());
                updateVisit.setPet(pet);
            }
            visitService.update(updateVisit);
            return ResponseEntity.ok(convertToDTO(updateVisit));
        } catch (VisitNotFoundException | PetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete visit by id
     *
     * @param id Visit id
     * @return Response
     */
    @DeleteMapping(value = "/visits/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            visitService.delete(id);
            return ResponseEntity.ok("Delete ID: " + id);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private VisitDTO convertToDTO(Visit visit) {
        return VisitDTO.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate() != null ? visit.getVisitDate().toString() : null)
                .description(visit.getDescription())
                .petId(visit.getPet() != null ? visit.getPet().getId() : null)
                .build();
    }

    private Visit convertToEntity(VisitDTO visitDTO) throws PetNotFoundException {
        Visit visit = new Visit();
        visit.setId(visitDTO.getId());
        visit.setVisitDate(LocalDate.parse(visitDTO.getVisitDate()));
        visit.setDescription(visitDTO.getDescription());
        if (visitDTO.getPetId() != null) {
            Pet pet = petService.findPetById(visitDTO.getPetId());
            visit.setPet(pet);
        }
        return visit;
    }
}
