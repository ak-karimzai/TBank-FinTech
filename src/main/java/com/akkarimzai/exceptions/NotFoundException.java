package com.akkarimzai.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(String name, Object key) {
        super(String.format("{%s} (%s) not found", name, key));
    }
}
