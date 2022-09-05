package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ACHDIRECT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ACHDIRECT implements Serializable {

    @Id
    @Column(name = "ROUTING_NUMBER")
    private Long routingNumber;

    @Column(name = "OFFICE_ID")
    private Long officeId;

    @Column(name = "OPERATOR_ROUTE_NUM")
    private Long operatorRoutingNumber;

    @Column(name = "ROUTE_NUM_REF")
    private Long routingNumberReference;

    @Column(name = "MBR_STATUS_CODE")
    private Long memberStatusCode;

    @Column(name = "INST_TYPE")
    private Long instType;

    @Column(name = "RESERVED")
    private Long reserved;

    @Column(name = "ASSOC_NUM")
    private Long assocNumber;

    @Column(name = "LAST_UPDATE")
    private String lastUpdate;

    @Column(name = "OVERRIDE_RT_NUM")
    private String overrideRtNumber;

    @Column(name = "INSTITUTE_NAME")
    private String instituteName;

    @Column(name = "PHONE_NUMBER")
    private Long phoneNumber;

    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "RECORD_ID")
    private String recordId;

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "CCD")
    private String ccd;

    @Column(name = "CTP")
    private String ctp;

    @Column(name = "CTX")
    private String ctx;

    @Column(name = "CIE")
    private String cie;

    @Column(name = "EDICONTACT")
    private String ediContact;

    @Column(name = "EDIPHONE")
    private String ediPhone;

    @Column(name = "FILLER")
    private String filler;
}
