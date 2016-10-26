package com.rambo.sdao.exception;

/**
 * Create by rambo on 2016/5/10
 **/
public class JasfException extends Exception {
    private static final long serialVersionUID = -4465294645827007440L;
    private String ecode;

    public JasfException(String code) {
        this.ecode = code;
    }

    public JasfException(String code, String message, Throwable cause) {
        super(message, cause);
        this.ecode = code;
    }

    public JasfException(String code, String message) {
        super(message);
        this.ecode = code;
    }

    public JasfException(String code, Throwable cause) {
        super(cause);
        this.ecode = code;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    public String getEcode() {
        return this.ecode;
    }
}