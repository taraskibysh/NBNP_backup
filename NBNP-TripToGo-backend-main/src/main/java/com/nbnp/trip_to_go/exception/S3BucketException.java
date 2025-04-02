package com.nbnp.trip_to_go.exception;

public class S3BucketException extends RuntimeException {
    public S3BucketException(String message) {
        super(message);
    }
}
