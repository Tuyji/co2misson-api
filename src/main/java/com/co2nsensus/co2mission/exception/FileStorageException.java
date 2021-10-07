package com.co2nsensus.co2mission.exception;

public class FileStorageException extends AffiliateCustomInternalServerException {

    public FileStorageException(String code, String message) {
        super(code, message);
    }

    public FileStorageException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}