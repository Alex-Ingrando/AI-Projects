import java.util.ArrayList;
import java.util.Random;

public class PrepositionPhrase {
	ArrayList<NounPhrase> noun;
	ArrayList<String> nounTypes;
	VerbPhrase verb;
	Word preposition;
	
	PrepositionPhrase(){
		noun = new ArrayList<NounPhrase>();
		verb = new VerbPhrase();
		nounTypes = new ArrayList<String>();
	}
	
	PrepositionPhrase(String p){
		preposition = new Word("preposition", "", p);
		noun = new ArrayList<NounPhrase>();
		verb = new VerbPhrase();
		nounTypes = new ArrayList<String>();
	}
	
	void setPreposition(String p) {
		preposition = new Word("preposition", "", p);
	}
	
	String getPrepositionType() {
		return preposition.getWord();
	}
	
	ArrayList<String> getNounTypes(){
		return nounTypes;
	}
	
	void addNounList(NounPhrase n) {
		noun.add(n);
		nounTypes.add(n.getType());
	}
	
	//add a single noun to one of the existing lists
	void addNoun(String t, String pc, String n) {
		nounTypes.add(t);
		for(int i = 0; i < noun.size(); i++) {
			if(noun.get(i).getType().equals(t)) {
				noun.get(i).addNoun(pc, n);	
			}
				
		}
	}
	
	void clearNouns(String t, String n) {
		for(int i = 0; i < noun.size(); i++) {
			noun.get(i).clearNoun(t, n);
			noun.get(i).repeatedNouns.clear();
		}
	}
	
	void addVerb(String v) {
		verb.addVerb(v);
	}
	
	void addAdverb(String v, String a) {
		verb.addAdverb(v, a);
	}
	
	ArrayList<Word> getPrepositionPhrase() {
		ArrayList<Word> phrase = new ArrayList<Word>();
		Random rand = new Random();
		
		phrase.addAll(verb.getVerbPhrase());
		phrase.add(preposition);
		phrase.addAll(noun.get(rand.nextInt(noun.size())).getNounPhrase());
		
		return phrase;
	}
}
