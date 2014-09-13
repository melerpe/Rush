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
public class PacketKeepAlive extends Packet {

	private int token;

	@Override
	public void read(ByteBuf input) throws IOException {
		if(compat || protocol < 16)
			token = input.readInt();
		else
			token = readVarInt(input);
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		if (compat || protocol < 32) 
			output.writeInt(token);
		else 
			writeByteInteger(output, token);
	}
}
