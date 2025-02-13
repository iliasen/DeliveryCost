package com.iliasen.delivcost.exeptions;

public class TransportOverloadedException extends RuntimeException{
    public TransportOverloadedException(String message) {
        super(message);
    }
}
