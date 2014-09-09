package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MapDataPacket extends Packet {

	private short itemType;
	private short itemId;
	private byte[] data;

	@Override
	public void write(ByteBuf output) throws IOException {
		if(compat)
			output.writeShort(itemType);
		else
			writeVarInt(itemType, output);
		if(compat)
			output.writeShort(itemId);
		if (protocol < 27) {
			output.writeShort(data.length);
			output.writeBytes(data);
		} else 
			throw new WrongMethodTypeException("Too lazy to write map-packet method for 1.8 :P");

	}

}
