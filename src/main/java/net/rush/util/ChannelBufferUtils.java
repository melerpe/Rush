package net.rush.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import net.rush.model.Coordinate;
import net.rush.model.Item;
import net.rush.util.nbt.CompoundTag;
import net.rush.util.nbt.NBTInputStream;
import net.rush.util.nbt.NBTOutputStream;
import net.rush.util.nbt.Tag;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Contains several {@link ChannelBuffer}-related utility methods.

 */
public final class ChannelBufferUtils {

	/**
	 * The UTF-8 character set.
	 */
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	
	/**
	* The bit flag indicating a varint continues.
	*/
	public static final byte VARINT_MORE_FLAG = (byte) (1 << 7);

	/**
	 * Writes a list of parameters (e.g. mob metadata) to the buffer.
	 * @param buf The buffer.
	 * @param parameters The parameters.
	 */
	@SuppressWarnings("unchecked")
	public static void writeParameters(ChannelBuffer buf, Parameter<?>[] parameters) {
		for (Parameter<?> parameter : parameters) {
			if (parameter == null)
				continue;

			int type  = parameter.getType();
			int index = parameter.getIndex();

			buf.writeByte(((type & 0x07) << 5) | (index & 0x1F));

			switch (type) {
			case Parameter.TYPE_BYTE:
				buf.writeByte(((Parameter<Byte>) parameter).getValue());
				break;
			case Parameter.TYPE_SHORT:
				buf.writeShort(((Parameter<Short>) parameter).getValue());
				break;
			case Parameter.TYPE_INT:
				buf.writeInt(((Parameter<Integer>) parameter).getValue());
				break;
			case Parameter.TYPE_FLOAT:
				buf.writeFloat(((Parameter<Float>) parameter).getValue());
				break;
			case Parameter.TYPE_STRING:
				writeString(buf, ((Parameter<String>) parameter).getValue());
				break;
			case Parameter.TYPE_ITEM:
				Item item = ((Parameter<Item>) parameter).getValue();
				buf.writeShort(item.getId());
				buf.writeByte(item.getCount());
				buf.writeShort(item.getDamage());
				break;
			case Parameter.TYPE_COORDINATE:
				Coordinate coord = ((Parameter<Coordinate>) parameter).getValue();
				buf.writeInt(coord.getX());
				buf.writeInt(coord.getY());
				buf.writeInt(coord.getZ());
				break;
			}
		}

		buf.writeByte(0x7F);
	}

	/**
	 * Reads a list of parameters from the buffer.
	 * @param buf The buffer.
	 * @return The parameters.
	 */
	public static Parameter<?>[] readParameters(ChannelBuffer buf) {
		Parameter<?>[] parameters = new Parameter<?>[Parameter.METADATA_SIZE];

		int b;
		while ((b = buf.readUnsignedByte()) != 0x7F) {
			int type  = (b >> 5) & 0x07;
			int index = b & 0x1F;

			switch (type) {
			case Parameter.TYPE_BYTE:
				parameters[index] = new Parameter<Byte>(type, index, buf.readByte());
				break;
			case Parameter.TYPE_SHORT:
				parameters[index] = new Parameter<Short>(type, index, buf.readShort());
				break;
			case Parameter.TYPE_INT:
				parameters[index] = new Parameter<Integer>(type, index, buf.readInt());
				break;
			case Parameter.TYPE_FLOAT:
				parameters[index] = new Parameter<Float>(type, index, buf.readFloat());
				break;
			case Parameter.TYPE_STRING:
				parameters[index] = new Parameter<String>(type, index, readString(buf));
				break;
			case Parameter.TYPE_ITEM:
				int id = buf.readShort();
				int count = buf.readByte();
				int damage = buf.readShort();
				Item item = new Item(id, count, damage);
				parameters[index] = new Parameter<Item>(type, index, item);
				break;
			case Parameter.TYPE_COORDINATE:
				int x = buf.readInt();
				int y = buf.readInt();
				int z = buf.readInt();
				Coordinate coordinate = new Coordinate(x, y, z);
				parameters[index] = new Parameter<Coordinate>(type, index, coordinate);
				break;
			}
		}

		return parameters;
	}

