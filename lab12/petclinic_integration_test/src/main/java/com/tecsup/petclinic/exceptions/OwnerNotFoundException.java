package com.tecsup.petclinic.exceptions;

/**
 * Exception for Owner not found
 */
public class OwnerNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public OwnerNotFoundException(String message) {
        super(message);
    }
}
