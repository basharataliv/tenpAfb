DECLARE @PrimaryKeyId varchar(50);
SELECT @PrimaryKeyId = name FROM sys.objects
WHERE type = 'PK'
  AND  parent_object_id = OBJECT_ID ('CODESFL');
IF @PrimaryKeyId IS NOT NULL
	EXEC('ALTER TABLE CODESFL DROP CONSTRAINT '+@PrimaryKeyId);
--2. Add new ID PRIMARY Key Constraint
ALTER TABLE CODESFL ADD ID INT IDENTITY CONSTRAINT PK_CODESFL PRIMARY KEY CLUSTERED;