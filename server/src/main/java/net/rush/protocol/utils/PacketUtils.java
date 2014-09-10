package net.rush.protocol.utils;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import net.rush.model.ItemStack;
import net.rush.model.Position;
import net.rush.util.nbt.CompoundTag;
import net.rush.util.nbt.NBTInputStream;
import net.rush.util.nbt.NBTOutputStream;
import net.rush.util.nbt.Tag;

public class PacketUtils {

	/**
	 * @deprecated use {@link PacketUtils#writeItemstack(ItemStack, ByteBuf)}
	 */
	public static void writeItemstack(ItemStack item, DataOutput buf, boolean prot18) throws IOException {
		if (item == null || item.getId() <= 0) { // FIXME less then zero check
			buf.writeShort(-1);
		} else {
			buf.writeShort(item.getId());
			buf.writeByte(item.getCount());
			buf.writeShort(item.getDamage());
			if(prot18)
				buf.writeByte(item.getDataLength());
			else
				buf.writeShort(item.getDataLength());
			if (item.getDataLength() > 0) // FIXME previous check if its enchantable // TODO is is Id or datalength?
				buf.write(item.getData());

		}
	}

	public static void writeItemstack(ItemStack item, ByteBuf buf, boolean prot18) {
		if (item == null || item.getId() <= 0) { // FIXME less then zero check
			buf.writeShort(-1);
		} else {
			buf.writeShort(item.getId());
			buf.writeByte(item.getCount());
			buf.writeShort(item.getDamage());
			if(prot18)
				buf.writeByte(item.getDataLength());
			else
				buf.writeShort(item.getDataLength());
			if (item.getDataLength() > 0) // FIXME previous check if its enchantable // TODO is is Id or datalength?
				buf.writeBytes(item.getData());

		}
	}

	public static ItemStack readItemstack(ByteBuf buf, boolean prot18) {
		short id = buf.readShort();
		if (id <= 0) {
			return null;
		} else {
			byte stackSize = buf.readByte();
			short dataValue = buf.readShort();
			short dataLenght;
			if(prot18)
				dataLenght = buf.readByte();
			else
				dataLenght = buf.readShort();
			byte[] metadata = new byte[0];
			if (dataLenght >= 0 && id != 0) { // FIXME previous check if its enchantable. Since MC 1.3.2 all items except 0 (empty hand) can send this.
				metadata = new byte[dataLenght];
				buf.readBytes(metadata);
			}
			return new ItemStack(id, stackSize, dataValue);
		}
	}

	@SuppressWarnings("unchecked")
	public static void writeMetadata(ByteBuf out, MetaParam<?>[] parameters) throws IOException {
		for (MetaParam<?> parameter : parameters) {

			if (parameter == null)
				continue;

			int type = (parameter.getType() << 5 | parameter.getIndex() & 31) & 255;
			out.writeByte(type);
			
			System.out.println("Writing Meta Of Type: " +parameter.getType());

			switch (parameter.getType()) {
			case MetaParam.TYPE_BYTE:
				out.writeByte(((MetaParam<Byte>) parameter).getValue());
				break;

			case MetaParam.TYPE_SHORT:
				out.writeShort(((MetaParam<Short>) parameter).getValue());
				break;

			case MetaParam.TYPE_INT:
				out.writeInt(((MetaParam<Integer>) parameter).getValue());
				break;

			case MetaParam.TYPE_FLOAT:
				out.writeFloat(((MetaParam<Float>) parameter).getValue());
				break;

			case MetaParam.TYPE_STRING:
				writeString(((MetaParam<String>) parameter).getValue(), out, false);
				break;

			case MetaParam.TYPE_ITEM:
				ItemStack item = ((MetaParam<ItemStack>) parameter).getValue();

				if (item.getId() <= 0) {
					out.writeShort(-1);
				} else {
					out.writeShort(item.getId());
					out.writeByte(item.getCount());
					out.writeShort(item.getDamage());
					out.writeShort(-1);
					// TODO implement NBT tag writing
				}
				break;

			case MetaParam.TYPE_COORDINATE:
				Position coord = ((MetaParam<Position>) parameter).getValue();

				out.writeInt((int) coord.x);
				out.writeInt((int) coord.y);
				out.writeInt((int) coord.z);
			}
		}
		out.writeByte(127);
	}

	public static MetaParam<?>[] readMetadata(ByteBuf in) throws IOException {

		MetaParam<?>[] parameters = new MetaParam<?>[MetaParam.METADATA_SIZE];

		for (int data = in.readUnsignedByte(); data != 127; data = in.readUnsignedByte()) {
			int index = data & 0x1F;
			int type = data >> 5;

				MetaType metaType = MetaType.fromId(type);

				switch (metaType) {
				case BYTE:
					parameters[index] = new MetaParam<Byte>(type, index, in.readByte());
					break;

				case SHORT:
					parameters[index] = new MetaParam<Short>(type, index, in.readShort());
					break;

				case INT:
					parameters[index] = new MetaParam<Integer>(type, index, in.readInt());
					break;

				case FLOAT:
					parameters[index] = new MetaParam<Float>(type, index, in.readFloat());
					break;

				case STRING:
					parameters[index] = new MetaParam<String>(type, index, readString(in, 9999999, false));
					break;

				case ITEM:
					short id = in.readShort();
					if (id <= 0) {
						parameters[index] = new MetaParam<ItemStack>(type, index, null);
					} else {
						byte stackSize = in.readByte();
						short dataValue = in.readShort();
						// TODO Implement NBT tag reading
						parameters[index] = new MetaParam<ItemStack>(type, index, new ItemStack(id, stackSize, dataValue));
					}
					break;

				case POSITION:
					int x = in.readInt();
					int y = in.readInt();
					int z = in.readInt();

					parameters[index] = new MetaParam<Position>(type, index, new Position(x, y, z));

				default:
					throw new UnsupportedOperationException("Unknown metadata: " + metaType);
				}
		}
		return parameters;
	}

