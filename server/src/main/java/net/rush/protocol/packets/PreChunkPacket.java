package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Deprecated
/**
 * @deprecated not working
 */
public class PreChunkPacket extends Packet {

	// TODO
	private short chunkCount;
	private boolean skyLight;
	private byte[] data;
	// Meta information
	private int x;
	private int z;
	private int primaryBitMap;
	private int addBitMap;
	
	public PreChunkPacket(short chunkCount, boolean skyLight, byte[] data, int x, int z, int primaryBitMap, int addBitMap) {
		super();
		this.chunkCount = chunkCount;
		this.skyLight = skyLight;
		this.data = data;
		this.x = x;
		this.z = z;
		this.primaryBitMap = primaryBitMap;
		this.addBitMap = addBitMap;
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeShort(chunkCount);
		out.writeInt(data.length);
		out.writeBoolean(skyLight);
		out.writeBytes(data);
		//meta
		out.writeInt(x);
		out.writeInt(z);
		out.writeShort(primaryBitMap);
		out.writeShort(addBitMap);
	}

}
