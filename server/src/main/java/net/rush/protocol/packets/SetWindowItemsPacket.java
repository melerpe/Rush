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
public class SetWindowItemsPacket extends Packet {
	
	private int windowId;
	private int size;
	private ItemStack[] items;
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(windowId);
		out.writeShort(size);
		for(ItemStack is : items)
			writeItemstack(is, out, !compat && protocol > 46);
	}

}
