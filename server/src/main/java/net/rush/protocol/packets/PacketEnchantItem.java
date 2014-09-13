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
public class PacketEnchantItem extends Packet {
	
	private int windowId;
	private int enchantment;

	@Override
	public void read(ByteBuf in) throws IOException {
		windowId = in.readByte();
		enchantment = in.readByte();
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(windowId);
		out.writeByte(enchantment);
	}
}
