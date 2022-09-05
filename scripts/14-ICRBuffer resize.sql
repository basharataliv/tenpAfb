DECLARE @DROP_CONSTRAINT_QUERY varchar(max);
select @DROP_CONSTRAINT_QUERY = 'ALTER TABLE ' + t.name + ' DROP CONSTRAINT '+ d.name
from sys.tables t join sys.default_constraints d on d.parent_object_id = t.object_id join sys.columns c on c.object_id = t.object_id and c.column_id = d.parent_column_id
where c.name in ('ICR_BUFFER') order by t.name

IF @DROP_CONSTRAINT_QUERY IS NOT NULL
	EXEC (@DROP_CONSTRAINT_QUERY);

ALTER TABLE ICRFILE ALTER COLUMN ICR_BUFFER VARCHAR(MAX) NOT NULL;
ALTER TABLE ICRFILE ADD CONSTRAINT DF_ICR_BUFFER DEFAULT ' ' FOR ICR_BUFFER;

