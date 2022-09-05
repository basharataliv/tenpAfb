-- liquibase formatted sql
-- changeset Ali Niaz:PROJ-27-create-indexing-table.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select name from sysobjects where name = 'EKD0210'
create table EKD0210 (
       EKD0210_DOCUMENT_ID varchar(12) default ' ' not null,
        EKD0210_DOCUMENT_TYPE varchar(8) default ' ' not null,
        FILLER varchar(19) default ' ' not null,
        EKD0210_INDEX_FLAG varchar(1) default 'N' not null,
        EKD0210_SCAN_DATE date default CURRENT_TIMESTAMP not null,
        EKD0210_SCANNING_REP_ID varchar(10) default ' ' not null,
        EKD0210_SCAN_TIME time default CURRENT_TIMESTAMP not null,
        primary key (EKD0210_DOCUMENT_ID, EKD0210_DOCUMENT_TYPE)
    )