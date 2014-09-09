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
public class ClickWindowPacket extends Packet {

	private int windowId;
	private int slot;
	private int button;
	private int actionId;
	private int mode;
	private ItemStack clickedItem;
	
	@Override
	public void read(ByteBuf input) throws IOException {
		windowId = input.readByte();
		slot = input.readShort();
		button = input.readByte();
		actionId = input.readShort();
		mode = input.readByte();
		clickedItem = readItemstack(input, !compat && protocol > 46);
	}
}
