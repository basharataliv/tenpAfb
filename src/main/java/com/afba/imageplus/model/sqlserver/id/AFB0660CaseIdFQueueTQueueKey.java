package com.afba.imageplus.model.sqlserver.id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AFB0660CaseIdFQueueTQueueKey implements Serializable {
    private String caseId;
    private String fromQueue;
    private String toQueue;

}
