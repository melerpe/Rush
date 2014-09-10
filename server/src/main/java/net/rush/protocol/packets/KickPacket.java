package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.protocol.Session;
import net.rush.protocol.utils.ServerPing;
import net.rush.util.JsonUtils;
import net.rush.util.StringUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class KickPacket extends Packet {
	
	private String reason;
	private boolean jsonize = false;

	/**
	 * @deprecated Use {@link Session#disconnect(String reason)}
	 */
	public KickPacket(String reason) {
		this.reason = StringUtils.colorize(reason);
		
		if(!compat || (compat && protocol == 78))
			jsonize = true;
	}
	
	public KickPacket(String reason, boolean jsonize) {
		this.reason = StringUtils.colorize(reason);
		this.jsonize = jsonize;
	}
	
	public KickPacket(ServerPing ping) {
		this.reason = JsonUtils.serverPingToJson(ping);
		jsonize = false;
	}

	@Override
	public void read(ByteBuf input) throws IOException {
		readString(input, 256, compat);
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		writeString(!jsonize ? reason : "\"" + reason + "\"", output, compat);
	}
	
}
