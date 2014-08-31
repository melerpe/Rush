package net.rush.util.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * <p>
 * This class writes <strong>NBT</strong>, or <strong>Named Binary Tag</strong>
 * <code>Tag</code> objects to an underlying <code>OutputStream</code>.
 * </p>
 *
 * <p>
 * The NBT format was created by Markus Persson, and the specification may be
 * found at <a href="http://www.minecraft.net/docs/NBT.txt">
 * http://www.minecraft.net/docs/NBT.txt</a>.
 * </p>
 *
 */
public class NBTOutputStream implements Closeable {

	/**
	 * The output stream.
	 */
	private DataOutputStream os;

	/**
	 * Creates a new <code>NBTOutputStream</code>, which will write data to the
	 * specified underlying output stream, GZip-compressed.
	 */
	public NBTOutputStream(OutputStream os) throws IOException {
		this.os = new DataOutputStream(new GZIPOutputStream(os));
	}
	
	public NBTOutputStream(OutputStream os, boolean compressed) throws IOException {
		this.os = new DataOutputStream(compressed ? new GZIPOutputStream(os) : os);
	}

	public void writeTag(Tag tag) throws IOException {
		int type = NBTUtils.getTypeCode(tag.getClass());
		String name = tag.getName();
		byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);

		os.writeByte(type);
		os.writeShort(nameBytes.length);
		os.write(nameBytes);

		if (type == NBTConstants.TYPE_END)
			throw new IOException("Named TAG_End not permitted.");

		writeTagPayload(tag);
	}

	private void writeTagPayload(Tag tag) throws IOException {
		int type = NBTUtils.getTypeCode(tag.getClass());
		switch (type) {
		case NBTConstants.TYPE_END:
			writeEndTagPayload((EndTag) tag);
			break;
		case NBTConstants.TYPE_BYTE:
			writeByteTagPayload((ByteTag) tag);
			break;
		case NBTConstants.TYPE_SHORT:
			writeShortTagPayload((ShortTag) tag);
			break;
		case NBTConstants.TYPE_INT:
			writeIntTagPayload((IntTag) tag);
			break;
		case NBTConstants.TYPE_LONG:
			writeLongTagPayload((LongTag) tag);
			break;
		case NBTConstants.TYPE_FLOAT:
			writeFloatTagPayload((FloatTag) tag);
			break;
		case NBTConstants.TYPE_DOUBLE:
			writeDoubleTagPayload((DoubleTag) tag);
			break;
		case NBTConstants.TYPE_BYTE_ARRAY:
			writeByteArrayTagPayload((ByteArrayTag) tag);
			break;
		case NBTConstants.TYPE_STRING:
			writeStringTagPayload((StringTag) tag);
			break;
		case NBTConstants.TYPE_LIST:
			writeListTagPayload((ListTag) tag);
			break;
		case NBTConstants.TYPE_COMPOUND:
			writeCompoundTagPayload((CompoundTag) tag);
			break;
		case NBTConstants.TYPE_INT_ARRAY:
			writeIntArrayTagPayload((IntArrayTag) tag);
			break;
		default:
			throw new IOException("Invalid tag type: " + type + ".");
		}
	}

	private void writeByteTagPayload(ByteTag tag) throws IOException {
		os.writeByte(tag.getValue());
	}

	private void writeByteArrayTagPayload(ByteArrayTag tag) throws IOException {
		byte[] bytes = tag.getValue();
		os.writeInt(bytes.length);
		os.write(bytes);
	}

	/**
	 * Writes a <code>TAG_Compound</code> tag.
	 *
	 * @param tag
	 *            The tag.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private void writeCompoundTagPayload(CompoundTag tag) throws IOException {

		for (Tag childTag : tag.getValue().values()) {
			writeTag(childTag);
		}
		os.writeByte((byte) 0); // end tag - better way?
	}

	private void writeListTagPayload(ListTag tag) throws IOException {
		Class<? extends Tag> clazz = tag.getType();
		List<Tag> tags = tag.getValue();
		int size = tags.size();

		os.writeByte(NBTUtils.getTypeCode(clazz));
		os.writeInt(size);
		for (int i = 0; i < size; i++) {
			writeTagPayload(tags.get(i));
		}
	}

	private void writeStringTagPayload(StringTag tag) throws IOException {
		byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
		os.writeShort(bytes.length);
		os.write(bytes);
	}

	private void writeDoubleTagPayload(DoubleTag tag) throws IOException {
		os.writeDouble(tag.getValue());
	}

	private void writeFloatTagPayload(FloatTag tag) throws IOException {
		os.writeFloat(tag.getValue());
	}

	private void writeLongTagPayload(LongTag tag) throws IOException {
		os.writeLong(tag.getValue());
	}

	private void writeIntTagPayload(IntTag tag) throws IOException {
		os.writeInt(tag.getValue());
	}

	private void writeShortTagPayload(ShortTag tag) throws IOException {
		os.writeShort(tag.getValue());
	}

	private void writeIntArrayTagPayload(IntArrayTag tag) throws IOException {
		int[] ints = tag.getValue();
		os.writeInt(ints.length);
		for (int i = 0; i < ints.length; i++) {
			os.writeInt(ints[i]);
		}
	}

	private void writeEndTagPayload(EndTag tag) {
		/* empty */
	}

	@Override
	public void close() throws IOException {
		os.close();
	}
}
