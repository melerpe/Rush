package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

import org.bukkit.Effect;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SoundOrParticleEffectPacket extends Packet {

	private Effect effect;
	private int x;
	private int y;
	private int z;
	private int data;
	private boolean relativeVolume;

	public SoundOrParticleEffectPacket(int effectId, int x, int y, int z, int data) {
		this(Effect.getById(effectId), x, y, z, data, false);
	}
	
	public SoundOrParticleEffectPacket(Effect effect, int x, int y, int z, int data) {
		this(effect, x, y, z, data, false);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(effect.getId());
		writePositionYByte(x, y, z, out);
		out.writeInt(data);
		out.writeBoolean(relativeVolume);
	}
}
