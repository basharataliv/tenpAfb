-- liquibase formatted sql
-- changeset Ali Niaz:PROJ-327-create-pend-doc-type-table.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select name from sysobjects where name = 'PNDDOCTYP'
create table PNDDOCTYP (
        DOCTYPE varchar(8) default ' ' not null,
        IS_DELETED int default 0,
        DOCDESC varchar(40) default ' ' not null,
        CONSTRAINT primary key (DOCTYPE)
);