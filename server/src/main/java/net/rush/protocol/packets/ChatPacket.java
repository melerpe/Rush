package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.JsonUtils;
import net.rush.util.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ChatPacket extends Packet {

	private String message;

	@Override
	public void read(ByteBuf input) throws IOException {
		message = readString(input, 65000, compat);
	}

	@Override
	public void write(ByteBuf output) throws IOException {		
		writeString(compat && protocol < 78 ? StringUtils.colorize(message.replace("%Rush", "&3Rush //&f")) : JsonUtils.plainMessageToJson(message), output, compat);		
		if (!compat && protocol > 15)
			output.writeByte(0);		
	}
}
