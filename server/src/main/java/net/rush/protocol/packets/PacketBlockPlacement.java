package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.ItemStack;
import net.rush.model.Position;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketBlockPlacement extends Packet {

	private int x;
	private int y;
	private int z;
	private int direction;
	private ItemStack heldItem;
	private int cursorX;
	private int cursorY;
	private int cursorZ;

	@Override
	public void read(ByteBuf in) throws IOException {
		if (compat || protocol < 16) {
			x = in.readInt();
			if(compat)
				y = (byte) in.readUnsignedByte();
			else
				y = in.readByte();
			z = in.readInt();
		} else {
			Position pos = readPosition18(in);
			x = pos.intX();
			y = (byte) pos.intY();
			z = pos.intZ();
		}
		direction = in.readByte();
		heldItem = readItemstack(in, !compat && protocol > 46);
		cursorX = in.readByte();
		cursorY = in.readByte();
		cursorZ = in.readByte();
	}
}
