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
public class PacketRemoveEntityEffect extends Packet {

	private int entityId;
	private int effectId;

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeInt(entityId);
		output.writeByte(effectId);
	}
}
