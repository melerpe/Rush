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
public class PacketNamedSoundEffect extends Packet {

	private String soundName;
	private int x;
	private int y;
	private int z;
	private float volume;
	private int pitch;

	public PacketNamedSoundEffect(String soundName, double x, double y, double z, float volume, float pitch) {
		super();

		if (pitch < 0) {
			pitch = 0;
		}

		if (pitch > 255) {
			pitch = 255;
		}

		this.soundName = soundName;
		this.x = (int) (x * 8D);
		this.y = (int) (y * 8D);
		this.z = (int) (z * 8D);
		this.volume = volume;
		this.pitch = (int) (pitch * 63.0F);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		writeString(soundName, out, compat);
		writePositionAllIntegers(x, y, z, out);
		out.writeFloat(volume);
		out.writeByte(pitch);
	}
}
