package com.afba.imageplus.controller;

import com.afba.imageplus.dto.req.FilesConvertRequest;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.res.ConvertResponse;
import com.afba.imageplus.service.FileConvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "Endpoints for TIF conversion operations.", tags = {"Tiff Conversion Services"})
@RestController
@AllArgsConstructor
public class FileConvertController {

	FileConvertService fileConvertService;

	@ApiOperation("For Testing only, converts files from other formats to tif, within a specific folder.")
	@PostMapping("/convert")
	public BaseResponseDto<ConvertResponse> convertToTif(@RequestBody FilesConvertRequest request) {
		return fileConvertService.convertFilesToTif(request.getFiles());
	}
}