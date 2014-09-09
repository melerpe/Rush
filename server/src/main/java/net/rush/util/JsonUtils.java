package net.rush.util;

import net.rush.protocol.utils.ServerPing;

/**
 * Smart, easy and comfortable JSON handling without the need of an additional library.
 * @author kangarko
 * (C) 2014 Patent pending. For fürther information contact your favorite toilet paper maker.
 */
public class JsonUtils {

	private JsonUtils() {}

	public static String serverPingToJson(ServerPing ping) {
		String json = "{";
		json+= "\"version\":{\"name\":\"" + ping.getVersion().getName() + "\",\"protocol\":" + ping.getVersion().getProtocol() + "},";
		json+= "\"description\":\"" + ping.getDescription() + "\",";
		json+= "\"favicon\":\"" + ping.getFavicon() + "\",";
		json+= "\"players\":{\"max\":" + ping.getPlayers().getMax() + ",\"online\":" + ping.getPlayers().getOnline() + "}}";
		return json;
	}

	public static String plainMessageToJson(String str) {
		String json = "{\"text\":\"" + StringUtils.colorize(str.replace("%Rush", "&3Rush //&f")) + "\"}";
		
		return json;
	}
	
	/*public static String chatMessageToJson(Message message) {
		String 
		json = "{";
		
		json+= "\"text\":\"" + message.text + "\",";
		
		json+= "\"bold\":\"" + message.bold + "\",";		
		json+= "\"italic\":\"" + message.italic + "\",";
		json+= "\"underlined\":\"" + message.underlined + "\",";
		json+= "\"strikethrough\":\"" + message.strikethrough + "\",";
		json+= "\"obfuscated\":\"" + message.obfuscated + "\",";
		
		json+= "\"color\":{\"code\":\"" + message.color.code + "\"" + "},";		
		json+= "\"clickEvent\":{\"action\":" + message.clickEvent.action + ",\"value\":" + message.clickEvent.value + "}";
				
		json+= "}";
		return json;
	}

	@SuppressWarnings("unchecked")
	public static String serverPingToJson(ServerPing ping) {

		JSONObject version = new JSONObject();		
		version.put("name", ping.getVersion().getName());
		version.put("protocol", ping.getVersion().getProtocol());

		JSONObject players = new JSONObject();
		players.put("max", ping.getPlayers().getMax());
		players.put("online", ping.getPlayers().getOnline());

		JSONObject json = new JSONObject();
		json.put("version", version);
		json.put("description", ping.getDescription());
		json.put("favicon", ping.getFavicon());
		json.put("players", players);

		return json.toJSONString();
	}*/
}
