package net.rush.util.nbt;

public class EndTag extends Tag {

	private Object value = null;

	public EndTag() {
		super("");
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "TAG_End";
	}
}
