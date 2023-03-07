package com.tcs.finalproject.bankapp.exception;

public class BankException extends Exception {
    public BankException(String errorMsg) {
        super(errorMsg);
    }

    public BankException(String errorMsg, Throwable err) {
        super(errorMsg, err);
    }
}
