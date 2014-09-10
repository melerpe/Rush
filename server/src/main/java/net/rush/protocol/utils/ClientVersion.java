package net.rush.protocol.utils;

import lombok.Getter;

@Getter
public class ClientVersion {
	private final int protocol;
	private final String version;

	public ClientVersion(int protocol) {
		super();
		this.protocol = protocol;
		this.version = getVersion(protocol);
	}

	public static String getVersion(int protocol) {
		switch (protocol) {
		case 29: 
			return "noSupport_1.2.5";
		case 39:
			return "noSupport_1.3.2";
		case 51:
			return "noSupport_1.4.7";
		case 61:
			return "1.5.2";
		case 78:
			return "1.6.4";
		case 4:
			return "1.7.2-5";
		case 5:
			return "1.7.6-10";
		case 47:
			return "1.8";
		default:
			throw new NullPointerException("Unsupported protocol (" + protocol + ")");
		}
	}

	@Override
	public String toString() {
		return "ver=" + version + ",protocol=" + protocol;
	}
}