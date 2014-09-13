package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.Position;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
/**
 * @deprecated Explosion Packet Only Tested On 1.6.4
 */
public class PacketExplosion extends Packet {

	private double x;
	private double y;
	private double z;
	private int size;
	private Set<Position> destroyedBlocks;


	@Override
	public void write(ByteBuf out) throws IOException {
		System.out.println("Write Explosion Only Tested On 1.6.4");
		writePositionAllDouble(x, y, z, out);
		out.writeFloat(size);
		out.writeInt(destroyedBlocks.size());
		for (Position block : destroyedBlocks)
			writePositionAllByte((int)block.x, (int)block.y, (int)block.z, out);
		
	}

}
