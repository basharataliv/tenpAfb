package com.afba.imageplus.dto.res.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private String documentId;
    private String documentExt;
    private String documentType;
    private String spDocumentId;
    private String spDocumentUrl;
    private String spDocumentSiteId;
    private String spDocumentLibraryId;
    private String reqListDescription;
    private String userLastUpdate;
    private LocalDate dateLastUpdate;
    private LocalTime timeLastUpdate;
    private LocalDateTime scanningDateTime;
    private LocalDateTime lastUpdateDateTime;
}
