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
public class ClientSettingsPacket extends Packet {

	private String locale;
	private byte viewDistance;
	private byte chatFlags;
	private byte difficulty;
	private boolean showCape;

	// 1.7
	public boolean chatColours;
	
	// 1.8
	public int flags;

	@Override
	public void readCompat(ByteBuf in) throws IOException {
		locale = readString(in, 7, true);
		viewDistance = in.readByte();
		chatFlags = in.readByte();
		difficulty = in.readByte();
		showCape = in.readBoolean();
	}

	@Override
	public void read(ByteBuf input) throws IOException {
		locale = readString(input, 7, false);
		viewDistance = input.readByte();
		chatFlags = input.readByte();
		chatColours = input.readBoolean();
		if (protocol < 16 ) {
			difficulty = input.readByte();
			showCape = input.readBoolean();
		} else
			flags = input.readUnsignedByte();		
	}
}
