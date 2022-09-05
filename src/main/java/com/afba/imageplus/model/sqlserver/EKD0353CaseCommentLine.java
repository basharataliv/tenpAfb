package com.afba.imageplus.model.sqlserver;

import com.afba.imageplus.model.sqlserver.id.EKD0353CaseCommentLineKey;
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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "EKD0353")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
@IdClass(EKD0353CaseCommentLineKey.class)
public class EKD0353CaseCommentLine extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "CMTKEY", nullable = false)
    private Long commentKey;

    @Id
    @Column(name = "CMTSEQ", nullable = false)
    private Integer commentSequence;

    @Column(name = "COMLIN")
    private String commentLine;

    @Column(name = "TBANUM")
    private Integer tbaNum;

    @Column(name = "TBACHR")
    private String tbaChar;

    @ManyToOne
    @JoinColumn(name = "CMTKEY", insertable = false, updatable = false)
    private EKD0352CaseComment comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKD0353CaseCommentLine that = (EKD0353CaseCommentLine) o;
        return commentKey != null && Objects.equals(commentKey, that.commentKey)
                && commentSequence != null && Objects.equals(commentSequence, that.commentSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentKey, commentSequence);
    }
}
