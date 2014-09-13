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
public class PacketUpdateWindowProperty extends Packet {

	private int windowId;
	private int property;
	private int value;

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeByte(windowId);
		output.writeShort(property);
		output.writeShort(value);
	}
}
