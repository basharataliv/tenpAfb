package com.afba.imageplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QcRunHistoryDto {

    private String qcPolId;
    private String qcDocType;
    private String qcUserId;
    private String qcReviewer;
    private String qcQcPass;
}
