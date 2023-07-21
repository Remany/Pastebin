package ru.romanov.pastbin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthErrorResponse extends RuntimeException {
    private String message;
    private long timestamp;
}
