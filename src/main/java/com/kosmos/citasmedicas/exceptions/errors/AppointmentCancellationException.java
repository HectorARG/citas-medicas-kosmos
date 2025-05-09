package com.kosmos.citasmedicas.exceptions.errors;

public class AppointmentCancellationException extends RuntimeException {
    public AppointmentCancellationException(String message) {
        super(message);
    }
    public AppointmentCancellationException(String message, Throwable cause) {
        super(message, cause);
    }
}
