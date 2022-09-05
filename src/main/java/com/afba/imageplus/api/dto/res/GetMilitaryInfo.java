package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMilitaryInfo {
    @JsonProperty("NameId")
    public int nameId;
    @JsonProperty("ServiceInfo")
    public String serviceInfo;
    @JsonProperty("Component")
    public String component;
    @JsonProperty("DutyStatus")
    public String dutyStatus;
    @JsonProperty("RankRate")
    public String rankRate;
    @JsonProperty("PayGrade")
    public String payGrade;
    @JsonProperty("CivilianSaluation")
    public String civilianSaluation;
    @JsonProperty("CivilSaluationOr")
    public String civilSaluationOr;
    @JsonProperty("OccupSpecialty")
    public String occupSpecialty;
    @JsonProperty("ProjRetPayGrade")
    public String projRetPayGrade;
    @JsonProperty("ProjRetDate")
    public int projRetDate;
    @JsonProperty("PromotionDate")
    public int promotionDate;
    @JsonProperty("PayEntryBasDate")
    public int payEntryBasDate;
    @JsonProperty("ActDutyBasDate")
    public int actDutyBasDate;
    @JsonProperty("Date1405")
    public int date1405;
    @JsonProperty("SbpElectionOpt")
    public String sbpElectionOpt;
    @JsonProperty("SbpCost")
    public int sbpCost;
    @JsonProperty("SbpPct")
    public int sbpPct;
    @JsonProperty("SbpOffsetPia")
    public String sbpOffsetPia;
    @JsonProperty("SbpAmount")
    public int sbpAmount;
}
