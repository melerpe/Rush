package net.rush.util.nbt;

public class FloatTag extends Tag {

	private float value;

	public FloatTag(String name, float value) {
		super(name);
		this.value = value;
	}

	@Override
	public Float getValue() {
		return value;
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Float" + append + ": " + value;
	}
}
