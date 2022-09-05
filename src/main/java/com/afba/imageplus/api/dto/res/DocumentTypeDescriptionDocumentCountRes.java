package com.afba.imageplus.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentTypeDescriptionDocumentCountRes  implements Serializable {
    private String documentType;
    private String documentDescription;
    private String count;
}
