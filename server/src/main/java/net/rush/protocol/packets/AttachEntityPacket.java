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
public class AttachEntityPacket extends Packet {

	private int entityId;
	private int vehicleId;
	private boolean leash;
	
	@Override
	public void read(ByteBuf in) throws IOException {
		entityId = in.readInt();
		vehicleId = in.readInt();
		leash = compat_readByteBoolean(in);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		out.writeInt(vehicleId);
		compat_writeByteBoolean(leash, out);
	}
}
