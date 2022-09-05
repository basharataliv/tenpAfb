package com.afba.imageplus.model.sqlserver;


import com.afba.imageplus.model.sqlserver.Enum.RecordState;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


@Data
@MappedSuperclass
public class SoftDeleteBaseEntity extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Column(name = "IS_DELETED")
    private Integer isDeleted = RecordState.ACTIVE.getId();


}

