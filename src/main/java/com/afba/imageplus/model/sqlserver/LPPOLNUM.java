package com.afba.imageplus.model.sqlserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LPPOLNUM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class LPPOLNUM extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@NonNull
    @Column(name = "JULIAN_DATE", unique = true, updatable = true)
    private String julianDate;
	
	/* Opted for string, because this column can have
	 * leading zeroes to fill the remaining length;
	 * which is 5, as per the ticket description
	 */
	@NonNull
	@Column(name = "SEQ_NUM", length = 5, nullable = false)
	private String sequenceNumber;
}
