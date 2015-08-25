package noki.preciousshot.helper;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import noki.preciousshot.PreciousShotData;

public class TwitterHelper {
	
	public static void tweetMedia(File file) {
		
		Thread thread = new Thread() {
			private File file;
			
			public Thread setFile(File file) {
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
					Status status = twitter.updateStatus(new StatusUpdate("").media(this.file));
				} catch (TwitterException e) {
				}
			}
		}.setFile(file);
		
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
