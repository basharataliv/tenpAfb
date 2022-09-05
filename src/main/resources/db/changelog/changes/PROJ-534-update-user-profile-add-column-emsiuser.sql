-- liquibase formatted sql
-- changeset Ali Niaz:PROJ-534-update-user-profile-add-column-emsiuser.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select c.name from sys.columns c join sys.tables t on c.object_id = t.object_id WHERE t.name = 'EKD0360' and c.name = 'EMSIUSER'
ALTER TABLE EKD0360 ADD COLUMN EMSIUSER VARCHAR(1) DEFAULT 'N' NOT NULL;