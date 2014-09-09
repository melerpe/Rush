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
public class UseEntityPacket extends Packet {

	private int playerEntityId;
	private int targetEntityId;
	private boolean rightclick;

	@Override
	public void readCompat(ByteBuf in) throws IOException {
		playerEntityId = in.readInt();
		targetEntityId = in.readInt();
		rightclick = in.readBoolean();
	}
	
	@Override
	public void read(ByteBuf input) throws IOException {
		if (protocol < 16) {
			targetEntityId = input.readInt();
			rightclick = input.readByte() == 0;
		} else {
			targetEntityId = readVarInt(input);
			int val = readVarInt(input);
			
			if (val == 2) {
				input.readFloat();
				input.readFloat();
				input.readFloat();
			} else {
				rightclick = val == 0;
			}
		}
	}

}
