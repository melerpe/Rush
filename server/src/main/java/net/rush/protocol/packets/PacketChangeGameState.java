package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.RushException;
import net.rush.util.enums.GameStateReason;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketChangeGameState extends Packet {

	private int reason;
	private int value;

	public PacketChangeGameState(GameStateReason reason, int value) {
		super();
		this.reason = reason.getValue();
		this.value = value;
	}

	public PacketChangeGameState(GameStateReason reason) {
		super();

		if(reason.needsMoreInfo())
			throw new RushException("GameStateReason " + reason.name() + " needs additional info!");

		this.reason = reason.getValue();
		this.value = 0;
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(reason);
		if(compat)
			out.writeByte(value);
		else
			out.writeFloat(value);
	}

}
