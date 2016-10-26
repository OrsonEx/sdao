package com.rambo.sdao.exception;


public class DataOptException extends JasfException {
    private static final long serialVersionUID = -5888818380838082456L;

    public DataOptException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataOptException(String code, String message) {
        super(code, message);
    }

    public DataOptException(String code, Throwable cause) {
        super(code, cause);
    }

    public DataOptException(String code) {
        super(code);
    }
}
