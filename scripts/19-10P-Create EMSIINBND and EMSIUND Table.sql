CREATE TABLE EMSIINBND
( 
	EIPOLID CHAR    (11) NOT NULL DEFAULT ' ' ,
	EIFILEXT CHAR    (3) NOT NULL DEFAULT ' ' ,
	EIDOCID CHAR    (8) NOT NULL DEFAULT ' ' ,
	EIPAGENO NUMERIC (3, 0) NOT NULL DEFAULT 0 ,
	EIPROCFLG CHAR    (1) NOT NULL DEFAULT ' ' ,
	EIRETCODE CHAR    (1) NOT NULL DEFAULT ' ' ,
	EIPROCDAT NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	EIPROCTIM NUMERIC (6, 0) NOT NULL DEFAULT 0 
);

ALTER table EMSIINBND add primary Key(EIDOCID);

CREATE TABLE EMSIUND
( 
	RECDKEY INTEGER   IDENTITY  NOT NULL,
	COMPCD CHAR    (2) DEFAULT ' ' ,
	POLID CHAR    (12) DEFAULT NULL ,
	PRODID CHAR    (10) DEFAULT ' ' ,
	PAYMODE CHAR    (5) DEFAULT ' ' ,
	EMSISTAT CHAR    (1) DEFAULT NULL ,
	EMSIRSN CHAR    (5) DEFAULT ' ' ,
	CREATEDT CHAR    (8) DEFAULT ' ' ,
	CREATETM CHAR    (6) DEFAULT ' ' ,
	UPDATEDT CHAR    (8) DEFAULT ' ' ,
	UPDATETM CHAR    (6) DEFAULT ' ' ,
	PROCFLG CHAR    (1) NOT NULL DEFAULT 'N' ,
	FAILRSN CHAR    (100) DEFAULT ' ' ,
	POLSTAT CHAR    (1) NOT NULL DEFAULT ' ' ,
	POLRSN CHAR    (5) DEFAULT ' ' ,
	NOTE CHAR    (100) DEFAULT ' ' ,
	ISSDOCFLG CHAR    (1) NOT NULL DEFAULT 'Y' 
);

ALTER TABLE EMSIUND ADD CONSTRAINT Q_AFBALIBFIL_EMSIUND_RECDKEY_00001
	PRIMARY KEY (RECDKEY);

	
--SET IDENTITY_INSERT AFBALIBFIL.STGXMLMEMBER ON;
SET IDENTITY_INSERT EMSIUND OFF

select * from EMSIUND
-- code to reseed identity column
declare @max BIGINT
select @max=max(RECDKEY) from EMSIUND
if @max IS NULL   --check when max is returned as null
  SET @max = 0
DBCC CHECKIDENT ('EMSIUND', RESEED, @max)