package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.ItemStack;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketSetSlot extends Packet {
	
	private int windowId;
	private int slot;
	private ItemStack item;

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeByte(windowId);
		output.writeShort(slot);
		writeItemstack(item, output, !compat && protocol > 46);
	}
}
