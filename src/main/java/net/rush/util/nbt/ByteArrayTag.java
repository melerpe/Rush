package net.rush.util.nbt;


public class ByteArrayTag extends Tag {

	private byte[] value;

	public ByteArrayTag(String name, byte[] value) {
		super(name);
		this.value = value;
	}

	@Override
	public byte[] getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder hex = new StringBuilder();
		for (byte b : value) {
			String hexDigits = Integer.toHexString(b).toUpperCase();
			if (hexDigits.length() == 1) {
				hex.append("0");
			}
			hex.append(hexDigits).append(" ");
		}
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		return "TAG_Byte_Array" + append + ": " + hex.toString();
	}
}
