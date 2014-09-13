package net.rush.protocol.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a single parameter - this is used for things like mob metadata.
 * @param <T> The type of value this parameter contains.
 */
@AllArgsConstructor
@Getter
public class MetaParam<T> {

	/**
	 * The maximum number of parameters in each group.
	 */
	public static final int METADATA_SIZE = 32;


	public static final int TYPE_BYTE = 0;
	public static final int TYPE_SHORT = 1;
	public static final int TYPE_INT = 2;
	public static final int TYPE_FLOAT = 3;
	public static final int TYPE_STRING = 4;
	public static final int TYPE_ITEM = 5;
	public static final int TYPE_COORDINATE = 6;


	private final int type;
	private final int index;
	private final T value;

}

