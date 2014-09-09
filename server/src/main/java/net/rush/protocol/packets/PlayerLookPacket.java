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
public class PlayerLookPacket extends Packet {

	private float yaw;
	private float pitch;
	private boolean onGround;

	@Override
	public void read(ByteBuf input) throws IOException {
		yaw = input.readFloat();
		pitch = input.readFloat();
		onGround = input.readBoolean();
	}
	
	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeFloat(yaw);
		output.writeFloat(pitch);
		output.writeBoolean(onGround);
	}

}
