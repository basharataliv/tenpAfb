package com.afba.imageplus.dto.req;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetIndexPoliciesReq {

    @NotBlank(message = "Document Id cannot be null")
    String documentId;
    @NotBlank(message = "Policy or SSN cannot be null")
    String policyOrSsn;
    @NotBlank(message = "Document type cannot be null")
    @Size(message = "Document type must not exceed 8 bytes", max = 8)
    String documentType;
    @Size(message = "Document description must not exceed 26 bytes", max = 26)
    String documentDescription;
    @Size(max = 20, message = "Length of first name must be between 1-30")
    String firstName;
    @Size(max = 20, message = "Length of last name must be between 1-20")
    String lastName;
    @Size(max = 1, message = "Length of middle initial must be 1")
    String middleInitial;
    @Size(max = 4, message = "Length of policy type must not increase 4")
    String policyType;
    @Size(max = 8, message = "Length of amt paid must not increase 8")
    String amtPaid;
    @Size(max = 10, message = "Length of product id must not increase 10")
    String productId;
}
