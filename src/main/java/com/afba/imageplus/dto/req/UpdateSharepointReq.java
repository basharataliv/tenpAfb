package com.afba.imageplus.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSharepointReq {

    @NotEmpty(message = "siteId cannot be null")
    String siteId;
    @NotEmpty(message = "libraryId cannot be null")
    String libraryId;
    @NotEmpty(message = "spDocId cannot be null")
    String spDocId;
    @NotEmpty(message = "docId cannot be null")
    String docId;
    MultipartFile doc;
}
