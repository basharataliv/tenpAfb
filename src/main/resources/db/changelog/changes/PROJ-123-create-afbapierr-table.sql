-- liquibase formatted sql
-- changeset Ali Niaz:PROJ-123-create-afbapierr-table.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select name from sysobjects where name = 'AFBAPIERR'
create table AFBAPIERR (
        ID bigint identity not null,
        AFBPGM varchar(10) default ' ' not null,
        DATEIN date default ' ' not null,
        RETCOD integer default 0 not null,
        STATUS varchar(6) default ' ' not null,
        TEXT varchar(100) default ' ' not null,
        TIMEIN integer default 0,
        USERID varchar(10) default ' ' not null,
        WAFPGM varchar(10) default ' ' not null,
        primary key (ID)
);