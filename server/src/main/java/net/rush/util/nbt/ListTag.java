package net.rush.util.nbt;

import java.util.ArrayList;
import java.util.List;

public class ListTag extends Tag {

	private Class<? extends Tag> type;

	private List<Tag> value;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ListTag(String name, Class<? extends Tag> type) {
		this(name, type, new ArrayList());
	}
	
	@SuppressWarnings("unchecked")
	public ListTag(String name, Class<? extends Tag> type, List<? extends Tag> value) {
		super(name);
		this.type = type;
		this.value = (List<Tag>) value;
	}

	public Class<? extends Tag> getType() {
		return type;
	}

	@Override
	public List<Tag> getValue() {
		return value;
	}
	
	public void add(Tag tag) {
		value.add(tag);
	}

	@Override
	public String toString() {
		String name = getName();
		String append = "";
		if (name != null && !name.equals("")) {
			append = "(\"" + getName() + "\")";
		}
		StringBuilder bldr = new StringBuilder();
		bldr.append("TAG_List" + append + ": " + value.size() + " entries of type " + NBTUtils.getTypeName(type) + "\r\n{\r\n");
		for (Tag t : value) {
			bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
		}
		bldr.append("}");
		return bldr.toString();
	}
}
