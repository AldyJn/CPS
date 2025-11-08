package com.tecsup.petclinic.exceptions;

/**
 * Exception for Vet not found
 */
public class VetNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public VetNotFoundException(String message) {
        super(message);
    }
}
