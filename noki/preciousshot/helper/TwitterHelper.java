/**********
 * NOTICE
 * 
 *
 * This work uses the Java library, "Twitter4J (http://twitter4j.org)", licensed under Apache License 2.0.
 * 
 * For Apache License 2.0, see here.
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Especially the class, noki.preciousshot.helper.TwitterHelper, directly uses classes supplied by the library.
 * 
 **********/


package noki.preciousshot.helper;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import noki.preciousshot.PreciousShotCore;
import noki.preciousshot.PreciousShotData;
import noki.preciousshot.helper.LangHelper.LangKey;


/**********
 * @class TwitterHelper
 *
 * @description Twitterに関する操作を行うクラスです。
 * @descriptoin_en 
 */
public class TwitterHelper {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	
	//----------
	//Static Method.
	//----------
	public static void tweetMedia(String text, File file) {
		
		Thread thread = new Thread() {
			private String text;
			private File file;
			
			public Thread setArgs(String text, File file) {
				this.text = text;
				this.file = file;
				return this;
			}
			
			@Override
			public void run() {
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true)
					.setOAuthConsumerKey(PreciousShotData.twitterKeys[0])
					.setOAuthConsumerSecret(PreciousShotData.twitterKeys[1])
					.setOAuthAccessToken(PreciousShotData.twitterKeys[2])
					.setOAuthAccessTokenSecret(PreciousShotData.twitterKeys[3]);
				TwitterFactory tf = new TwitterFactory(cb.build());
				Twitter twitter = tf.getInstance();
				
				try {
					Status status = twitter.updateStatus(new StatusUpdate(this.text).media(this.file));
					if(status != null && status.getId() != 0) {
						String url = String.format("https://twitter.com/%s/status/%s", twitter.getScreenName(), status.getId());
						PreciousShotCore.log("the url is %s.", url);
						LangHelper.sendChatWithUrl(LangKey.TWITTER_SUCCESS, LangKey.TWITTER_URL, url);
					}
					else {
						LangHelper.sendChat(LangKey.TWITTER_FAILED);
					}
				} catch (TwitterException e) {
					LangHelper.sendChat(LangKey.TWITTER_FAILED);
				}
			}
		}.setArgs(text, file);
		
		thread.start();
		
	}
	
	public static boolean isEnable() {
		
		boolean twitterFlag = true;
		try {
			@SuppressWarnings("unused")
			Class<?> c = Class.forName("twitter4j.Twitter");
		} catch (ClassNotFoundException e) {
			twitterFlag = false;
		}
		
		if(twitterFlag
				&& !PreciousShotData.twitterKeys[0].equals("") && !PreciousShotData.twitterKeys[1].equals("")
				&& !PreciousShotData.twitterKeys[2].equals("") && !PreciousShotData.twitterKeys[3].equals("")) {
			return true;
		}
		return false;
		
	}

}
