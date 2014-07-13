package net.rush.util.nbt;


public class IntArrayTag extends Tag {

	private int[] value;

	public IntArrayTag(String name, int[] value) {
		super(name);
		this.value = value;
	}

	@Override
	public int[] getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder integers = new StringBuilder();
		for (int b : value) {
			integers.append(b).append(" ");
		}
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Int_Array" + append + ": " + integers.toString();
	}
}
