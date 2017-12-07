import java.util.ArrayList;
import java.util.Random;

//returns a verb with corresponding adverb by probability
public class VerbPhrase {
	ArrayList<Word> verbs;
	ArrayList<ArrayList<Word>> adverbs; //double list as many adverbs go with one verb
	
	VerbPhrase(){
		verbs = new ArrayList<Word>();
		adverbs = new ArrayList<ArrayList<Word>>();
	}
	
	//add verbs
	void addVerb(String w) {
		verbs.add(new Word("verb", "", w));
		adverbs.add(new ArrayList<Word>());
	}
	
	//add adverbs to corresponding verbs
	void addAdverb(String v, String w) {
		int verbIndex = 0;
		//looping backwards as the corresponding verb is likely at the end
		for(int i = verbs.size() - 1; i >= 0; i--) {
			if(verbs.get(i).getWord().equals(v)) {
				verbIndex = i;
			}
		}
		//add the adverb to the correct list
		adverbs.get(verbIndex).add(new Word("adverb", v, w));
	}
	
	ArrayList<Word> getVerbPhrase(){
		ArrayList<Word> phrase = new ArrayList<Word>();
		Random rand = new Random();
		
		int verbIndex = rand.nextInt(verbs.size());
		
		//add adverb
		if(rand.nextInt(5) < 3 && adverbs.get(verbIndex).isEmpty() == false) {
			phrase.add(adverbs.get(verbIndex).get(rand.nextInt(adverbs.get(verbIndex).size())));
		}
		
		phrase.add(verbs.get(verbIndex));
		return phrase;
	}
}
