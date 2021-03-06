package noki.preciousshot.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;


/**********
 * @class LangHelper
 * @inner_class LangKey
 *
 * @description lang key及びチャットに関するヘルパークラスです。
 * @descriptoin_en 
 */
public class LangHelper {
	
	//******************************//
	// define member variables.
	//******************************//
	private static final String clickEventPrefix = "2aaf7189bdef12:";
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void sendChat(LangKey key, Object... inserts) {
		
		sendChat(new ChatComponentTranslation(key.key(), inserts));
		
	}
	
	public static void sendChatWithUrl(LangKey key, LangKey urlKey, String url, Object... urlInserts) {
		
		IChatComponent component = new ChatComponentTranslation(urlKey.key(), urlInserts);
		component.getChatStyle().setUnderlined(true);
		component.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		
		sendChat(new ChatComponentTranslation(key.key(), component));
		
	}
	
	public static void sendChatWithViewOpen(LangKey key, LangKey urlKey, String fileName, Object... urlInserts) {
		
		IChatComponent component = new ChatComponentTranslation(urlKey.key(), urlInserts);
		component.getChatStyle().setUnderlined(true);
		component.getChatStyle().setChatClickEvent(new ClickEvent(null, clickEventPrefix+fileName));
		
		sendChat(new ChatComponentTranslation(key.key(), component));
		
	}
	
	public static void sendChat(IChatComponent chatComponent) {
		
		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(chatComponent);
		
	}
	
	public static String getViewOpenString(ClickEvent event) {
		
		if(event.getValue().length() < clickEventPrefix.length()) {
			return null;
		}
		
		boolean res = event.getValue().substring(0, clickEventPrefix.length()).equals(clickEventPrefix);
		return res ? event.getValue().substring(clickEventPrefix.length()) : null;
		
	}
	
	
	//--------------------
	// Inner Class.
	//--------------------
	public enum LangKey {
		//*****define enums.************//
		SHOOTING_MODE	("preciousshot.chat.shooting.mode"),
		SHOOTING_DONE	("preciousshot.chat.shooting.done"),
		SHOOTING_FAILED	("preciousshot.chat.shooting.failed"),
		SHOOTING_URL	("preciousshot.chat.shooting.url"),
		PANORAMA_MODE	("preciousshot.chat.panorama.mode"),
		PANORAMA_DONE	("preciousshot.chat.panorama.done"),
		PANORAMA_FAILED	("preciousshot.chat.panorama.failed"),
		TWITTER_DISABLED("preciousshot.chat.twitter.disabled"),
		TWITTER_FAILED	("preciousshot.chat.twitter.failed"),
		TWITTER_SUCCESS	("preciousshot.chat.twitter.sccess"),
		TWITTER_URL		("preciousshot.chat.twitter.url");
		
		
		//*****define member variables.*//
		private String key;
		
		
		//*****define member methods.***//
		private LangKey(String key) {
			this.key = key;
		}
		
		public String key() {
			return this.key;
		}
		
		public String translated(Object... args) {
			return I18n.format(this.key, args);
		}
	}
	
}
