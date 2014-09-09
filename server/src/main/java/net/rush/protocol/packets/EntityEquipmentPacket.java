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
public class EntityEquipmentPacket extends Packet {

	public static final int HELD_ITEM = 0;
	public static final int BOOTS_SLOT = 1;
	public static final int LEGGINGS_SLOT = 2;
	public static final int CHESTPLATE_SLOT = 3;
	public static final int HELMET_SLOT = 4;

	private int entityId;
	private int slot;
	private ItemStack item;
	
	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) 
			out.writeInt(entityId);
		else 
			writeByteInteger(out, entityId);
		out.writeShort(slot);
		if(compat) {
			out.writeShort(item == null ? -1 : item.getId());
			out.writeShort(item == null ? 0 : item.getDamage());
		} else
			writeItemstack(item, out, !compat && protocol > 46);
	}
}
