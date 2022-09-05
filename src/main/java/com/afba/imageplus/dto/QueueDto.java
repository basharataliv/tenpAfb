package com.afba.imageplus.dto;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueDto {

    @NotBlank(groups = { Insert.class }, message = "Queue ID is a required field")
    private String queueId;

    @NotBlank(groups = { Insert.class, Update.class }, message = "Queue Type is a required field")
    private String queueType;

    @NotBlank(groups = { Insert.class, Update.class }, message = "Queue Description is a required field")
    private String queueDescription;

    private Integer noQRec;

    @NotBlank(groups = { Insert.class }, message = "alternateQueueId is a required field")
    private String alternateQueueId;

    @NotNull(groups = { Insert.class, Update.class }, message = "Queue Class is a required field")
    private Integer queueClass;

    @NotNull(groups = { Insert.class, Update.class }, message = "dfltPr is a required field")
    private Integer dfltPr;

    private String nextQueueToWork;

    @JsonProperty("aDepartmentId")
    @NotBlank(groups = { Insert.class, Update.class }, message = "aDepartmentId is a required field")
    private String aDepartmentId;

    @NotBlank(groups = { Insert.class, Update.class }, message = "regionId is a required field")
    private String regionId;

    private String fill20;

    private String caseDescription;

    @NotNull(message = "isHotQueue cannot be null")
    private Boolean isHotQueue;

}
