ALTER TABLE EKD0360 ADD CLOSEFL char(1) COLLATE SQL_Latin1_General_CP1_CI_AS CONSTRAINT DF_EKD0360_CLOSEFL DEFAULT ' ' NOT NULL;
ALTER TABLE EKD0360 ADD IS_ADMIN char(1) COLLATE SQL_Latin1_General_CP1_CI_AS CONSTRAINT DF_EKD0360_IS_ADMIN DEFAULT ' ' NOT NULL;

ALTER TABLE EKD0260 ADD EKD0260_INDEX_FLAG CHAR(1) CONSTRAINT DF_EKD0260_EKD0260_INDEX_FLAG DEFAULT ' ' NOT NULL;

ALTER TABLE EKD0310 ADD EKD0310_SP_DOC_ID varchar(255);
ALTER TABLE EKD0310 ADD EKD0310_SP_DOC_LIBRARY_ID varchar(255);
ALTER TABLE EKD0310 ADD EKD0310_SP_DOC_SITE_ID varchar(255);
ALTER TABLE EKD0310 ADD EKD0310_SP_DOC_URL varchar(255);
ALTER TABLE EKD0310 ADD EKD0310_DOCUMENT_EXT varchar(10);

-- Increased COMLIN length from 75 to 1000 as per the discussion and requirement.
ALTER TABLE EKD0353 ALTER COLUMN COMLIN CHAR(1000) NOT NULL;