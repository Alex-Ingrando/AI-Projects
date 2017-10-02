import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.*;

public class TwitterInteraction {

	Twitter twitter;

	TwitterInteraction() {

		try {

			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("OlloJyd71MCZY776wcUYxVFNl")
					.setOAuthConsumerSecret("ELNiZafYW9wuktv5fuc8JpuXxobPAvClNiZ2w6FriqdqKdOVAR")
					.setOAuthAccessToken("912350199665872896-GTLvnsUGcQEUriXUVoGKS1VMSYFzI0D")
					.setOAuthAccessTokenSecret("GA46Z1ErUTvnzZXDJsItJDqCxa8OoIRFt2f1ahT0vJnCB");
			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to get timeline: " + e.getMessage());
		}

	}

	public void updateTwitter(String update_str) {
		try {

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

	public ArrayList searchForTweets(String searchTerm) {
		ArrayList res = new ArrayList(); 
		try {
			Query query = new Query(searchTerm);
			query.count(100);
			QueryResult result = twitter.search(query);
			for (Status status : result.getTweets()) {
//				System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
				res.add(status.getText()); 
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to read the system input.");
		}
		return res; 
	}

}