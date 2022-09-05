package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FileConvertStatus {

	private final String fileName;
	private final boolean convertedToTiff;
}
