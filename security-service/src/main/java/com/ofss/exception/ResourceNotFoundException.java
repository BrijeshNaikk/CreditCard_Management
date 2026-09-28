package com.ofss.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String resourceId) {
        super(resourceName + " with ID " + resourceId + " is not present in the APP_USERS table");
    }
}
