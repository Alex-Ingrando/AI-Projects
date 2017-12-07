import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Sentence {
	PredicatePhrase predicate;
	NounPhrase subject;
	ArrayList<Word> conjunctions;
	Word comma;
	Word period;
	ArrayList<String> subString;
	ArrayList<String> charString;
	
	Sentence(){
		conjunctions = new ArrayList<Word>();
		subString = new ArrayList<String>();
		charString = new ArrayList<String>();
		subject = new NounPhrase();
		predicate = new PredicatePhrase();
		comma = new Word("","",",");
		period = new Word("","",".");
	}
	
	//add subject (sentence specific)
	void addSubject(String w) {
		subject.addProperNoun(w);
		subString.add(w.replace("-", ""));
	}
	//clear out the previous subjects
	void clearSubject() {
		for(int i = 0; i < subString.size(); i++) {
			subject.clearNoun("proper", subString.get(i));
		}
	}
	
	//clear nouns added by user input
	void clearNouns() {
		for(int i = 0; i < charString.size(); i++ ) {
			predicate.clearNouns("proper", charString.get(i));
		}
	}
	
	//add user input nouns
	void addProperNoun(String w) {
		predicate.addDirectObjectNoun("animate", "proper", w);
		predicate.addNounToPrepositions("animate", "proper", w);
		charString.add(w.replace("-", ""));
	}
	
	// sub + pred (+ conj + sub + pred)
	ArrayList<Word> getSentence(){
		ArrayList<Word> phrase = new ArrayList<Word>();
		Word temp;
		Random rand = new Random();
		
		phrase.addAll(subject.getNounPhrase());
		phrase.addAll(predicate.getPredicatePhrase());
		
		if(rand.nextInt(4) < 2) {
			//add comma
			phrase.add(comma);
			//get conjunction and add to the correct place
			temp = conjunctions.get(rand.nextInt(conjunctions.size()));
			if(temp.getType().equals("begin")) {
				phrase.add(0, temp);
			} else {
				phrase.add(temp);
			}
			phrase.addAll(subject.getNounPhrase());
			phrase.addAll(predicate.getPredicatePhrase());
		}
		phrase.add(period);
		return phrase;
	}

	//interprets the format of the file
	void readFile(File file) {
		try {
			Scanner input = new Scanner(file);
			System.out.println("File opened");
			ArrayList<NounPhrase> nounPhrases = new ArrayList<NounPhrase>();
			VerbPhrase verbPhrases = new VerbPhrase();
			String line = "";
			String[] var;
			String type;
			
			while(input.hasNextLine()) {
				line = input.nextLine();
				if(line.equals("conj")) {
					type = input.nextLine();
					line = input.nextLine();
					while(!line.equals("1")) {
						conjunctions.add(new Word("conjunction", type, line));
						line = input.nextLine();
					}
				} else if (line.equals("article") || line.equals("adjective") || line.equals("noun")) {
					String temp = line;
					type = input.nextLine();
					line = input.nextLine();
					while(!line.equals("1")) {
						boolean match = false;
						for(int i = 0; i < nounPhrases.size(); i++) {
							if(type.equals(nounPhrases.get(i).getType())) {
								if (temp.equals("article")) {
									nounPhrases.get(i).addArticle(line);
								} else if (temp.equals("adjective")){
									nounPhrases.get(i).addAdjectives(line);
								} else if (temp.equals("noun")) {
									nounPhrases.get(i).addCommonNoun(line);
								}
								match = true;
							}
						}
						if(match == false) {
							nounPhrases.add(new NounPhrase(type));
							if (temp.equals("article")) {
								nounPhrases.get(nounPhrases.size() - 1).addArticle(line);
							} else if (temp.equals("adjetive")){
								nounPhrases.get(nounPhrases.size() - 1).addAdjectives(line);
							} else if (temp.equals("noun")) {
								nounPhrases.get(nounPhrases.size() - 1).addCommonNoun(line);
							}
						}
						line = input.nextLine();
					}
				} else if (line.equals("preposition")) {
					type = input.nextLine();
					line = input.nextLine();
					predicate.addPreposition(type);
					while(!line.equals("verb")) {
						var = line.split(",");
						for (int i = 0; i < nounPhrases.size(); i++) {
							if(nounPhrases.get(i).getType().equals(var[1]))
								predicate.addPrepositionNounList(type, nounPhrases.get(i));
						}
						line = input.nextLine();
					}
					line = input.nextLine();
					while(!line.equals("1")) {
						var = line.split(",");
						predicate.addPrepositionVerb(type, var[0]);
						verbPhrases.addVerb(var[0]);
						for(int i = 1; i < var.length; i++) {
							predicate.addPrepositionAdverb(type, var[0], var[i]);
							verbPhrases.addAdverb(var[0], var[i]);
						}
						line = input.nextLine();
					}
				} else if (line.equals("direct object")) {
					line = input.nextLine();
					while(!line.equals("verb")) {
						var = line.split(",");
						for (int i = 0; i < nounPhrases.size(); i++) {
							if(nounPhrases.get(i).getType().equals(var[1]))
								predicate.addDirectObjectNounList(nounPhrases.get(i));
						}
						line = input.nextLine();
					}
					line = input.nextLine();
					while(!line.equals("1") && input.hasNextLine()) {
						var = line.split(",");
						predicate.addDirectObjectVerb(var[0]);
						verbPhrases.addVerb(var[0]);
						for(int i = 1; i < var.length; i++) {
							predicate.addDirectObjectAdverb(var[0], var[i]);
							verbPhrases.addAdverb(var[0], var[i]);
						}
						line = input.nextLine();
					}
				}
			}
			predicate.addSimpleVerbs(verbPhrases);
			input.close();
			System.out.println("Reached end of the file");
		} catch (Exception ex) {
			System.out.println("Could not open file");
			ex.printStackTrace();
		}
		
	}
}