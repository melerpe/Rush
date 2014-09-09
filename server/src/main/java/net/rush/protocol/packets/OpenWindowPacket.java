package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class OpenWindowPacket extends Packet {
	
	private int windowId;
	private int inventoryType;
	private String windowTitle;
	private int numberOfSlots;
	private boolean useProvidedWindowTitle;
	private int horseId;
	
	public OpenWindowPacket(int windowId, int inventoryType, String windowTitle, int numberOfSlots, boolean useProvidedWindowTitle) {
		this(windowId, inventoryType, windowTitle, numberOfSlots, useProvidedWindowTitle, -1);
	}

	@Override
	public void read(ByteBuf input) throws IOException {
		windowId = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		inventoryType = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		windowTitle = readString(input, 33, false);
		numberOfSlots = (byte) (compat ? input.readByte() : input.readUnsignedByte());
		useProvidedWindowTitle = input.readBoolean();
		
		if(inventoryType == 11)
			horseId = input.readInt();
	}
	
	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeByte(windowId);
		output.writeByte(inventoryType);
		writeString(windowTitle, output, false);
		output.writeByte(numberOfSlots);
		output.writeBoolean(useProvidedWindowTitle);
		
		if(inventoryType == 11)
			output.writeInt(horseId);
	}

}
