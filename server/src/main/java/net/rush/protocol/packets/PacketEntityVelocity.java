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
public class PacketEntityVelocity extends Packet {
	
	private int entityId;
	private int velocityX;
	private int velocityY;
	private int velocityZ;

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) 
			out.writeInt(entityId);
		else 
			writeByteInteger(out, entityId);
		out.writeShort(velocityX);
		out.writeShort(velocityY);
		out.writeShort(velocityZ);
	}
}
