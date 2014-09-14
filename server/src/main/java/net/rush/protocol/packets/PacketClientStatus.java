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
public class PacketClientStatus extends Packet {

	public static enum ClientStatusAction {
		PERFORM_RESPAWN(0),
		REQUEST_STATS(1),
		OPEN_ACHIEVEMENT_INVENTORY(2);
		
		int id;
		
		private ClientStatusAction(int id) {
			this.id = id;
		}
		
		public static ClientStatusAction byId(int id) {
			for (ClientStatusAction action : values())
				if(action.id == id)
					return action;
			throw new NullPointerException("Unknown client status action id " + id);
		}
	}
	
	private ClientStatusAction action;

	@Override
	public void read(ByteBuf in) throws IOException {
		action = ClientStatusAction.byId(compat || protocol < 47 ? in.readByte() : in.readUnsignedByte());
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeByte(action.id);
	}
}
