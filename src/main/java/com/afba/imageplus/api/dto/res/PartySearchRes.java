package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartySearchRes {

    @JsonProperty("Name_id")
    private String name_id;
    @JsonProperty("Name_format_code")
    private String name_format_code;
    @JsonProperty("Ssn")
    private String ssn;
    @JsonProperty("Individual_prefix")
    private String individual_prefix;
    @JsonProperty("Individual_first")
    private String individual_first;
    @JsonProperty("Individual_middle")
    private String individual_middle;
    @JsonProperty("Individual_last")
    private String individual_last;
    @JsonProperty("Individual_Suffix")
    private String individual_Suffix;
    @JsonProperty("Name_business")
    private String name_business;
    @JsonProperty("Business_Prefix")
    private String business_Prefix;
    @JsonProperty("Business_Suffix")
    private String business_Suffix;
    @JsonProperty("Date_of_birth")
    private String date_of_birth;
    @JsonProperty("Sex_code")
    private String sex_code;
    @JsonProperty("Address_id")
    private String address_id;
    @JsonProperty("Line1")
    private String line1;
    @JsonProperty("Line2")
    private String line2;
    @JsonProperty("Line3")
    private String line3;
    @JsonProperty("City")
    private String city;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Zip_code")
    private String zip_code;
    @JsonProperty("Tele_num")
    private String tele_num;
    @JsonProperty("Cell_number")
    private String cell_number;
    @JsonProperty("Agent_Record_Exits")
    private String agent_Record_Exits;
    @JsonProperty("Provider_Record_Exits")
    private String provider_Record_Exits;
}