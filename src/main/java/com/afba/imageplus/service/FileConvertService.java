package com.afba.imageplus.service;

import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ConvertResponse;

import java.io.InputStream;
import java.nio.file.Path;

public interface FileConvertService {

    BaseResponseDto<ConvertResponse> convertFilesToTif(String[] files);

    Path convertFileToTif(InputStream fileToConvert, String fromExtension);

    void deleteConversionTempFolder(Path tempPath);
}
