package net.rush.util.nbt;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code TAG_Compound} tag.

 */
public final class CompoundTag extends Tag {

	/**
	 * The value in tag.
	 */
	private final Map<String, Tag> valueMap;

	public CompoundTag(String name, Map<String, Tag> value) {
		super(name);
		this.valueMap = value;
	}
	
	public CompoundTag(String name) {
		this(name, new HashMap<String, Tag>());
	}
	
	public CompoundTag() {
		this("");
	}
	
	public void setBoolean(String name, boolean value) {
		setByte(name, (byte) (value ? 1 : 0));
	}

	public void setByte(String name, byte value) {
		valueMap.put(name, new ByteTag(name, value));
	}

	public void setByteArray(String name, byte values[]) {
		valueMap.put(name, new ByteArrayTag(name, values));
	}

	public void setCompoundTag(String name, CompoundTag tag) {
		tag.setName(name);
		valueMap.put(name, tag);
	}

	public void setDouble(String name, double value) {
		valueMap.put(name, new DoubleTag(name, value));
	}

	public void setFloat(String name, float value) {
		valueMap.put(name, new FloatTag(name, value));
	}

	public void setInteger(String name, int value) {
		valueMap.put(name, new IntTag(name, value));
	}

	public void setLong(String name, long value) {
		valueMap.put(name, new LongTag(name, value));
	}

	public void setShort(String name, short value) {
		valueMap.put(name, new ShortTag(name, value));
	}

	public void setString(String name, String value) {
		valueMap.put(name, new StringTag(name, value));
	}

	public void setTag(String name, Tag value) {
		value.setName(name);
		valueMap.put(name, value);
	}
	
	@Override
	public Map<String, Tag> getValue() {
		return valueMap;
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + this.getName() + "\")";
		}

		StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_Compound" + append + ": " + valueMap.size() + " entries\r\n{\r\n");
		for (Map.Entry<String, Tag> entry : valueMap.entrySet()) {
			bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}

}

