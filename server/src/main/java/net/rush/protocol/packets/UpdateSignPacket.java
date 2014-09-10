package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.Position;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UpdateSignPacket extends Packet {

	private int x;
	private int y;
	private int z;
	private String line1;
	private String line2;
	private String line3;
	private String line4;

	@Override
	public void read(ByteBuf input) throws IOException {
		if (compat || protocol < 16) {
			x = input.readInt();
			y = input.readShort();
			z = input.readInt();
		} else {
			Position pos = readPosition18(input);
			x = pos.integerX();
			y = (short) pos.integerY();
			z = pos.integerZ();
		}

		line1 = readString(input, 16, compat);
		line2 = readString(input, 16, compat);
		line3 = readString(input, 16, compat);
		line4 = readString(input, 16, compat);
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		if (compat || protocol < 16) {
			output.writeInt(x);
			if(compat)
				output.writeShort(y);
			else
				output.writeInt(y);
			output.writeInt(z);
		} else
			writePosition18(output, x, y, z);
		writeString(line1, output, compat);
		writeString(line2, output, compat);
		writeString(line3, output, compat);
		writeString(line4, output, compat);
	}
}
