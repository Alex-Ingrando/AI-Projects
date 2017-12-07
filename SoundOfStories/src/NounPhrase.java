import java.util.ArrayList;
import java.util.Random;

public class NounPhrase {
	ArrayList<Word> commonNouns;
	ArrayList<Word> repeatedNouns; //only common nouns
	ArrayList<Word> properNouns;
	ArrayList<Word> articles;
	ArrayList<Word> adjectives;
	String type;
	
	NounPhrase(){
		commonNouns = new ArrayList<Word>();
		properNouns = new ArrayList<Word>();
		repeatedNouns = new ArrayList<Word>();
		articles = new ArrayList<Word>();
		adjectives = new ArrayList<Word>();
	}
	
	NounPhrase(String t){
		commonNouns = new ArrayList<Word>();
		properNouns = new ArrayList<Word>();
		repeatedNouns = new ArrayList<Word>();
		articles = new ArrayList<Word>();
		adjectives = new ArrayList<Word>();
		type = t;
	}
	
	//getter
	String getType() {
		return type;
	}
	
	//setters to add new words belonging to a noun phrase
	void addCommonNoun(String w) {
		commonNouns.add(new Word("noun", type, w));
	}
	
	void addProperNoun(String w) {
		properNouns.add(new Word("noun", type, w));
	}
	
	void addNoun(String t, String w) {
		if(t.equals("proper"))
			properNouns.add(new Word("noun", type, w));
		else if (t.equals("common"))
			commonNouns.add(new Word("noun", type, w));
	}
	
	void addArticle(String w) {
		articles.add(new Word("article", type, w));
	}
	
	void addAdjectives(String w) {
		adjectives.add(new Word("adjectives", type, w));
	}
	
	ArrayList<Word> getNounPhrase(){
		ArrayList<Word> phrase = new ArrayList<Word>();
		Random rand = new Random();
		int commonProbability = rand.nextInt(5);
		
		//determine if adding a proper noun phrase or a common noun phrase
		if((commonProbability < 3 && commonNouns.isEmpty() == false) || properNouns.isEmpty() == true) {
			//if common noun, add an article, possibly an adjective, and a common noun
			//either get a previous noun, or a new one
			Word temp;
			if(repeatedNouns.isEmpty() == false) {
				if(rand.nextInt(5) < 3) {
					phrase.add(repeatedNouns.get(rand.nextInt(repeatedNouns.size())));
				} else {
					phrase.add(commonNouns.get(rand.nextInt(commonNouns.size())));
					repeatedNouns.add(phrase.get(0));
				}
			} else {
				phrase.add(commonNouns.get(rand.nextInt(commonNouns.size())));
			    repeatedNouns.add(phrase.get(0));			
			}
			
			//determine if adjective will be added or not
			if(rand.nextInt(5) < 2) {
				phrase.add(0, adjectives.get(rand.nextInt(adjectives.size())));
			}
			temp = articles.get(rand.nextInt(articles.size()));
		
			//check for either adjective or noun in case a 'a' and 'an'
			if(phrase.get(0).getWord().charAt(1) == 'a' || 
			   phrase.get(0).getWord().charAt(1) == 'e' || 
			   phrase.get(0).getWord().charAt(1) == 'i' || 
			   phrase.get(0).getWord().charAt(1) == 'o' || 
			   phrase.get(0).getWord().charAt(1) == 'u') {
				while(temp.getWord().charAt(temp.getWord().length() - 1) == 'a') {
					temp = articles.get(rand.nextInt(articles.size()));
				}
			} else {
				while(temp.getWord().charAt(temp.getWord().length() - 1) == 'n') {
					temp = articles.get(rand.nextInt(articles.size()));
				}
			}

			phrase.add(0,temp);
		} else {
			phrase.add(properNouns.get(rand.nextInt(properNouns.size())));
		}
		return phrase;
	}
	
	//remove nouns from either proper or common arrayList
	void clearNoun(String type, String word) {
		if(type.equals("proper")) {
			for(int i = 0; i < properNouns.size(); i++) {
				if(properNouns.get(i).getWord().equals(word))
					properNouns.remove(i);
			}
		} else if (type.equals("common")) {
			for(int i = 0; i < commonNouns.size(); i++) {
				if(commonNouns.get(i).getWord().equals(word))
					commonNouns.remove(i);
			}
		}
	}
	
	void clearRepeatedNouns() {
		repeatedNouns.clear();
	}
	
}
