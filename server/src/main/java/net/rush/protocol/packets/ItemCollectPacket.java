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
public class ItemCollectPacket extends Packet {

	private int collected;
	private int collector;

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) {
			out.writeInt(collected);
			out.writeInt(collector);
		} else {
			writeByteInteger(out, collected);
			writeByteInteger(out, collector);
		}
	}
}
