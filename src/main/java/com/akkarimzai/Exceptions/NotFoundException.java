package com.akkarimzai.Exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(String name, Object key) {
        super(String.format("{%s} (%s) not found", name, key));
    }
}
