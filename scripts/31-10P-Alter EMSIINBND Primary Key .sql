--1. Drop already created PRIMARY Key Constraint if exists
DECLARE @PrimaryKeyId varchar(50);
SELECT @PrimaryKeyId = name FROM sys.objects
WHERE type = 'PK' 
AND  parent_object_id = OBJECT_ID ('EMSIINBND');
IF @PrimaryKeyId IS NOT NULL
	EXEC('ALTER TABLE EMSIINBND DROP CONSTRAINT '+@PrimaryKeyId);
--2. Add new ID PRIMARY Key Constraint
ALTER TABLE EMSIINBND ADD ID INT IDENTITY CONSTRAINT PK_EMSIINBND PRIMARY KEY CLUSTERED;