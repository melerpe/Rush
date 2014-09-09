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
public class SetExperiencePacket extends Packet {

	private float experienceBar;
	private int level;
	private int totalExperience;
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeFloat(experienceBar);
		if (compat || protocol < 16) {
			out.writeShort(level);
			out.writeShort(totalExperience);
		} else {
			writeByteInteger(out, level);
			writeByteInteger(out, totalExperience);
		}
	}
}
