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
public class Packet17LoginSuccess extends Packet {

	private String uuid;
	private String name;

	@Override
	public void write(ByteBuf output) throws IOException {
		writeString(uuid, output, false);
		writeString(name, output, false);
	}

}
