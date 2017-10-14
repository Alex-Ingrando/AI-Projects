import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainBot {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Timer timer = new Timer();
		
		TimerTask hourlyTask = new TimerTask () {
		    
		    public void run () {
		    	TwitterInteraction twitterBot = new TwitterInteraction();
				Tokenizer token = new Tokenizer();
				token.readFile();
				ArrayList<String> data = token.getTokens();
				MarkovChain<String> tweet = new MarkovChain<String>();
				tweet.train(data);
				tweet.generate();
				ArrayList<String> gen = tweet.getGenerated();
				String finishedTweet = "";
				for(int i = 0; i < tweet.getNumElements(); i++) {
					if(i == 0) {
						finishedTweet = gen.get(i);
					} else {
						finishedTweet += gen.get(i);
					} if(i + 1 < tweet.getNumElements()) {
						if(gen.get(i + 1).equals(".") == false && gen.get(i + 1).equals(",") == false) {
							finishedTweet += " ";
						}
					}
				}
				
				twitterBot.updateTwitter(finishedTweet);
		    }
		};
		
		timer.schedule (hourlyTask, 0l, 1000*60*60);
		
		
	}
}

