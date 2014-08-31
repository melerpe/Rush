package net.rush.util.nbt;

/**
 * Represents a single NBT tag.
 */
public abstract class Tag {

	private String name;

	public Tag() {
		this("");
	}
	
	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Tag setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Gets the value of this tag.
	 *
	 * @return The value of this tag.
	 */
	public abstract Object getValue();
}
