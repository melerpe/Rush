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
public class PacketEntityStatus extends Packet {

	private int entityId;
	private int status;

	@Override
	public void read(ByteBuf input) throws IOException {
		entityId = input.readInt();
		status = input.readByte();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeInt(entityId);
		output.writeByte(status);
	}
}