	/**
	 * Writes a string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long
	 * <em>after</em> it is encoded.
	 */
	public static void writeString(ChannelBuffer buf, String str) {
		int len = str.length();
		if (len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}

		buf.writeShort(len);
		for (int i = 0; i < len; i++) {
			buf.writeChar(str.charAt(i));
		}
	}

	/**
	 * Writes a UTF-8 string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long
	 * <em>after</em> it is encoded.
	 */
	public static void writeUtf8String(ChannelBuffer buf, String str) {
		byte[] bytes = str.getBytes(CHARSET_UTF8);
		if (bytes.length >= 65536) {
			throw new IllegalArgumentException("Encoded UTF-8 string too long.");
		}

		buf.writeShort(bytes.length);
		buf.writeBytes(bytes);
	}

	/**
	 * Reads a string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readString(ChannelBuffer buf) {
		int len = buf.readUnsignedShort();

		char[] characters = new char[len];
		for (int i = 0; i < len; i++) {
			characters[i] = buf.readChar();
		}

		return new String(characters);
	}


	/**
	 * Reads a UTF-8 encoded string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readUtf8String(ChannelBuffer buf) {
		int len = buf.readUnsignedShort();

		byte[] bytes = new byte[len];
		buf.readBytes(bytes);

		return new String(bytes, CHARSET_UTF8);
	}

	/**
	 * Read a protobuf varint from the buffer.
	 * @param buf The buffer.
	 * @return The value read.
	 */
	public static int readVarInt(ChannelBuffer buf) {
		int ret = 0;
		short read;
		byte offset = 0;
		do {
			read = buf.readUnsignedByte();
			ret = ret | ((read & ~VARINT_MORE_FLAG) << offset);
			offset += 7;
		} while (((read >> 7) & 1) != 0);
		return ret;
	}

	/**
	 * Write a protobuf varint to the buffer.
	 * @param buf The buffer.
	 * @param num The value to write.
	 */
	public static void writeVarInt(ChannelBuffer buf, int num) {
		do {
			short write = (short) (num & ~VARINT_MORE_FLAG);
			num >>= 7;
		if (num != 0) {
			write |= VARINT_MORE_FLAG;
		}
		buf.writeByte(write);
		} while (num != 0);
	}
	
    public static Map<String, Tag> readCompound(ChannelBuffer buf) {
        int len = buf.readShort();
        if (len >= 0) {
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            NBTInputStream str = null;
            try {
                str = new NBTInputStream(new ByteArrayInputStream(bytes));
                Tag tag = str.readTag();
                if (tag instanceof CompoundTag) {
                    return ((CompoundTag) tag).getValue();
                }
            } catch (IOException e) {
            } finally {
                if (str != null) {
                    try {
                        str.close();
                    } catch (IOException e) {}
                }
            }
        }
        return null;
    }

    public static void writeCompound(ChannelBuffer buf, Map<String, Tag> data) {
        if (data == null) {
            buf.writeShort(-1);
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        NBTOutputStream str = null;
        try {
            str = new NBTOutputStream(out);
            str.writeTag(new CompoundTag("", data));
            str.close();
            str = null;
            buf.writeShort(out.size());
            buf.writeBytes(out.toByteArray());
        } catch (IOException e) {
        } finally {
            if (str != null) {
                try {
                    str.close();
                } catch (IOException e) {}
            }
        }

    }

    public static void writeBoolean(ChannelBuffer buf, boolean bool) {
    	buf.writeByte(bool ? 1 : 0);
    }
    
	/**
	 * Default private constructor to prevent instantiation.
	 */
	private ChannelBufferUtils() {}

}

