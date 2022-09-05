package com.afba.imageplus.utilities;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvFileReaderUtil {
    public static <T> List<T> readCsv(Class<T> clazz, InputStream stream) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(clazz).withoutHeader()
                .withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR)
                .withArrayElementSeparator(CsvSchema.DEFAULT_ARRAY_ELEMENT_SEPARATOR).withNullValue(StringUtils.EMPTY)
                .withoutEscapeChar();
        return mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE).readerFor(clazz)
                .with(CsvParser.Feature.TRIM_SPACES).with(CsvParser.Feature.SKIP_EMPTY_LINES).with(schema)
                .<T>readValues(stream).readAll();
    }
}
