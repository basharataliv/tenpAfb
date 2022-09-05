
DECLARE @PrimaryKeyId varchar(50);
SELECT @PrimaryKeyId = name FROM sys.objects
WHERE type = 'PK'
  AND  parent_object_id = OBJECT_ID ('LPAPP');
IF @PrimaryKeyId IS NOT NULL
	EXEC('ALTER TABLE LPAPP DROP CONSTRAINT '+@PrimaryKeyId);

ALTER table LPAPP
    add  ID bigint IDENTITY(1,1) NOT NULL,

ALTER TABLE LPAPP
    ADD PRIMARY KEY (ID);