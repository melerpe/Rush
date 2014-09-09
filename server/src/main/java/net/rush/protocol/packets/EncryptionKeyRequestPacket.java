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
public class EncryptionKeyRequestPacket extends Packet {

	private String serverId;
	private int publicKeyLength;
	private byte[] publicKey;
	private int verifyTokenLength;
	private byte[] verifyToken;

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		writeString(serverId, out, true);
		out.writeShort(publicKeyLength);
		out.writeBytes(publicKey);
		out.writeShort(verifyTokenLength);
		out.writeBytes(verifyToken);
	}
}