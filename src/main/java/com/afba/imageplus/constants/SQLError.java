package com.afba.imageplus.constants;

public enum SQLError implements IError {

    SQL000001,
    SQL000002;

    public String code() {
        return this.toString();
    }

}
