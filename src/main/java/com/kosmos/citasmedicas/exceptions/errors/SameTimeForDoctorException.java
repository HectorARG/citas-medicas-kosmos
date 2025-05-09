package com.kosmos.citasmedicas.exceptions.errors;

public class SameTimeForDoctorException extends RuntimeException {
    public SameTimeForDoctorException(String message) {
        super(message);
    }
    public SameTimeForDoctorException(String message, Throwable cause) {
        super(message, cause);
    }
}
