package com.example.demo.service.image.exception;

public class FileStorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String msg;

    public FileStorageException(String message) {

    }

    public String getMsg() {
        return msg;
    }
}
