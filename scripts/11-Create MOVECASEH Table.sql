CREATE TABLE MOVECASEH
( 
	MCCASEID CHAR    (9) NOT NULL DEFAULT ' ' ,
	MCFROMQUE CHAR    (10) NOT NULL DEFAULT ' ' ,
	MCTOQUE CHAR    (10) NOT NULL DEFAULT ' ' ,
	MCUSERID CHAR    (10) NOT NULL DEFAULT ' ' ,
	MCDATE NUMERIC (8, 0) NOT NULL DEFAULT 0 ,
	MCTIME NUMERIC (8, 0) NOT NULL DEFAULT 0 
);

ALTER TABLE MOVECASEH ADD ID bigint NOT NULL IDENTITY(1,1) PRIMARY KEY;