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
public class PacketPlayerOnGround extends Packet {

	protected boolean onGround;
	
	protected boolean positionPacket = false;
	protected boolean lookPacket = false;

	public PacketPlayerOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	@Override
	public void read(ByteBuf in) throws IOException {
		onGround = in.readBoolean();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeBoolean(onGround);
	}
}
