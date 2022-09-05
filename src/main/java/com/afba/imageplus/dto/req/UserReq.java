package com.afba.imageplus.dto.req;

import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReq {

    @NotBlank(groups = {Insert.class}, message = "PolicyNumber can not be null or empty")
    @Size(groups = {Insert.class}, max = 40, message = "Length of PolicyNumber must be between 1-40")
    private String policyNumber;

    @NotBlank(groups = {Insert.class, Update.class}, message = "ssn can not be null or empty")
    @Size(groups = {Insert.class, Update.class}, min = 9, max = 9, message = "Length of ssn must be 9")
    private String ssn;

    @NotBlank(groups = {Insert.class, Update.class}, message = "FirstName can not be null or empty")
    @Size(groups = {Insert.class, Update.class}, max = 30, message = "Length of FirstName must be between 1-30")
    private String firstName;

    @NotBlank(groups = {Insert.class, Update.class}, message = "LastName can not be null or empty")
    @Size(groups = {Insert.class, Update.class}, max = 20, message = "Length of LastName must be between 1-20")
    private String lastName;

    @NotNull(groups = {Insert.class, Update.class}, message = "MiddleInitial can not be null")
    @Size(groups = {Insert.class, Update.class}, max = 1, message = "Length of MiddleInitial must be 1")
    private String middleInitial;

    @NotBlank(groups = {Insert.class, Update.class}, message = "HasImageCase can not be null or empty")
    @Size(groups = {Insert.class, Update.class}, max = 1, message = "Length of HasImageCase must be 1")
    private String hasImageCase;
}
