package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EKD0050")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@DynamicInsert
@DynamicUpdate
public class EKD0050NextCase extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@NonNull
	@Column(name = "NXTCAS")
	private String nextCase;
	
	@Column(name = "FILL20", nullable = true)
	private String fill;
	
}
