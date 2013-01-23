package com.sras.datamodel.exceptions;

/**
 * User: dsonti
 * Date: Jan 12, 2011
 * Time: 6:52:21 PM
 */
public class DuplicateKeyException extends Exception {

    public String getCode() {
        return code;
    }

    private String code;

    public DuplicateKeyException(String code, String description) {
        super(description);
        this.code = code;
    }

}
