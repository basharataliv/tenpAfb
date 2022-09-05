package com.afba.imageplus.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDto {

    private String ssn;
    private String type;
    private String relationCode;
    private String relationSymbol;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String sex;
    private String rank;
    private String grade;
    private String service;
    private String duty;
    private String rs;
    private String heightFeet;
    private String heightInches;
    private String weight;
    private String addressLine1;
    private String addressLine2;
    private String addressCity;
    private String addressState;
    private String addressCountry;
    private String addressZip;
    private String email;
    private String homePhone;
    private String workPhone;
    private String beneficiary;
    private String employmentClass;

    public PersonDto copyTo(PersonDto target) {
        target.setPrefix(prefix);
        target.setFirstName(firstName);
        target.setMiddleName(middleName);
        target.setLastName(lastName);
        target.setDateOfBirth(dateOfBirth);
        target.setSex(sex);
        target.setRank(rank);
        target.setGrade(grade);
        target.setService(service);
        target.setDuty(duty);
        target.setRs(rs);
        target.setHeightFeet(heightFeet);
        target.setHeightInches(heightInches);
        target.setWeight(weight);
        target.setAddressLine1(addressLine1);
        target.setAddressLine2(addressLine2);
        target.setAddressCity(addressCity);
        target.setAddressState(addressState);
        target.setAddressCountry(addressCountry);
        target.setAddressZip(addressZip);
        target.setEmail(email);
        target.setHomePhone(homePhone);
        target.setWorkPhone(workPhone);
        target.setBeneficiary(beneficiary);
        target.setEmploymentClass(employmentClass);
        return target;
    }

}
