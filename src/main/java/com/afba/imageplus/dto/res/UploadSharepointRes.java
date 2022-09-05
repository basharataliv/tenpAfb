package com.afba.imageplus.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadSharepointRes {

    String spDocName;
    String spDocId;
    String spDocUrl;
    String spDocLibraryId;
    String spDocSiteId;
}
