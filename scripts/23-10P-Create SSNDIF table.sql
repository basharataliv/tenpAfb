CREATE TABLE SSNDIF (
	id bigint DEFAULT ' ' NOT NULL,
	ssn char(9) DEFAULT ' ' NULL,
	processFlag char(1) NULL,
	createdAt datetime NULL,
	updatedAt datetime NULL
);
ALTER table SSNDIF add primary Key(id);