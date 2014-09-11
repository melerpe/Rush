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
public class Packet18LoginCompression extends Packet {
	
	public static final int COMPRESSION_DISABLED = -1;
	
	private int threshold;

	@Override
	public void write(ByteBuf out) throws IOException {
		writeByteInteger(out, threshold);
	}
}