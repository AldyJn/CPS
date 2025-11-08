package com.tecsup.petclinic.exceptions;

/**
 * Exception for PetType not found
 */
public class PetTypeNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public PetTypeNotFoundException(String message) {
        super(message);
    }
}
