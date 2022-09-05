package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "AUTOCMTPF")
public class AUTOCMTPF extends BaseEntity{
    @Id
    @Column(name = "CMTID", columnDefinition = "varchar(4) default ' '", length = 4)
    private String commentId;

    @Column(name = "CMTTEXT", columnDefinition = "varchar(75) default ' '", length = 75)
    private String commentText;

    @Column(name = "ID3NOTE", columnDefinition = "varchar(1) default ' '", length = 1)
    private String id3Note;

    public String getCommentId() {
        return commentId != null ? commentId.trim() : "";
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId != null ? commentId.trim() : "";
    }
}
