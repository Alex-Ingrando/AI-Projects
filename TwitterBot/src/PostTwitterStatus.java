import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader; 

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class PostTwitterStatus {

	PostTwitterStatus() {

 
	}

	public void updateTwitter(String update_str) {
		try {
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("OlloJyd71MCZY776wcUYxVFNl")
			.setOAuthConsumerSecret("ELNiZafYW9wuktv5fuc8JpuXxobPAvClNiZ2w6FriqdqKdOVAR")
			.setOAuthAccessToken("912350199665872896-GTLvnsUGcQEUriXUVoGKS1VMSYFzI0D")
			.setOAuthAccessTokenSecret("GA46Z1ErUTvnzZXDJsItJDqCxa8OoIRFt2f1ahT0vJnCB");
			
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			Status status = twitter.updateStatus(update_str);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException te) {
			
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("Failed to read the system input.");
		}
	}
}