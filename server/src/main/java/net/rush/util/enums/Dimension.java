package net.rush.util.enums;

public enum Dimension {
	NETHER(-1),
	OVERWORLD(0),
	NORMAL(0),
	END(1);
	
	int value;
	
	Dimension(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static Dimension fromId(int id) {
		for (Dimension dimension : values())
			if(dimension.value == id)
				return dimension;
		throw new NullPointerException("Unknown dimension ID " + id);
	}
}
