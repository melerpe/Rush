package net.rush.util.nbt;

public class LongTag extends Tag {

	private long value;

	public LongTag(String name, long value) {
		super(name);
		this.value = value;
	}

	@Override
	public Long getValue() {
		return value;
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Long" + append + ": " + value;
	}
}
