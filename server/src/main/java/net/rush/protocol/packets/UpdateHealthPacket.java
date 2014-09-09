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
public class UpdateHealthPacket extends Packet {
	
	private float health;
	private int food;
	private float saturation;

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeFloat(health);
		out.writeShort(food);
		out.writeFloat(saturation);
	}
}
