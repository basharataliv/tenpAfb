package com.afba.imageplus.constants;

public class ErrorConstants {

    private ErrorConstants() {
    }

    public static final String CASE_NOT_FOUND = "case not found";
    public static final String IMAGE_CONVERSION_FAILED = "Image conversion failed.";
    public static final String ERROR_WHILE_CONVERTING = "Error while converting: {}";
    public static final String EXCEPTION_MESSAGE = "Exception in Case Controller";
    public static final String NOT_FOUND = "Case Not Found against this id";
    public static final String CASE_CONFLICT = "Target case and existing case should not be same";
    public static final String ACCESS_TOKEN_INVALID = "Access token invalid";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String ACCESS_TOKEN_EXPIRED = "Access token expired";
    public static final String RESOURCE_NOT_FOUND = "Resource not found";

    public static final String DOCUMENT_TYPE_CAN_NOT_BE_NULL_OR_EMPTY = "Document type can not be null or empty";
    public static final String DOCUMENT_ID_INVALID_SIZE = "Document id should contain 12 characters";
    public static final String DOCUMENT_TYPE_INVALID_LENGTH = "Maximum 8 characters are allowed for document type";
    public static final String REQ_LIST_DESCRIPTION_INVALID_SIZE = "Maximum 26 characters are allowed for description";

}
