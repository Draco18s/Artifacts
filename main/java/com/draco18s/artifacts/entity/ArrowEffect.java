package com.draco18s.artifacts.entity;

public enum ArrowEffect {
	DEFAULT(0), 
	EXPLOSIVE(1);

	public final byte ID;

	private ArrowEffect(int id) {
		this.ID = ((byte)id);
	}

	public static ArrowEffect get(byte id) {
		if ((id < 0) || (id >= values().length))
			return DEFAULT;
		return values()[id];
	}
}
