-- liquibase formatted sql
-- changeset Ali Niaz:PROJ-327-create-pend-case-table.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select name from sysobjects where name = 'EKD0370'
create table EKD0370 (
       EKD0370_CASE_ID varchar(9) default ' ' not null,
        IS_DELETED int,
        EKD0370_DOCUMENT_TYPE varchar(8) default ' ',
        FIL11 varchar(20) default ' ',
        EKD0370_IDENTIFIER varchar(40) default ' ' not null,
        EKD0370_INPUT_FIELD_1 varchar(20) default ' ',
        EKD0370_INPUT_FIELD_2 varchar(20) default ' ',
        EKD0370_INPUT_FIELD_3 varchar(20) default ' ',
        EKD0370_INPUT_FIELD_4 varchar(20) default ' ',
        EKD0370_INPUT_FIELD_5 varchar(20) default ' ',
        EKD0370_LAST_REP_ID varchar(10) default ' ' not null,
        EKD0370_MEDIA_MATCH_FLAG varchar(1) default ' ',
        EKD0370_PEND_DATE Date default ' ' not null,
        EKD0370_PEND_TIME time default ' ' not null,
        EKD0370_PRINT_REQUEST_FLAG varchar(1) default ' ',
        EKD0370_RELEASE_DATE Date default ' ' not null,
        EKD0370_REQ_LIST_DESCRIPTION varchar(40) default ' ',
        EKD0370_RETURN_QUEUE varchar(10) default ' ' not null,
        primary key (EKD0370_CASE_ID)
);