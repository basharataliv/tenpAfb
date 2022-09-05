-- liquibase formatted sql
-- changeset Saad Surya:PROJ-36-create-queue-profile.sql
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:NULL select name from sysobjects where name = 'EKD0150'
CREATE TABLE EKD0150
(
	QUEID VARCHAR    (10) NOT NULL,
	QUETYP VARCHAR    (1) NOT NULL,
	QUEDES VARCHAR    (30) NOT NULL,
	NOQREC NUMERIC (7, 0) NOT NULL DEFAULT 0 ,
	AQUEID VARCHAR    (10) NULL ,
	QCLASS NUMERIC (3, 0) NOT NULL ,
	DFLTPR NUMERIC (1, 0) NULL,
	NXQTWK VARCHAR    (10) NULL,
	ADEPT VARCHAR    (4) NULL,
	REGNID VARCHAR    (4) NULL,
	FILL20 VARCHAR    (20) NULL,

	CONSTRAINT EKD0150_PK PRIMARY KEY (QUEID)
);
