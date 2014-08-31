package net.rush.util.nbt;

public class IntTag extends Tag {

	private int value;

	public IntTag(String name, int value) {

		super(name);
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Int" + append + ": " + value;
	}
}
