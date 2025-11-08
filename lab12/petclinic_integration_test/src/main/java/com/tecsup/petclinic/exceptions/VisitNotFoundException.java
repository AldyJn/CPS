package com.tecsup.petclinic.exceptions;

/**
 * Exception for Visit not found
 */
public class VisitNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public VisitNotFoundException(String message) {
        super(message);
    }
}
