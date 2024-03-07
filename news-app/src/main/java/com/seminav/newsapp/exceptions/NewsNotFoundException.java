package com.seminav.newsapp.exceptions;

public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String message) {
        super(message);
    }
}
