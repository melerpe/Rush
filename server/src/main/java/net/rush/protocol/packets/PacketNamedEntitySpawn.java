package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.Position;
import net.rush.protocol.Packet;
import net.rush.protocol.utils.MetaParam;
import net.rush.util.BlockDebreakifier;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketNamedEntitySpawn extends Packet {

	private int entityId;
	private String entityName;
	private int x;
	private int y;
	private int z;
	private int yaw;
	private int pitch;
	private int currentItem;
	private MetaParam<?>[] metadata;

	public PacketNamedEntitySpawn(int entityId, String playerName, Position pos, int yaw, int pitch, int currentItem, MetaParam<?>[] metadata) {
		super();
		this.entityId = entityId;
		entityName = playerName;
		x = (int) pos.x;
		y = (int) pos.y;
		z = (int) pos.z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.currentItem = currentItem;
		this.metadata = metadata;
	}
	
	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		writeString(entityName, out, true);
		writePositionAllIntegers(x, y, z, out);
		out.writeByte(yaw);
		out.writeByte(pitch);
		out.writeShort(currentItem);
		writeMetadata(out, metadata, !compat && protocol > 22);
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		writeVarInt(entityId, output);
		if (protocol < 20) {
			writeString("0-0-0-0-0", output, false);
			writeString(entityName, output, false);
			if (protocol > 4)
				writeVarInt(0, output);		
		} else {
			writeUuid(output, UUID.fromString("0-0-0-0-0"));
		}
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(z);		
		output.writeByte(yaw);
		output.writeByte(pitch);
		if (protocol >= 47)
			output.writeShort(BlockDebreakifier.getItemId(currentItem));
		else
			output.writeShort(currentItem);
		writeMetadata(output, metadata, !compat && protocol > 22);
	}
}
