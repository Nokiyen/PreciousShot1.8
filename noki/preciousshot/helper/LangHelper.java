package noki.preciousshot.helper;


public class LangHelper {
	
	public static void sendChat(LangKey key, String... strArgs) {
		
	}
	
	public enum LangKey {
		
		SHOOTING_MODE	("preciousshot.chat.shooting.mode"),
		PANORAMA_MODE	("preciousshot.chat.panorama.mode"),
		PANORAMA_SHOT	("preciousshot.chat.panorama.shot"),
		PANORAMA_DONE	("preciousshot.chat.panorama.done"),
		TWITTER_DISABLED("preciousshot.chat.twitter.disabled"),
		TWITTER_FAILED	("preciousshot.chat.twitter.failed"),
		TWITTER_SUCCESS	("preciousshot.chat.twitter.sccess"),
		TWITTER_URL		("preciousshot.chat.twitter.url");
		
		private String key;
		
		private LangKey(String key) {
			this.key = key;
		}
		
		public String key() {
			return this.key;
		}
		
	}

}
