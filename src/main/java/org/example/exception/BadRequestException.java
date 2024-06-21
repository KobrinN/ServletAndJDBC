package org.example.exception;

public class BadRequestException extends RuntimeException{
    private final int status;

    public BadRequestException(String message) {
        super(message);
        this.status = 400;
    }

    public int getStatus() {
        return status;
    }
}
