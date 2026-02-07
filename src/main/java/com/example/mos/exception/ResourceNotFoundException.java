package com.example.mos.exception;

/**
 * リソースが見つからない例外
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
