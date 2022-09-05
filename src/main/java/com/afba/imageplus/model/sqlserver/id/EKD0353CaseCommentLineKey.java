package com.afba.imageplus.model.sqlserver.id;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EKD0353CaseCommentLineKey implements Serializable {

    private Long commentKey;
    private Integer commentSequence;

}
