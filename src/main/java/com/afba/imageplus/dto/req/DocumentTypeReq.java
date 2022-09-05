package com.afba.imageplus.dto.req;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Convert;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DocumentTypeReq {

    @NotBlank(message = "documentType cannot be blank", groups = { Insert.class, Update.class })
    @Size(max = 8, message = "documentType cannot exceeed 8 characters", groups = { Insert.class, Update.class })
    private String documentType;

    @NotBlank(message = "documentDescription cannot be blank", groups = { Insert.class, Update.class })
    @Size(max = 40, message = "documentDescription cannot exceeed 40 characters", groups = { Insert.class,
            Update.class })
    private String documentDescription;

    @NotNull(message = "defaultSuspendDays cannot be null", groups = { Insert.class, Update.class })
    @Min(value = 1, message = "Length of defaultSuspendDays should at least be 1", groups = { Insert.class,
            Update.class })
    private Integer defaultSuspendDays;

    @NotNull(message = "createNewCase cannot be null", groups = { Insert.class, Update.class })
    @Pattern(regexp = "^[1,2,3]$", message = "createNewCase should be 1,2 or 3", groups = { Insert.class,
            Update.class })
    private String createNewCase;

    @NotBlank(message = "defaultQueueId cannot be null", groups = { Insert.class, Update.class })
    @Size(max = 10, message = "defaultQueueId cannot exceeed 10 characters", groups = { Insert.class, Update.class })
    private String defaultQueueId;

    @NotNull(message = "securityClass cannot be null", groups = { Insert.class, Update.class })
    @Pattern(regexp = "[0-9]{1,}$", message = "securityClass must be a number of minimum length 1", groups = {
            Insert.class, Update.class })
    private String securityClass;

    @NotNull(message = "allowImpA cannot be null", groups = { Insert.class, Update.class })
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean allowImpA;

    @NotNull(message = "isAppsDoc cannot be null", groups = { Insert.class, Update.class })
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAppsDoc;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean dasdControlFlag;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean olsStoreFlag;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean outputRequired;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean informRetrievalDept;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean indexScanFlag;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean requestSuspendFlag;

    @JsonIgnore
    private LocalDate dateLastUpdate = LocalDate.now();

    @JsonIgnore
    private LocalTime timeLastUpdate = LocalTime.now();

    @JsonIgnore
    private String userLastUpdate;

    @Min(value = 1, message = "Length of noInBatch should at least be 1", groups = { Insert.class, Update.class })
    private Integer noInBatch;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean requestInputFlag;

    @Min(value = 1, message = "Length of retentionPeriod should at least be 1", groups = { Insert.class, Update.class })
    private Integer retentionPeriod;

    @Size(max = 2, message = "Length of inpdTypeA can either be 0 or 2", groups = { Insert.class, Update.class })
    private String inpdTypeA;

    @Pattern(regexp = "[0-9]{1,}$", message = "faxPSizeA must be a number of minimum length 1", groups = { Insert.class,
            Update.class })
    private String faxPSizeA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwannflA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean alwredflA;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean allowOcr;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean confirmAll;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean workstationOnly;

    @Convert(converter = BooleanToYNConverter.class)
    private Boolean matchCp;

    @Range(min = 1, max = 9, message = "Length of storeMethod must be 1", groups = { Insert.class, Update.class })
    private Integer storeMethod;

    private String storeClass;

    @Size(max = 1, message = "Length of optSysId can either be 0 or 1", groups = { Insert.class, Update.class })
    private String optSysId;

    @Range(min = 1, max = 9, message = "Length of lan3995 must be 1", groups = { Insert.class, Update.class })
    private Integer lan3995;

    @JsonIgnore
    private LocalDateTime lastUpdateDateTime = LocalDateTime.now();

    @Size(max = 3, message = "productCode cannot exceeed 3 characters", groups = { Insert.class, Update.class })
    private String productCode;
}
