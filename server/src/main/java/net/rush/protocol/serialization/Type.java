package net.rush.protocol.serialization;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.rush.model.ItemStack;
import net.rush.model.Position;
import net.rush.protocol.serialization.Serialization.Serializor;
import net.rush.protocol.utils.MetaParam;
import net.rush.protocol.utils.PacketUtils;
import net.rush.util.JsonUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public enum Type {
	
	INT(new Serializor<Integer>() {
		@Override
		public Integer read(ByteBuf in) throws IOException {
			return in.readInt();
		}

		@Override
		public void write(ByteBuf out, Integer val) throws IOException {
			out.writeInt(val);
		}
	}), NULL_INT(new Serializor<Integer>() {
		@Override
		public Integer read(ByteBuf in) throws IOException {
			return in.readInt();
		}

		@Override
		public void write(ByteBuf out, Integer val) throws IOException {
			if(val != -1)
				out.writeInt(val);
		}
	}), BYTE(new Serializor<Byte>() {
		@Override
		public Byte read(ByteBuf in) throws IOException {
			return in.readByte();
		}

		@Override
		public void write(ByteBuf out, Byte val) throws IOException {
			out.writeByte(val);
		}
	}), UNSIGNED_BYTE(new Serializor<Integer>() {
		@Override
		public Integer read(ByteBuf in) throws IOException {
			return (int) in.readUnsignedByte();
		}

		@Override
		public void write(ByteBuf out, Integer val) throws IOException {
			out.writeByte(val);
		}
	}), BOOL(new Serializor<Boolean>() {
		@Override
		public Boolean read(ByteBuf in) throws IOException {
			return in.readBoolean();
		}

		@Override
		public void write(ByteBuf out, Boolean val) throws IOException {
			out.writeBoolean(val);
		}
	}), STRING(new Serializor<String>() {
		@Override
		public String read(ByteBuf in) throws IOException {
			return PacketUtils.readString(in, 1000, true);
		}

		@Override
		public void write(ByteBuf out, String val) throws IOException {
			PacketUtils.writeString(val, out, true);
		}
	}), JSON_CHAT(new Serializor<String>() {
		@Override
		public String read(ByteBuf in) throws IOException {
			return PacketUtils.readString(in, 1000, true);
		}

		@Override
		public void write(ByteBuf out, String val) throws IOException {
			PacketUtils.writeString(JsonUtils.plainMessageToJson(val), out, true);
		}
	}), SHORT(new Serializor<Short>() {
		@Override
		public Short read(ByteBuf in) throws IOException {
			return in.readShort();
		}

		@Override
		public void write(ByteBuf out, Short val) throws IOException {
			out.writeShort(val);
		}
	}), UNSIGNED_SHORT(new Serializor<Integer>() {
		@Override
		public Integer read(ByteBuf in) throws IOException {
			return in.readUnsignedShort();
		}

		@Override
		public void write(ByteBuf out, Integer val) throws IOException {
			out.writeShort(val);
		}
	}), BYTE_ARRAY(new Serializor<byte[]>() {
		@Override
		public byte[] read(ByteBuf in) throws IOException {
			byte[] bytes = new byte[] {};
			in.readBytes(bytes);
			return bytes;
		}

		@Override
		public void write(ByteBuf out, byte[] val) throws IOException {
			out.writeBytes(val);
		}
	}), FLOAT(new Serializor<Float>() {
		@Override
		public Float read(ByteBuf in) throws IOException {
			return in.readFloat();
		}

		@Override
		public void write(ByteBuf out, Float val) throws IOException {
			out.writeFloat(val);
		}
	}), DOUBLE(new Serializor<Double>() {
		@Override
		public Double read(ByteBuf in) throws IOException {
			return in.readDouble();
		}

		@Override
		public void write(ByteBuf out, Double val) throws IOException {
			out.writeDouble(val);
		}
	}), LONG(new Serializor<Long>() {
		@Override
		public Long read(ByteBuf in) throws IOException {
			return in.readLong();
		}

		@Override
		public void write(ByteBuf out, Long val) throws IOException {
			out.writeLong(val);
		}
	}), ENTITY_METADATA(new Serializor<MetaParam<?>[]>() {
		@Override
		public MetaParam<?>[] read(ByteBuf input) throws IOException {
			MetaParam<?>[] parameters = new MetaParam<?>[MetaParam.METADATA_SIZE];
			for (int x = input.readUnsignedByte(); x != 127; x = input.readUnsignedByte()) {
				int index = x & 0x1F;
				int type = x >> 5;
				switch (type) {
					case MetaParam.TYPE_BYTE:
						parameters[index] = new MetaParam<Byte>(type, index, input.readByte());
						break;
					case MetaParam.TYPE_SHORT:
						parameters[index] = new MetaParam<Short>(type, index, input.readShort());
						break;
					case MetaParam.TYPE_INT:
						parameters[index] = new MetaParam<Integer>(type, index, input.readInt());
						break;
					case MetaParam.TYPE_FLOAT:
						parameters[index] = new MetaParam<Float>(type, index, input.readFloat());
						break;
					case MetaParam.TYPE_STRING:
						parameters[index] = new MetaParam<String>(type, index, PacketUtils.readUtf8String(input));
						break;
					case MetaParam.TYPE_ITEM:
						short id = input.readShort();
						if (id <= 0) {
							parameters[index] = new MetaParam<ItemStack>(type, index, null);
						} else {
							byte stackSize = input.readByte();
							short dataValue = input.readShort();
							//short dataLenght = in.readShort();
							//byte[] metadata = new byte[0];
							//if(dataLenght > 0) {
							// FIXME previous check if its enchantable
							//metadata = new byte[dataLenght];
							//in.readFully(metadata);
							//}
							parameters[index] = new MetaParam<ItemStack>(type, index, new ItemStack(id, stackSize, dataValue));
						}
						break;
					default:
						throw new UnsupportedOperationException("Metadata ID '" + type + "' is not implemented!");
				}
			}
			return parameters;
		}

		@Override
		public void write(ByteBuf output, MetaParam<?>[] val) throws IOException {
			for (MetaParam<?> parameter : val) {

				if (parameter == null)
					continue;

				int type = parameter.getType();
				int index = parameter.getIndex();

				output.writeByte(((type & 0x07) << 5) | (index & 0x1F));

				switch (type) {
					case MetaParam.TYPE_BYTE:
						output.writeByte(((MetaParam<Byte>) parameter).getValue());
						break;
					case MetaParam.TYPE_SHORT:
						output.writeShort(((MetaParam<Short>) parameter).getValue());
						break;
					case MetaParam.TYPE_INT:
						output.writeInt(((MetaParam<Integer>) parameter).getValue());
						break;
					case MetaParam.TYPE_FLOAT:
						output.writeFloat(((MetaParam<Float>) parameter).getValue());
						break;
					case MetaParam.TYPE_STRING:
						PacketUtils.writeUtf8String(output, ((MetaParam<String>) parameter).getValue());
						break;
					case MetaParam.TYPE_ITEM:
						ItemStack item = ((MetaParam<ItemStack>) parameter).getValue();

						if (item.getId() < 0) { // FIXME less then zero check
							output.writeShort(-1);
						} else {
							output.writeShort(item.getId());
							output.writeByte(item.getCount());
							output.writeShort(item.getDamage());
							output.writeShort(-1);
							//if (item.getDataLength() >= 0) { // FIXME previous check if its enchantable
							//	out.write(item.getMetadata());
							//}
						}
						break;
					case MetaParam.TYPE_COORDINATE:
						Position coord = ((MetaParam<Position>) parameter).getValue();
						output.writeInt((int) coord.x);
						output.writeInt((int) coord.y);
						output.writeInt((int) coord.z);
						break;
				}
			}
			output.writeByte(127);
		}
	}), ITEM(new Serializor<ItemStack>() {
		@Override
		public ItemStack read(ByteBuf in) throws IOException {
			short id = in.readShort();
			if (id <= 0) {
				return null;
			} else {
				byte stackSize = in.readByte();
				short dataValue = in.readShort();
				short dataLenght = in.readShort();
				byte[] metadata = new byte[0];
				if (dataLenght >= 0 && id != 0) { // FIXME previous check if its enchantable. Since MC 1.3.2 all items except 0 (empty hand) can send this.
					metadata = new byte[dataLenght];
					in.readBytes(metadata);
				}
				return new ItemStack(id, stackSize, dataValue);
			}
		}

		@Override
		public void write(ByteBuf out, ItemStack val) throws IOException {
			if (val == null || val.getId() <= 0) { // FIXME less then zero check
				out.writeShort(-1);
			} else {
				out.writeShort(val.getId());
				out.writeByte(val.getCount());
				out.writeShort(val.getDamage());
				out.writeShort(val.getData().length);
				if (val.getData().length > 0) // FIXME previous check if its enchantable // TODO is is Id or datalength?
					out.writeBytes(val.getData());				
			}
		}
	}),
	INT_ARRAY(new Serializor<int[]>() {
		@Override
		public int[] read(ByteBuf in, Object more) throws IOException {
			int count = ((Number) more).intValue();
			int[] array = new int[count];
			Serializor<Integer> intserializator = (Serializor<Integer>) INT.getSerializor();
			for (int i = 0; i < count; i++) {
				array[i] = intserializator.read(in);
			}
			return array;
		}

		@Override
		public void write(ByteBuf out, int[] val) throws IOException {
			Serializor intSerializor = INT.getSerializor();
			for (int i = 0; i < val.length; i++) {
				intSerializor.write(out, val[i]);
			}
		}
	}),
	ITEM_ARRAY(new Serializor<ItemStack[]>() {
		@Override
		public ItemStack[] read(ByteBuf in, Object more) throws IOException {
			int count = ((Number) more).intValue();
			ItemStack[] items = new ItemStack[count];
			Serializor<ItemStack> itemSerializor = (Serializor<ItemStack>) ITEM.getSerializor();
			for (int i = 0; i < count; i++) {
				items[i] = itemSerializor.read(in);
			}
			return items;
		}

		@Override
		public void write(ByteBuf out, ItemStack[] val) throws IOException {
			Serializor itemSerializor = ITEM.getSerializor();
			for (int i = 0; i < val.length; i++) {
				itemSerializor.write(out, val[i]);
			}
		}
	}), CONDITIONAL_SHORT(new Serializor<Short>() {
		/*
		 * ugly hack, but I don't see a better solution :/
		 */
		@Override
		public Short read(ByteBuf in, Object moreInfo) throws IOException {
			if (((Number) moreInfo).intValue() > 0) {
				return in.readShort();
			} else {
				return Short.MIN_VALUE;
			}
		}

		@Override
		public void write(ByteBuf out, Short val) throws IOException {
			if (val != Short.MIN_VALUE)
				out.writeShort(val);
		}
	}), BLOCKCOORD_COLLECTION(new Serializor<Collection<Position>>() {
		@Override
		public Collection<Position> read(ByteBuf in) throws IOException {
			int size = in.readInt();
			Set<Position> ret = new HashSet<Position>();
			for (int i = 0; i < size; i++) {
				int x = in.readByte();
				int y = in.readByte();
				int z = in.readByte();
				ret.add(new Position(x, y, z));
			}
			return ret;
		}

		@Override
		public void write(ByteBuf out, Collection<Position> val) throws IOException {
			out.writeInt(val.size());
			for (Position block : val) {
				out.writeByte((int) block.x);
				out.writeByte((int) block.y);
				out.writeByte((int) block.z);
			}
		}
	});

	private final Serializor<?> serializor;

	Type(Serializor<?> serializor) {
		this.serializor = serializor;
	}

	public Serializor<?> getSerializor() {
		return serializor;
	}
}
