package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.protocol.utils.MetaParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EntityMetadataPacket extends Packet {

	private int entityId;
	private MetaParam<?>[] metadata;

	@Override
	public void read(ByteBuf in) throws IOException {
		entityId = in.readInt();
		metadata = readMetadata(in);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) 
			out.writeInt(entityId);
		else 
			writeByteInteger(out, entityId);
		writeMetadata(out, metadata);
	}
}
