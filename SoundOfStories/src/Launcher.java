import java.io.File;
import processing.core.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.midi.*;
import javax.swing.*;

public class Launcher extends PApplet{
	
	PImage bg;
	PFont font;
	Synthesizer midiSynth;
	Instrument[] instr;
	MidiChannel[] mChannels;
	
	boolean menu;
	boolean interaction;
	boolean addSubjects;
	boolean newStory;
	boolean playStory;
	
	int wordNum;
	int syllableNum;
	int sentenceNum;
	int noteDuration;
	int noteLengthMax;
	
	ArrayList<ArrayList<Word>> story;
	ArrayList<String> printSentences;
	Sentence sentence;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("Launcher");
	}
	
	public void settings() {
		size(displayWidth, displayHeight);
		fullScreen();
	}
	
	public void setup() {
		bg = loadImage("paper.jpg");
		sentence = new Sentence();
		File file = new File("C:\\Users\\alexa\\Desktop\\CRCP3330\\CRCP3330\\FinalProject\\bin\\words.txt");
		sentence.readFile(file);
				
		font = createFont("Bradley Hand ITC",10,true);
		menu = true;
		newStory = true;
		story = new ArrayList<ArrayList<Word>>();
		printSentences = new ArrayList<String>();
		try{
			
	        midiSynth = MidiSystem.getSynthesizer(); 
	        midiSynth.open();

	        //get and load default instrument and channel lists
	        instr = midiSynth.getDefaultSoundbank().getInstruments();
	        mChannels = midiSynth.getChannels();

	        midiSynth.loadInstrument(instr[0]);//load an instrument


	      } catch (MidiUnavailableException e) {}
		
		//Unit Tests
		//unitTestWordsAdded();
		//unitTestGrammar();
	}
	
	public void draw() {
		unitTestMusic();
		/*background(255);
		image(bg, 0, 0, displayWidth, displayHeight);
		if(menu == true)
			displayMenu();
		if(interaction == true)
			displayInteraction();
		if(playStory == true) {
			displayStory();
		}*/
		
	}
	
	public void keyPressed() {
		println("keyPressed() called at " + System.currentTimeMillis());
		if (key ==   ' ' && menu == true)   {
			interaction = true;
			menu = false;
		} 
		if (key == 'd' && interaction == true) {
			defaultSubject();
			interaction = false;
		} else if (key == 's' && interaction == true) {
			addSubject();
			interaction = false;
		}
		if(key == ' ' && playStory == true) {
			printSentences.clear();
			story.clear();
			playStory = false;
			menu = true;
			
		}
	}
	
	//start display
	public void displayMenu() {
		fill(0);
		textAlign(CENTER);
		textFont(font, 90);
		text("Sound of Stories", displayWidth / 2, (displayHeight / 2));
		textFont(font, 40);
		text("Literary A Song", displayWidth/2, (displayHeight / 2) + 50);
		text("Press SPACE to start", displayWidth/2, displayHeight - 100);
	}
	
	//display for user input prompts
	public void displayInteraction() {
		textAlign(CENTER);
		textFont(font, 40);
		text("Press 'S' to add your own subject and characters", displayWidth / 2, (displayHeight / 2) + 10);
		text("Press 'D' to continue with the default subject.", displayWidth/2, (displayHeight/2) + 50);
	}
	
	//will add subject to sentence object
	public void addSubject() {
		String cont = "y";
		int subjectNum = 0;
		
		//displays the menu for the subject addition
		while(cont.charAt(0) == 'y' || cont.charAt(0) == 'Y') {
			String name = " ";
			name += JOptionPane.showInputDialog( null, "Type in your subjects name: \n Type it out with dashes between syllables \n For example: Tyler should be typed as Ty-ler");
			//if cancelled, do not continue with prompts
			if(name != null) {
				sentence.addSubject(name);
				subjectNum++;
				cont = JOptionPane.showInputDialog( null, "Press y to add more name's for the subject such as the subjec's pronouns.");
			}
			//if prompt is cancelled
			//if a subject has already been added, go straight to story
			//else go back to the main menu to begin again
			if(name.equals(" ")) {
				cont = "stop";
				if(subjectNum == 0) {
					menu = true;
				}
			}

		} 
		addCharacters();
	}
	
	//will add these as the default subject
	public void defaultSubject() {
		sentence.addSubject(" Hen-ry");
		sentence.addSubject(" he");
		generateStory();
		playStory = true;
	}
	
	//add characters to be direct objects/in prepositions
	public void addCharacters() {
		String name = " ";		
		//displays the menu for the subject addition
		while(!name.equals(" no") && !name.equals(" NO") && !name.equals(" No")) {
			name = " ";
			name += JOptionPane.showInputDialog( null, "Would you like to add characters? \n Type it out with dashes between syllables \n Type 'no' to continue to story");	
			System.out.println("Input is: '" + name + "'");
			if(!name.equals(" no") && !name.equals(" NO")&& !name.equals(" No")  )
				sentence.addProperNoun(name);
		}
		generateStory();
		playStory = true;
	}
	
	//generate a random number of sentences to create the story, make sure code knows that it is no longer 
	//generating a new story
	public void generateStory() {
		Random rand = new Random();
		int lines = 10 + rand.nextInt(5);
		for(int i = 0; i < lines; i++) {
			story.add(sentence.getSentence());
			newStory = false;
		}
		
		wordNum = 0;
		syllableNum = 0;
		sentenceNum = 0;
	}

	//prints sentences syllable by syllable while also playing music
	public void displayStory() {
		
		textAlign(LEFT);
		textFont(font, 35);
		
		if (printSentences.isEmpty() == false) {
			printSentences();
		}
		if(sentenceNum < story.size()) {
			//determine how long each note will be played
			noteLengthMax = (story.get(sentenceNum).get(wordNum).getWord().length() / story.get(sentenceNum).get(wordNum).getNumSyllables());
			if(story.get(sentenceNum).get(wordNum).getSyllables()[syllableNum].length() < story.get(sentenceNum).get(wordNum).getWord().length()) 
				noteLengthMax *= (story.get(sentenceNum).get(wordNum).getSyllables()[syllableNum].length()/story.get(sentenceNum).get(wordNum).getWord().length());
			
			//play the note
			playNote();
			//counters to adjust position in sentence
			if(syllableNum >= story.get(sentenceNum).get(wordNum).getNumSyllables()) {
				wordNum++;
				syllableNum = 0;
			}
		    //add a pause between sentences
			if(wordNum >= story.get(sentenceNum).size()) {
				int delay = 0;
				while (delay < 80) {
					delay++;
				}
				syllableNum = 0;
				wordNum = 0;
				sentenceNum++;
			}
		} else {
			//story has ended, prompt user to start again
			sentence.clearSubject();
			textAlign(RIGHT);
			text("Press SPACE to start over.", displayWidth - 10, displayHeight - 55);
		}
	}
	
	//printing previous sentences
	public void printSentences() {
		for (int i = 0; i < printSentences.size(); i++) {
			int mod = 0;
			if(i >= 6)
				mod = 2;
			if (i >= 10)
				mod = 3;
			text(printSentences.get(i), 10, (40 * i) + 110 + mod);
		}
	}
	
	//print the newest syllable
	public void printNewSyllable() {
		String temp = "";
		if(syllableNum == 0) {
			printSentences.add(temp);
		}
		temp = printSentences.get(sentenceNum) + story.get(sentenceNum).get(wordNum).getSyllables()[syllableNum];
		if (wordNum == 0) temp = temp.substring(0, 2).toUpperCase() + temp.substring(2);
		printSentences.set(sentenceNum, temp);
		int mod = 0;
		if(sentenceNum >= 6)
			mod = 2;
		if (sentenceNum >= 10)
			mod = 3;
		text(printSentences.get(sentenceNum), 10, (40 * sentenceNum) + 110 + mod);
	}
	
	//turn note on and off
	public void playNote() {
		if (noteDuration == 0) {
			printNewSyllable();
			if (!story.get(sentenceNum).get(wordNum).getWord().equals(".") && !story.get(sentenceNum).get(wordNum).getWord().equals(",") && noteDuration == 0) {						
				mChannels[0].noteOn(story.get(sentenceNum).get(wordNum).getSyllableNotes()[syllableNum], 100);		
			}
		}
		
		if (noteDuration >= noteLengthMax) {
			mChannels[0].noteOff(story.get(sentenceNum).get(wordNum).getSyllableNotes()[syllableNum]);
			syllableNum++;
			noteDuration = 0;
		} else {
			noteDuration+= 2;
		}
	}
	
	public void unitTestWordsAdded() {
		System.out.println("Direct Object");
		for(int i = 0; i < sentence.predicate.directObject.noun.size(); i++) {
			System.out.println("  NounPhrase Type:" + sentence.predicate.directObject.noun.get(i).getType());
			System.out.println("    Common Nouns");
			for(int j = 0; j < sentence.predicate.directObject.noun.get(i).commonNouns.size(); j++) {
				System.out.println("      " + sentence.predicate.directObject.noun.get(i).commonNouns.get(j).getWord());
			}
			System.out.println("    Proper Nouns");
			for(int j = 0; j < sentence.predicate.directObject.noun.get(i).properNouns.size(); j++) {
				System.out.println("    " + sentence.predicate.directObject.noun.get(i).properNouns.get(j).getWord());
			}
			System.out.println("    Articles");
			for(int j = 0; j < sentence.predicate.directObject.noun.get(i).articles.size(); j++) {
				System.out.println("    " + sentence.predicate.directObject.noun.get(i).articles.get(j).getWord());
			}
			System.out.println("    Adjectives");
			for(int j = 0; j < sentence.predicate.directObject.noun.get(i).adjectives.size(); j++) {
				System.out.println("    " + sentence.predicate.directObject.noun.get(i).adjectives.get(j).getWord());
			}
		}
		System.out.println("  Verbs");
		for(int i = 0; i < sentence.predicate.directObject.verb.verbs.size(); i++) {
			System.out.println("    " + sentence.predicate.directObject.verb.verbs.get(i).getWord());
			System.out.println("      Adverbs:");
			for(int j = 0; j < sentence.predicate.directObject.verb.adverbs.size(); j++) {
				if(sentence.predicate.directObject.verb.adverbs.get(i).isEmpty() == false)
					System.out.println("        " + sentence.predicate.directObject.verb.adverbs.get(i).get(j).getWord());
			}
		}
		System.out.println("Prepositions");
		for(int i = 0; i < sentence.predicate.preposition.size(); i++) {
			System.out.println("  " + sentence.predicate.preposition.get(i).getPrepositionType());
			for(int j = 0; j < sentence.predicate.preposition.get(i).noun.size(); j++) {
				System.out.println("    NounPhrase Type:" + sentence.predicate.preposition.get(i).noun.get(j).getType());
				System.out.println("      Common Nouns");
				for(int h = 0; h < sentence.predicate.preposition.get(i).noun.get(j).commonNouns.size(); h ++ ) {
					System.out.println("        " + sentence.predicate.preposition.get(i).noun.get(j).commonNouns.get(h).getWord());
				}
				System.out.println("      Proper Nouns");
				for(int h = 0; h < sentence.predicate.preposition.get(i).noun.get(j).properNouns.size(); h ++ ) {
					System.out.println("        " + sentence.predicate.preposition.get(i).noun.get(j).properNouns.get(h).getWord());
				}
				System.out.println("      Articles");
				for(int h = 0; h < sentence.predicate.preposition.get(i).noun.get(j).articles.size(); h ++ ) {
					System.out.println("        " + sentence.predicate.preposition.get(i).noun.get(j).articles.get(h).getWord());
				}
				System.out.println("      Adjectives Nouns");
				for(int h = 0; h < sentence.predicate.preposition.get(i).noun.get(j).adjectives.size(); h ++ ) {
					System.out.println("        " + sentence.predicate.preposition.get(i).noun.get(j).adjectives.get(h).getWord());
				}
				
			}
			System.out.println("  Verbs");
			for(int j = 0; j < sentence.predicate.preposition.get(i).verb.verbs.size(); j++) {
				System.out.println("    " + sentence.predicate.preposition.get(i).verb.verbs.get(j).getWord());
				System.out.println("      Adverbs:");
				for(int k = 0; k < sentence.predicate.preposition.get(i).verb.adverbs.get(j).size(); k++) {
					if(sentence.predicate.preposition.get(i).verb.adverbs.get(j).isEmpty() == false) {
						System.out.println("        " + sentence.predicate.preposition.get(i).verb.adverbs.get(j).get(k).getWord());
				
					}
				}
			}
		}
		System.out.println("Single Verbs");
		System.out.println("  Verbs:");
		for(int i = 0; i < sentence.predicate.simpleVerb.verbs.size(); i++) {
			System.out.println(sentence.predicate.simpleVerb.verbs.get(i).getWord());
			System.out.println("    Adverbs:");
			for(int j = 0; j < sentence.predicate.simpleVerb.adverbs.get(i).size(); j++) {
				System.out.println("      " + sentence.predicate.simpleVerb.adverbs.get(i).get(j).getWord());
			}
			
		}
	}
	
	public void unitTestGrammar() {
		sentence.addSubject("Bob");
		ArrayList<Word> temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		sentence.clearNouns();
		sentence.clearSubject();
		sentence.addSubject("Bob");
		temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		temp = sentence.getSentence();
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i).getWord());
		}
		sentence.clearNouns();
		sentence.clearSubject();
		
	}
	
	public void unitTestMusic() {
		Word abc = new Word("", "", "abc");
		int time = 0;
		if(time == 0) {
			for(int i = 0; i < abc.getNumSyllables(); i++) {
				System.out.println(abc.getSyllables()[i]);
				mChannels[0].noteOn(abc.getSyllableNotes()[i], 100);
				System.out.println("note on");
				try { Thread.sleep(1000); // wait time in milliseconds to control duration
		        } catch( InterruptedException e ) { }
				mChannels[0].noteOff(abc.getSyllableNotes()[i]);
				System.out.println("note off");
			}
			time++;
		}
		if(time == 1) {
			Word abcd = new Word("", "", "abcd");
			for(int i = 0; i < abcd.getNumSyllables(); i++) {
				System.out.println(abcd.getSyllables()[i]);
				mChannels[0].noteOn(abcd.getSyllableNotes()[i], 100);
				System.out.println("note on");
				try { Thread.sleep(1000); // wait time in milliseconds to control duration
		        } catch( InterruptedException e ) { }
				mChannels[0].noteOff(abcd.getSyllableNotes()[i]);
				System.out.println("note off");
			}
			time++;
		}
		if(time == 2) {
			Word abcde = new Word("", "", "abc-de");
			for(int i = 0; i < abcde.getNumSyllables(); i++) {
				System.out.println(abcde.getSyllables()[i]);
				mChannels[0].noteOn(abcde.getSyllableNotes()[i], 100);
				System.out.println("note on");
				try { Thread.sleep(1000); // wait time in milliseconds to control duration
		        } catch( InterruptedException e ) { }
				mChannels[0].noteOff(abcde.getSyllableNotes()[i]);
				System.out.println("note off");
			}
		}
		
	}
}
