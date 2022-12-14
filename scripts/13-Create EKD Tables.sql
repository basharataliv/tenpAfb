-- noinspection SqlDialectInspectionForFile

CREATE TABLE ACHDIRECT
(
    ROUTING_NUMBER NUMERIC (9, 0) NOT NULL DEFAULT 0 ,
    OFFICE_ID NUMERIC (4, 0) NOT NULL DEFAULT 0 ,
    OPERATOR_ROUTE_NUM NUMERIC (9, 0) NOT NULL DEFAULT 0 ,
    ROUTE_NUM_REF NUMERIC (1, 0) NOT NULL DEFAULT 0 ,
    MBR_STATUS_CODE NUMERIC (1, 0) NOT NULL DEFAULT 0 ,
    INST_TYPE NUMERIC (1, 0) NOT NULL DEFAULT 0 ,
    RESERVED NUMERIC (1, 0) NOT NULL DEFAULT 0 ,
    ASSOC_NUM NUMERIC (2, 0) NOT NULL DEFAULT 0 ,
    LAST_UPDATE CHAR    (6) NOT NULL DEFAULT ' ' ,
    OVERRIDE_RT_NUM CHAR    (9) NOT NULL DEFAULT ' ' ,
    INSTITUTE_NAME CHAR    (26) NOT NULL DEFAULT ' ' ,
    PHONE_NUMBER NUMERIC (10, 0) NOT NULL DEFAULT 0 ,
    MAIL_ADDRESS CHAR    (23) NOT NULL DEFAULT ' ' ,
    CITY CHAR    (16) NOT NULL DEFAULT ' ' ,
    STATE CHAR    (2) NOT NULL DEFAULT ' ' ,
    ZIP_CODE CHAR    (9) NOT NULL DEFAULT ' ' ,
    RECORD_ID CHAR    (1) NOT NULL DEFAULT ' ' ,
    CONTACT_NAME CHAR    (20) NOT NULL DEFAULT ' ' ,
    CCD CHAR    (1) NOT NULL DEFAULT ' ' ,
    CTP CHAR    (1) NOT NULL DEFAULT ' ' ,
    CTX CHAR    (1) NOT NULL DEFAULT ' ' ,
    CIE CHAR    (1) NOT NULL DEFAULT ' ' ,
    EDICONTACT CHAR    (20) NOT NULL DEFAULT ' ' ,
    EDIPHONE CHAR    (10) NOT NULL DEFAULT ' ' ,
    FILLER CHAR    (16) NOT NULL DEFAULT ' '
);


CREATE TABLE CREDITHIS
(
    SSN NUMERIC (9, 0) NOT NULL DEFAULT 0 ,
    COMPCODE CHAR    (1) NOT NULL DEFAULT ' ' ,
    POLID CHAR    (20) NOT NULL DEFAULT ' ' ,
    DOLLARAMT NUMERIC (7, 2) NOT NULL DEFAULT 0 ,
    RECUR CHAR    (1) NOT NULL DEFAULT ' ' ,
    PRODDESC CHAR    (40) NOT NULL DEFAULT ' ' ,
    RC CHAR    (1) NOT NULL DEFAULT ' ' ,
    DATEIN NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
    TIMEIN NUMERIC (8, 0) NOT NULL DEFAULT 0
);