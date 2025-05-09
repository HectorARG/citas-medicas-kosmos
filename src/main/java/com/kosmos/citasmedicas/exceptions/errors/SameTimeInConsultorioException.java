package com.kosmos.citasmedicas.exceptions.errors;

public class SameTimeInConsultorioException extends RuntimeException {
    public SameTimeInConsultorioException(String message) {
        super(message);
    }
    public SameTimeInConsultorioException(String message, Throwable cause) {
        super(message, cause);
    }
}
