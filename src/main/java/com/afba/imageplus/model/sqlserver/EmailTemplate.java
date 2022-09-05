package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_TEMPLATE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class EmailTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TEMPLATE_NAME", unique = true)
    private String templateName;

    @Lob
    @Column(name = "TEMPLATE_CONTENT", nullable = false)
    private String templateContent;

    @Column(name = "EMAIL_SUBJECT", nullable = false)
    private String emailSubject;

    @Column(name = "EMAIL_TO", nullable = false)
    private String emailTo;

    @Column(name = "EMAIL_CC")
    private String emailCC;

    @Column(name = "EMAIL_BCC")
    private String emailBcc;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
}
