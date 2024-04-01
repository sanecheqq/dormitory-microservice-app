package com.seminav.marketapp.exceptions;

public class InstanceNotFoundException extends RuntimeException{
    public InstanceNotFoundException(String message) {
        super(message);
    }
}