	public static void writeUtf8String(ByteBuf out, String str) {
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
		if (bytes.length >= 65536) {
			throw new IllegalArgumentException("Encoded UTF-8 string too long.");
		}

		out.writeShort(bytes.length);
		out.writeBytes(bytes);
	}

	public static String readUtf8String(ByteBuf in) {
		int len = in.readUnsignedShort();

		byte[] bytes = new byte[len];
		in.readBytes(bytes);

		return new String(bytes, StandardCharsets.UTF_8);
	}

	public static Map<String, Tag> readCompound(ByteBuf in) {
		int len = in.readShort();
		if (len >= 0) {
			byte[] bytes = new byte[len];
			in.readBytes(bytes);
			NBTInputStream nbtIn = null;
			try {
				nbtIn = new NBTInputStream(new ByteArrayInputStream(bytes));
				Tag tag = nbtIn.readTag();
				if (tag instanceof CompoundTag) {
					return ((CompoundTag) tag).getValue();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (nbtIn != null) {
					try {
						nbtIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public static void writeCompound(ByteBuf out, Map<String, Tag> data) {
		if (data == null) {
			out.writeShort(-1);
			return;
		}
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		NBTOutputStream str = null;
		try {
			str = new NBTOutputStream(byteout);
			str.writeTag(new CompoundTag("", data));
			str.close();
			str = null;
			out.writeShort(byteout.size());
			out.writeBytes(byteout.toByteArray());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (str != null) {
				try {
					str.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static String readString(ByteBuf in, int maxLength, boolean compatmode) throws IOException {
		if(compatmode){
			short length = in.readShort();
			if (length > maxLength) {
				throw new IOException("Received string length longer than maximum allowed (" + length + " > " + maxLength + ")");
			} else if (length < 0) {
				throw new IOException("Received string length is less than zero! Weird string!");
			} else {
				StringBuilder stringbuilder = new StringBuilder();
				for (int j = 0; j < length; ++j) {
					stringbuilder.append(in.readChar());
				}
				return stringbuilder.toString();
			}
		}
		int len = readVarInt( in);
		byte[] b = new byte[ len ];
		in.readBytes( b );

		return new String(b);
	}

	public static void writeString(String str, ByteBuf out, boolean compatmode) throws IOException {
		if(compatmode) {
			if (str.length() > 65536)
				throw new IOException("String too big");
			else {
				int len = str.length();
				out.writeShort(len);
				for (int i = 0; i < len; i++) {
					out.writeChar(str.charAt(i));
				}
				return;
			}
		}
		byte[] b = str.getBytes();
		writeVarInt( b.length, out );
		out.writeBytes(b);
	}

	/** writes X as int, Y as short and Z as int */
	public static void writePositionYShort(int x, int y, int z, ByteBuf out) {
		out.writeInt(x);
		out.writeShort(y);
		out.writeInt(z);
	}

	/** writes X as int, Y as byte and Z as int */
	public static void writePositionYByte(int x, int y, int z, ByteBuf out) {
		out.writeInt(x);
		out.writeByte(y);
		out.writeInt(z);
	}

	public static void writePositionAllByte(int x, int y, int z, ByteBuf out) {
		out.writeByte(x);
		out.writeByte(y);
		out.writeByte(z);
	}

	public static void writePositionAllDouble(double x, double y, double z, ByteBuf out) {
		out.writeDouble(x);
		out.writeDouble(y);
		out.writeDouble(z);
	}

	public static void writePositionAllIntegers(int x, int y, int z, ByteBuf out) {
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);
	}

	// Minecraft 1.7x

	public static int readVarInt(ByteBuf in) {
		int out = 0;
		int bytes = 0;
		byte input;
		while (true) {
			input = in.readByte();

			out |= (input & 0x7F) << (bytes++ * 7);

			if (bytes > 32)
				throw new RuntimeException("VarInt too big");

			if ((input & 0x80) != 0x80)
				break;
		}

		return out;
	}

	public static void writeVarInt(int value, ByteBuf out) {
		int part;
		while (true) {
			part = value & 0x7F;

			value >>>= 7;
				if (value != 0) {
					part |= 0x80;
				}

				out.writeByte(part);

				if (value == 0)
					break;
		}
	}

	// Minecraft 1.8x

	public static Position readPosition18(ByteBuf in) {
		long val = in.readLong();
		long x = val >> 38;
		long y = val << 26 >> 52;
		long z = val << 38 >> 38;

		return new Position(x, y, z);
	}

	public static void writePosition18(ByteBuf out, int x, int y, int z) {
		out.writeLong((x & 0x3FFFFFF) << 38 | (y & 0xFFF) << 26 | (z & 0x3FFFFFF));
	}

	public static UUID readUUID(ByteBuf in) {
		return new UUID(in.readLong(), in.readLong());
	}

	public static void writeUuid(ByteBuf out, UUID uuid) {
		out.writeLong(uuid.getMostSignificantBits());
		out.writeLong(uuid.getLeastSignificantBits());
	}

	public static void writeByteInteger(ByteBuf out, int value) {
		while ((value & -128) != 0) {
			out.writeByte(value & 127 | 128);
			value >>>= 7;
		}
		out.writeByte(value);
	}

	protected PacketUtils() {}

}

