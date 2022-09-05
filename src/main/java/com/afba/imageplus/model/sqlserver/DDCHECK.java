package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import com.afba.imageplus.model.sqlserver.converter.Numeric8ByteToLocalTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "DDCHECK")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
public class DDCHECK extends BaseEntity {

    @Id
    @Column(name = "TRANSID")
    private String transactionId;

    @Column(name = "TEMPLNAME")
    private String templateName;

    @Column(name = "NBROFPAGES")
    private Integer noOfPages;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "PROCESSFLG")
    private Boolean processFlag;

    @Column(name = "INSSSN1")
    private Long insSSN1;

    @Column(name = "INSSSN2")
    private Long insSSN2;

    @Column(name = "INSSSN3")
    private Long insSSN3;

    @Column(name = "INSSSN4")
    private Long insSSN4;

    @Column(name = "INSSSN5")
    private Long insSSN5;

    @Column(name = "INSSSN6")
    private Long insSSN6;

    @Column(name = "INSSSN7")
    private Long insSSN7;

    @Column(name = "INSSSN8")
    private Long insSSN8;

    @Column(name = "FUTUREDATE")
    private LocalDate futureDate;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "MINIT")
    private String middleInitial;

    @Column(name = "PAYORSSN")
    private Long payorSSN;

    @Column(name = "ADDR1")
    private String address1;

    @Column(name = "ADDR2")
    private String address2;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ZIPCODE")
    private Long zipCode;

    @Column(name = "RTNNBR")
    private Long rtnNumber;

    @Column(name = "BILLDAY")
    private String billDay;

    @Column(name = "ACCTTYPE")
    private String accountType;

    @Column(name = "ACCTNBR")
    private String accountNumber;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "SIGNFLAG")
    private Boolean signFlag;

    @Column(name = "SIGNDATE")
    private LocalDate signDate;

    @Column(name = "ENTRYDATE")
    private LocalDate entryDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "ENTRYTIME")
    private LocalTime entryTime;

    @Column(name = "LSTCHGDATE")
    private LocalDate lastChangeDate;

    @Convert(converter = Numeric8ByteToLocalTimeConverter.class)
    @Column(name = "LSTCHGTIME")
    private LocalTime lastChangeTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DDCHECK ddcheck = (DDCHECK) o;
        return transactionId != null && Objects.equals(transactionId, ddcheck.transactionId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
