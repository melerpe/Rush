package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Packet17StatusRequest extends Packet {


	@Override
	public void read(ByteBuf in) {
	}

	@Override
	public void write(ByteBuf out) {
	}

}
