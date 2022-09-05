package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "EKDUSER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
public class EKDUser extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "EKDUSER_ACCOUNT_NUMBER", nullable = false, columnDefinition = "varchar(40) default ' '", length = 40)
    private String accountNumber;

    @Column(name = "EKDUSER_INDICES", nullable = false, columnDefinition = "varchar(250) default ' '", length = 250)
    private String indices;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKDUser ekdUser = (EKDUser) o;
        return accountNumber != null && Objects.equals(accountNumber, ekdUser.accountNumber);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getAccountNumber() {
        return  accountNumber != null ? accountNumber.trim() : "";
    }

    public void setAccountNumber(String accountNumber) {
      this.accountNumber=  accountNumber != null ? accountNumber.trim() : "";
    }
}
