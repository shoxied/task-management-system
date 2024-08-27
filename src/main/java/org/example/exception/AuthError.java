package org.example.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class AuthError {
    private int status;
    private String message;
    private Date timestamp;

    public AuthError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
