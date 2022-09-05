package com.afba.imageplus.api.dto.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentSearchRes {

    @JsonProperty("AgentMasterData")
    private List<AgentMasterData> agentMasterData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentMasterData {
        @JsonProperty("CompanyCode")
        private String companyCode;
        @JsonProperty("AgentNumber")
        private String agentNumber;
        @JsonProperty("NameId")
        private String nameId;
        @JsonProperty("StatusCode")
        private String statusCode;
        @JsonProperty("ReasonCode")
        private String reasonCode;
        @JsonProperty("StatusDate")
        private String statusDate;
        @JsonProperty("Classification1")
        private String classification1;
        @JsonProperty("Classification2")
        private String classification2;
        @JsonProperty("Classification3")
        private String classification3;
        @JsonProperty("DbaState")
        private String dbaState;
        @JsonProperty("CertificationCode")
        private String certificationCode;
        @JsonProperty("ContractDate")
        private String contractDate;
        @JsonProperty("DateOfBirth")
        private String dateOfBirth;
        @JsonProperty("AutoIssueFlag")
        private String autoIssueFlag;
        @JsonProperty("OriginalTableId1")
        private String originalTableId1;
        @JsonProperty("OriginalTableId2")
        private String originalTableId2;
        @JsonProperty("OriginalTableId3")
        private String originalTableId3;
        @JsonProperty("OriginalTableId4")
        private String originalTableId4;
        @JsonProperty("OriginalTableId5")
        private String originalTableId5;
        @JsonProperty("SubsTableId1")
        private String subsTableId1;
        @JsonProperty("SubsTableId2")
        private String subsTableId2;
        @JsonProperty("SubsTableId3")
        private String subsTableId3;
        @JsonProperty("SubsTableId4")
        private String subsTableId4;
        @JsonProperty("SubsTableId5")
        private String subsTableId5;
        @JsonProperty("AgencyInd")
        private String agencyInd;
        @JsonProperty("DivisionCode")
        private String divisionCode;
        @JsonProperty("ConsoleNum")
        private String consoleNum;
        @JsonProperty("BypssPrrtChgbkS")
        private String bypssPrrtChgbkS;
        @JsonProperty("HierarchyRecordExists")
        private String hierarchyRecordExists;
        @JsonProperty("AgentMasterName")
        private AgentMasterName businessName;
        @JsonProperty("AgentMasterAddress")
        private AgentMasterAddress agentMasterAddress;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AgentMasterName {
            @JsonProperty("NameFormatCode")
            private String nameFormatCode;
//            @JsonProperty("IndividualName")
//            private String individualName;

            @JsonProperty("BusinessName")
            private BusinessName businessName;


            public static class BusinessName {
                @JsonProperty("Prefix")
                private String prefix;
                @JsonProperty("Name")
                private String name;
                @JsonProperty("Suffix")
                private String suffix;
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AgentMasterAddress {
            @JsonProperty("FaxNum")
            private String faxNum;
            @JsonProperty("TeleNum")
            private String teleNum;
            @JsonProperty("CellNumber")
            private String cellNumber;
            @JsonProperty("AddrLine1")
            private String addrLine1;
            @JsonProperty("AddrLine2")
            private String addrLine2;
            @JsonProperty("AddrLine3")
            private String addrLine3;
            @JsonProperty("BoxNumber")
            private String boxNumber;
            @JsonProperty("City")
            private String city;
            @JsonProperty("CityCode")
            private String cityCode;
            @JsonProperty("Country")
            private String country;
            @JsonProperty("CountyCode")
            private String countyCode;
            @JsonProperty("ResidentCounty")
            private String residentCounty;
            @JsonProperty("State")
            private String state;
            @JsonProperty("ZipCode")
            private String zipCode;
            @JsonProperty("ZipExtension")
            private String zipExtension;
        }
    }
}
