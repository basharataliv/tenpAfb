package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "EKD0352")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Builder
public class EKD0352CaseComment extends SoftDeleteBaseEntity {

    @Id
    @Column(name = "CMTKEY")
    private Long commentKey;

    @Column(name = "CASEID",nullable = false, length = 9)
    private String caseId;

    @Column(name = "CMTDAT")
    private LocalDate commentDate;

    @Column(name = "CMTTIM")
    private LocalTime commentTime;

    @Column(name = "USRID")
    private String userId;

    @Column(name = "CMTMOD")
    private String commentMode;

    @Column(name = "DOCID")
    private String documentId;

    @Column(name = "WPKGID")
    private Long wpkgId;

    @Column(name = "FCCODE")
    private String fcCode;

    @Column(name = "KEYWRD")
    private String keyWord;

    @Column(name = "CMTSTS")
    private String commentStatus;

    @Column(name = "EKDID")
    private String ekdId;

    @Column(name = "EKDIDT")
    private String ekdIdt;

    @Column(name = "TBANUM")
    private Integer tbaNum;

    @Column(name = "TBACHR")
    private String tbaChar;

    @ManyToOne
    @JoinColumn(name = "CASEID", referencedColumnName = "EKD0350_CASE_ID", insertable = false, updatable = false)
    private EKD0350Case ekdCase;

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private Set<EKD0353CaseCommentLine> commentLines;

    @Column(name = "CMT_DATETIME")
    private LocalDateTime commentDateTime;

    public void setCommentDateTime(LocalDateTime commentDateTime) {
        if (commentDateTime == null)
            commentDateTime = LocalDateTime.now();

        this.commentDateTime = commentDateTime;
        this.commentDate = commentDateTime.toLocalDate();
        this.commentTime = commentDateTime.toLocalTime();
    }

    public LocalDateTime getCommentDateTime() {
        if (this.commentDateTime == null) {
            if (this.commentDate != null && this.commentTime != null) {
                this.commentDateTime = LocalDateTime.of(this.commentDate, this.commentTime);
            }
        }
        return this.commentDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EKD0352CaseComment that = (EKD0352CaseComment) o;
        return commentKey != null && Objects.equals(commentKey, that.commentKey);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
