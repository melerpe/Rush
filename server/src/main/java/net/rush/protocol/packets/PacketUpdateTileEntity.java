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
public class PacketUpdateTileEntity extends Packet {

	private int x;
	private int y;
	private int z;
	private int action;
	private byte[] data;
	
	@Override
	public void write(ByteBuf out) throws IOException {
		writePositionYShort(x, y, z, out);
		out.writeByte(action);
		out.writeShort(data.length);
		out.writeBytes(data);
	}

}
