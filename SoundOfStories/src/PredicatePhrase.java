import java.util.ArrayList;
import java.util.Random;

//Will return either a plain predicate, a verb with a preposition, or a verb with a direct object
public class PredicatePhrase {
	
	ArrayList<PrepositionPhrase> preposition;
	DirectObjectPhrase directObject;
	VerbPhrase simpleVerb;
	
	PredicatePhrase(){
		preposition = new ArrayList<PrepositionPhrase>();
		directObject = new DirectObjectPhrase();
		simpleVerb = new VerbPhrase();
	}
	
	void addPreposition(String p) {
		preposition.add(new PrepositionPhrase(p));
	}
	
	//will add a preposition specific verb
	void addPrepositionVerb(String p, String v) {
		for(int i = 0; i < preposition.size(); i++) {
			if(preposition.get(i).getPrepositionType().equals(p)) {
				preposition.get(i).addVerb(v);
			}
		}
		
	}
	
	//will add a verb specific adverb to the verb's preposition
	void addPrepositionAdverb(String p, String v, String a) {
		for(int i = 0; i < preposition.size(); i++) {
			if(preposition.get(i).getPrepositionType().equals(p)) {
				preposition.get(i).addAdverb(v, a);
			}
		}
	}
	
	//add preposition specific nouns of a certain type
	void addPrepositionNounList(String p, NounPhrase n) {
		for(int i = 0; i < preposition.size(); i++) {
			if(preposition.get(i).getPrepositionType().equals(p)) {
				preposition.get(i).addNounList(n);
			}
		}
	}
	
	//add single preposition specific noun
	void addPrepositionNoun(String p, String t, String pc, String n) {
		for(int i = 0; i < preposition.size(); i++) {
			if(preposition.get(i).getPrepositionType().equals(p)) {
				preposition.get(i).addNoun(t, pc, n);
			}
		}
		
	}
	
	//determine the noun's preposition, then add it
	void addNounToPrepositions(String t, String pc, String n) {
		for(int i = 0; i < preposition.size(); i++) {
			for(int j = 0; j < preposition.get(i).getNounTypes().size(); j++) {
				if(preposition.get(i).getNounTypes().get(j).equals(t)) {
					preposition.get(i).addNoun(t, pc, n);
					break;
				}
			}
		}
		
	}
	
	//clear user input nouns
	void clearNouns(String t, String w) {
		for(int i = 0; i < preposition.size(); i++) {
			preposition.get(i).clearNouns(t, w);
		}
		directObject.clearNouns(t,w);
	}
	
	//same as above, but direct object specific
	void addDirectObjectNounList(NounPhrase n) {
		directObject.addNounList(n);
	}
	
	void addDirectObjectNoun(String t, String pc, String n) {
		directObject.addNoun(t, pc, n);
	}
	
	void addDirectObjectVerb(String v) {
		directObject.addVerb(v);
	}
	
	void addDirectObjectAdverb(String v, String a) {
		directObject.addAdverb(v, a);
	}
	
	void addSimpleVerbs(VerbPhrase v) {
		simpleVerb = v;
	}
	
	ArrayList<Word> getPredicatePhrase(){
		ArrayList<Word> phrase = new ArrayList<Word>();
		Random rand = new Random();
		int predicate = rand.nextInt(10);
		
		if(predicate < 5) {
			phrase.addAll(preposition.get(rand.nextInt(preposition.size())).getPrepositionPhrase());
		} else if (predicate < 8) {
			phrase.addAll(directObject.getDirectObjectPhrase());
		} else {
			phrase.addAll(simpleVerb.getVerbPhrase());
		}
		return phrase;
	}

}
