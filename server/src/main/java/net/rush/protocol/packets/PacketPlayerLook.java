package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class PacketPlayerLook extends PacketPlayerOnGround {

	private float yaw;
	private float pitch;
	
	public PacketPlayerLook(float yaw, float pitch, boolean onGround) {
		super(onGround);
		this.yaw = yaw;
		this.pitch = pitch;
		
		lookPacket = true;
	}

	@Override
	public void read(ByteBuf input) throws IOException {
		yaw = input.readFloat();
		pitch = input.readFloat();
		onGround = input.readBoolean();
	}
}
