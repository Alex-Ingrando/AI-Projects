//holds a words grammatic information
public class Word {
	String partOfSpeech;
	String type;
	String word;
	String[] wordSyllables;
	int numSyllables;
	int[] syllableNote;
	
	Word(String p, String t, String w){
		partOfSpeech = p;
		type = t;
		word = w;
		
		//remove dash from words and use it to store the indivitual syllables
		wordSyllables = word.split("-");
		word = word.replace("-", "");
		numSyllables = wordSyllables.length;
		syllableNote = new int[numSyllables];
		
		//calculate the note for each syllable
		for(int i = 0; i < numSyllables; i++) {
			syllableNote[i] = 0;
			for(int j = 0; j < wordSyllables[i].length(); j++) {
				syllableNote[i] += (int) wordSyllables[i].charAt(j);
			}
			syllableNote[i] = syllableNote[i] / ((wordSyllables[i].length() * 3) / 2);
			while (syllableNote[i] > 127) {
				syllableNote[i] = syllableNote[i] / 2;
			}
		}
	}
	
	void setWord(String w) {
		word = w;
	}
	
	String getWord() {
		return word;
	}
	
	String getType() {
		return type;
	}
	
	String[] getSyllables() {
		return wordSyllables;
	}
	
	int[] getSyllableNotes() {
		return syllableNote;
	}
	
	int getNumSyllables() {
		return numSyllables;
	}

}