package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.json.simple.parser.JSONParser;

public class MojangNameLookup {

	private static final JSONParser jsonParser = new JSONParser();

	public static String lookupName(UUID id) throws Exception {
		if (id == null) {
			return null;
		}

		InputStream inputStream = null;
		try {
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);
			connection.setUseCaches(false);
			inputStream = connection.getInputStream();

			Response response = (Response) jsonParser.parse(new InputStreamReader(connection.getInputStream()));

			if (response == null || response.name == null) {
				System.out.println("Failed to lookup name from UUID");
				return null;
			}

			if (response.cause != null && response.cause.length() > 0) {
				System.out.println("Failed to lookup name from UUID: " + response.errorMessage);
				return null;
			}

			return response.name;
		} catch (MalformedURLException ex) {
			System.out.println("Malformed URL in UUID lookup");
			return null;
		} catch (IOException ex) {
			inputStream.close();
		} finally {
			inputStream.close();
		}

		return null;
	}

	private class Response {
		String errorMessage;
		String cause;
		String name;
	}
}
