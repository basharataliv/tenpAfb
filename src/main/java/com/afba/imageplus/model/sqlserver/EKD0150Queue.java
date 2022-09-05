package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.annotation.QueueAuthorization;
import com.afba.imageplus.model.sqlserver.converter.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "EKD0150")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@SQLDelete(sql = "UPDATE EKD0150 SET IS_DELETED = 1 WHERE QUEID=?")
@Where(clause = "(IS_DELETED IS NULL OR IS_DELETED = 0)")
@Builder
public class EKD0150Queue extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "QUEID", nullable = false, length = 10)
    @QueueAuthorization
    private String queueId;

    @Column(name = "QUETYP", nullable = false, length = 1)
    private String queueType;

    @Column(name = "QUEDES", nullable = false, length = 30)
    private String queueDescription;

    @Column(name = "CASEDES", length = 30)
    private String caseDescription;

    @Column(name = "NOQREC")
    private Integer noQRec;

    @Column(name = "AQUEID", length = 10)
    private String alternateQueueId;

    @Column(name = "QCLASS", nullable = false, length = 3)
    private Integer queueClass;

    @Column(name = "DFLTPR", length = 1)
    private Integer dfltPr;

    @Column(name = "NXQTWK", length = 10)
    private String nextQueueToWork;

    @Column(name = "ADEPT", length = 4)
    private String aDepartmentId;

    @Column(name = "REGNID", length = 4)
    private String regionId;

    @Column(name = "FILL20", length = 20)
    private String fill20;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "IsHotQueue", length = 1)
    private Boolean isHotQueue;

    public void setQueueId(String queueId) {
        this.queueId = queueId != null ? queueId.trim() : "";
    }

    public String getQueueId() {
        return queueId != null ? queueId.trim() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        EKD0150Queue that = (EKD0150Queue) o;
        return queueId != null && Objects.equals(queueId, that.queueId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
