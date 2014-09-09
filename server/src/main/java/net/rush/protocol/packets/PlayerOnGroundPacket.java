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
public class PlayerOnGroundPacket extends Packet {

	private boolean onGround;

	@Override
	public void read(ByteBuf in) throws IOException {
		onGround = in.readBoolean();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeBoolean(onGround);
	}
}
