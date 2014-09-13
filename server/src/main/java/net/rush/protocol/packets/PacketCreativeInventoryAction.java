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
public class PacketCreativeInventoryAction extends Packet {

	private int slot;
	private ItemStack item;
	
	@Override
	public void read(ByteBuf in) throws IOException {
		slot = in.readShort();
		item = readItemstack(in, !compat && protocol > 46);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeShort(slot);
		writeItemstack(item, out, !compat && protocol > 46);
	}

}
