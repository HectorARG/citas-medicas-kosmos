package com.kosmos.citasmedicas.exceptions.errors;

public class AppointmentEditException extends RuntimeException {
    public AppointmentEditException(String message) {
        super(message);
    }
    public AppointmentEditException(String message, Throwable cause) {
        super(message, cause);
    }
}
