package com.afba.imageplus.api.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitValue {
    @JsonProperty("UvGuarCoiRate")
    private Double uvGuarCoiRate;
    @JsonProperty("UvCurrCoiRate")
    private Double uvCurrCoiRate;
    @JsonProperty("UvCurrentNar")
    private Double uvCurrentNar;
    @JsonProperty("UvTotalBasisIss")
    private Double uvTotalBasisIss;
    @JsonProperty("UvTaxBasisIssue")
    private Double uvTaxBasisIssue;
    @JsonProperty("UvEi1")
    private Double uvEi1;
    @JsonProperty("UvEi2")
    private Double uvEi2;
    @JsonProperty("UvEi3")
    private Double uvEi3;
    @JsonProperty("UvEi4")
    private Double uvEi4;
    @JsonProperty("UvEi5")
    private Double uvEi5;
    @JsonProperty("UvEi6")
    private Double uvEi6;
    @JsonProperty("UvEi7")
    private Double uvEi7;
    @JsonProperty("UvEi8")
    private Double uvEi8;
    @JsonProperty("UvEi9")
    private Double uvEi9;
    @JsonProperty("UvEi10")
    private Double uvEi10;
    @JsonProperty("UvEi11")
    private Double uvEi11;
    @JsonProperty("UvEi12")
    private Double uvEi12;
    @JsonProperty("UvAnnEi1")
    private Double uvAnnEi1;
    @JsonProperty("UvAnnEi2")
    private Double uvAnnEi2;
    @JsonProperty("UvAnnEi3")
    private Double uvAnnEi3;
    @JsonProperty("UvAnnEi4")
    private Double uvAnnEi4;
    @JsonProperty("UvAnnEi5")
    private Double uvAnnEi5;
    @JsonProperty("UvAnnEi6")
    private Double uvAnnEi6;
    @JsonProperty("UvAnnEi7")
    private Double uvAnnEi7;
    @JsonProperty("UvAnnEi8")
    private Double uvAnnEi8;
    @JsonProperty("UvAnnEi9")
    private Double uvAnnEi9;
    @JsonProperty("UvAnnEi10")
    private Double uvAnnEi10;
    @JsonProperty("UvAnnEi11")
    private Double uvAnnEi11;
    @JsonProperty("UvAnnEi12")
    private Double uvAnnEi12;
    @JsonProperty("UvTotSpecialAmt")
    private Double uvTotSpecialAmt;
    @JsonProperty("Uv10201988SpAmt")
    private Double uv10201988SpAmt;
    @JsonProperty("Uv12311986FnVal")
    private Double uv12311986FnVal;
    @JsonProperty("Uv12311988FnVal")
    private Double uv12311988FnVal;
    @JsonProperty("UvWaiverFee")
    private String uvWaiverFee;
    @JsonProperty("UvWaiverCalc")
    private String uvWaiverCalc;
    @JsonProperty("UvFillerLock")
    private String uvFillerLock;
}
