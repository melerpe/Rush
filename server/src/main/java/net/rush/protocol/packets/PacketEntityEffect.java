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
public class PacketEntityEffect extends Packet {

	private int entityId;
	private int effectId;
	private int amplifier;
	private short duration;

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) {
			out.writeInt(entityId);
			out.writeByte(effectId);
			out.writeByte(amplifier);
			out.writeShort(duration);
		} else {
			 writeByteInteger(out, entityId);
			 out.writeByte(effectId);
			 out.writeByte(amplifier);
			 writeByteInteger(out, duration);
			 out.writeBoolean(false);
		}
	}
}
