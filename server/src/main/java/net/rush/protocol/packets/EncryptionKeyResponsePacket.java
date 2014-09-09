package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.rush.protocol.Packet;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EncryptionKeyResponsePacket extends Packet {

	private int secretLength;
	private byte[] secret;
	private int verifyTokenLength;
	private byte[] verifyTokenResponse;

	public EncryptionKeyResponsePacket() {
		super();
		secretLength = (short) 0;
		secret = new byte[] {};
		verifyTokenLength = (short) 0;
		verifyTokenResponse = new byte[] {};
	}

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeShort(secretLength);
		out.writeBytes(secret);
		out.writeShort(verifyTokenLength);
		out.writeBytes(verifyTokenResponse);
	}
}