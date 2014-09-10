package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TabCompletePacket extends Packet {

	@NonNull
	private String text;
	
	public long position;

	@Override
	public void read(ByteBuf input) throws IOException {
		text = readString(input, 32767, compat);
		if (!compat && protocol >= 37)
			if (input.readBoolean()) {
				position = input.readLong();
			}
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		writeString(text, output, compat);
	}
}
