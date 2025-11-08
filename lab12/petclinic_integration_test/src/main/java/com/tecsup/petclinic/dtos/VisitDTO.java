package com.tecsup.petclinic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Visit entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDTO {
    private Long id;
    private String visitDate;
    private String description;
    private Integer petId;
}
