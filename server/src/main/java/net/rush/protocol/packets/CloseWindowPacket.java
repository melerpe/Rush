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
public class CloseWindowPacket extends Packet {
	
	private int windowId;
	
	@Override
	public void read(ByteBuf in) throws IOException {
		windowId = (byte) (compat ? in.readByte() : in.readUnsignedByte());
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(windowId);
	}

}
