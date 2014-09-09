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
public class MultiBlockChangePacket extends Packet {

	private int chunkX;
	private int chunkZ;
	private short recordCount;
	private int dataSize;
	private byte[] data;
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(chunkX);
		out.writeInt(chunkZ);
		out.writeShort(recordCount);
		out.writeInt(dataSize);
		out.writeBytes(data);
	}

}
