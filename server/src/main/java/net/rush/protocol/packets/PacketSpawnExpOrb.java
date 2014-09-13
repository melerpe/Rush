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
public class PacketSpawnExpOrb extends Packet {

	private int entityId;
	private int x;
	private int y;
	private int z;
	private short count;

	@Override
	public void write(ByteBuf output) throws IOException {
		if(compat)
			output.writeInt(entityId);
		else
			writeVarInt(entityId, output);
		output.writeInt(x * 32);
		output.writeInt(y * 32);
		output.writeInt(z * 32);
		output.writeShort(count);
	}
}
