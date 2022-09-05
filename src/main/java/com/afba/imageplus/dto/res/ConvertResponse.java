package com.afba.imageplus.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ConvertResponse {

	private final List<FileConvertStatus> filesStatusArray;

}
