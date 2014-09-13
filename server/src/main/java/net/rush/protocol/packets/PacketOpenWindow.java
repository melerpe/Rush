package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.JsonUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketOpenWindow extends Packet {

	private int windowId;
	private int inventoryType;
	private String windowTitle;
	private int numberOfSlots;
	private boolean useProvidedWindowTitle;
	private int horseId;

	public PacketOpenWindow(int windowId, int inventoryType, String windowTitle, int numberOfSlots, boolean useProvidedWindowTitle) {
		this(windowId, inventoryType, windowTitle, numberOfSlots, useProvidedWindowTitle, -1);
	}

	/*@Override
	public void read(ByteBuf input) throws IOException {
		windowId = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		inventoryType = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		windowTitle = readString(input, 33, compat);
		numberOfSlots = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		useProvidedWindowTitle = input.readBoolean();

		if(inventoryType == 11)
			horseId = input.readInt();
	}*/

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeByte(windowId);
		out.writeByte(inventoryType);
		writeString(windowTitle, out, compat);
		out.writeByte(numberOfSlots);
		out.writeBoolean(useProvidedWindowTitle);

		if(inventoryType == 11)
			out.writeInt(horseId);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(windowId);
		if(protocol < 22) {
			out.writeByte(inventoryType);
			writeString(windowTitle, out, compat);
		} else {
			writeString(getInventoryType(inventoryType), out, false);
			writeString(JsonUtils.plainMessageToJson(windowTitle), out, compat);
		}
		out.writeByte(numberOfSlots);

		if(protocol < 22)
			out.writeBoolean(useProvidedWindowTitle);

		if(inventoryType == 11)
			out.writeInt(horseId);
	}

	private String getInventoryType(int id) {
		switch (id) {
		case 0:
			return "minecraft:chest";
		case 1:
			return "minecraft:crafting_table";
		case 2:
			return "minecraft:furnace";
		case 3:
			return "minecraft:dispenser";
		case 4:
			return "minecraft:enchanting_table";
		case 5:
			return "minecraft:brewing_stand";
		case 6:
			return "minecraft:villager";
		case 7:
			return "minecraft:beacon";
		case 8:
			return "minecraft:anvil";
		case 9:
			return "minecraft:hopper";
		case 10:
			return "minecraft:dropper";
		case 11:
			return "EntityHorse";
		}
		throw new IllegalArgumentException("Unknown type " + id);
	}

}
