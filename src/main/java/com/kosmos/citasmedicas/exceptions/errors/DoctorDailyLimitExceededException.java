package com.kosmos.citasmedicas.exceptions.errors;

public class DoctorDailyLimitExceededException extends RuntimeException {
    public DoctorDailyLimitExceededException(String message) {
        super(message);
    }
    public DoctorDailyLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
