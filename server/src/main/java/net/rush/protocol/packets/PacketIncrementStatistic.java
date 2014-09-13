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
public class PacketIncrementStatistic extends Packet {

	private int statisticId;
	private int amount;

	@Override
	public void write(ByteBuf out) throws IOException {
		System.out.println("Increment Statistics Has Been Tested Only On 1.6.4");
		out.writeInt(statisticId);
		out.writeByte(amount);
	}

}
