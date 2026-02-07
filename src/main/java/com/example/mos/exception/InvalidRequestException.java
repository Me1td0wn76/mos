package com.example.mos.exception;

/**
 * 無効なリクエスト例外
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
