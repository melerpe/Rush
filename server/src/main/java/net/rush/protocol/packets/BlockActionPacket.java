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
public class BlockActionPacket extends Packet {

	private int x;
	private int y;
	private int z;
	private int byte1;
	private int byte2;
	private int blockId;

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		writePositionYShort(x, y, z, out);
		out.writeByte(byte1);
		out.writeByte(byte2);
		out.writeShort(blockId);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		compat_writePosition18(x, y, z, out);
		out.writeByte(byte1);
		out.writeByte(byte2);
		writeVarInt(blockId, out);
	}
}
