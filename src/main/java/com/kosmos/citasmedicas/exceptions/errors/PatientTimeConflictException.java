package com.kosmos.citasmedicas.exceptions.errors;

public class PatientTimeConflictException extends RuntimeException {
    public PatientTimeConflictException(String message) {
        super(message);
    }
    public PatientTimeConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
