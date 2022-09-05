package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SHAREPOINT_CONTROL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class SharepointControl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "library_name")
    private String libraryName;

    @Column(name = "library_id")
    private String libraryId;

    @Column(name = "counter")
    private Integer filesCount;

    @Column(name = "is_available")
    private Boolean isAvailable;

}
