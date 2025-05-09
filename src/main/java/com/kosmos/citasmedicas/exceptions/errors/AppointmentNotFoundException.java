package com.kosmos.citasmedicas.exceptions.errors;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(String message) {
        super(message);
    }
    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
