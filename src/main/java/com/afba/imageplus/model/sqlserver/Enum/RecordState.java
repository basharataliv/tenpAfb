package com.afba.imageplus.model.sqlserver.Enum;

public enum RecordState {
	DELETED(1), ACTIVE(0);

	private Integer id;

	RecordState(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
