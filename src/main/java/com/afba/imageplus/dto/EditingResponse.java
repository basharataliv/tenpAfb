package com.afba.imageplus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditingResponse {

    private String queue;
    private String comment;
    private String commentCode;
    private boolean autoDecline;

}
