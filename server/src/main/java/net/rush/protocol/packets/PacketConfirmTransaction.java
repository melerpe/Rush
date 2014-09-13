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
public class PacketConfirmTransaction extends Packet {

	private int windowId;
	private int action;
	private boolean accepted;

	@Override
	public void read(ByteBuf in) throws IOException {
		windowId = in.readByte();
		action = in.readShort();
		accepted = in.readBoolean();
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(windowId);
		out.writeShort(action);
		out.writeBoolean(accepted);
	}
}
