import java.util.*;

public class MarkovChain {
	
	ArrayList<Integer> notesInFile;
	ArrayList<Double> tallyNotes;
	ArrayList<Integer> producedNotes;
	int[][] tallyAfterNotes;
	int[] sizeAfterNotes;
	double[][] percentage;
	Random rand;
	int numNotes;
	
	MarkovChain(){
		notesInFile = new ArrayList<Integer>();
		tallyNotes = new ArrayList<Double>();
		producedNotes = new ArrayList<Integer>();
		rand = new Random();
		numNotes = 0;
		
	}
	
	void train(int[] originalNotes) {
		boolean match = false;
		
		for(int i = 0; i < originalNotes.length; i++) {	//will go through each note in the original melody
			match = false;
			for(int j = 0; j < notesInFile.size(); j++) { //will check existing tallies for current note
				if(notesInFile.get(j) == originalNotes[i]) {	//if matched
					tallyNotes.set(j, tallyNotes.get(j) + 1);	//add tally
					match = true;								//change check for match
				}
			}
			if(match == false) {							//if no match found, add new element
				notesInFile.add(originalNotes[i]);
				tallyNotes.add(1.0);
			}
		}
		
		tallyAfterNotes = new int[notesInFile.size()][notesInFile.size()]; //declare and initialize arrays for tallys, sizes, and percentages
		sizeAfterNotes = new int[notesInFile.size()];
		percentage = new double [notesInFile.size()][notesInFile.size()];
		
		for (int i = 0; i < notesInFile.size(); i++) {		//set all elements of the arrays to zero 
			for(int j = 0; j < notesInFile.size(); j++) {
				tallyAfterNotes[i][j] = 0;
				percentage[i][j] = 0;
			}
			sizeAfterNotes[i] = 0;
		} 
		
		for(int i = 0; i < originalNotes.length; i++) {		//loop for every note in the original melody
			for(int currentNote = 0; currentNote < notesInFile.size(); currentNote++) {	//loop to find current note in the notesInFile arrayList
				if(notesInFile.get(currentNote) == originalNotes[i] && i != originalNotes.length - 1) { //if found and the note is not the last in the melody
					for(int nextNote = 0; nextNote < notesInFile.size(); nextNote++) {	//loop to find the next note in the notesInFile array
						if(notesInFile.get(nextNote) == originalNotes[i+1]) {	//if found
							sizeAfterNotes[currentNote] += 1;					//increment the number of tallies for the current note
							tallyAfterNotes[currentNote][nextNote] += 1;		//increment the number of tallies for the next note coming after the current note
							//System.out.println(notesInFile.get(nextNote) + " comes after " + notesInFile.get(currentNote) + " " + tallyAfterNotes[currentNote][nextNote] + " times.");
						}
					}
				}
			}	
		}
		
		/*for(int currentNote = 0; currentNote < notesInFile.size(); currentNote++) {	//loop to find the percent that every note comes after a specific note
			for(int nextNote = 0; nextNote < notesInFile.size(); nextNote++) {
				percentage[currentNote][nextNote] = tallyAfterNotes[currentNote][nextNote] / sizeAfterNotes[currentNote] * 100;
				//System.out.println(notesInFile.get(nextNote) + " comes after " + notesInFile.get(currentNote) + " " + percentage[currentNote][nextNote] + " % of the time.");
			}
		}*/
	}
	
	
	void generate() {
		//determine the seed
		int currentNote = 0;
				
		int cont = rand.nextInt(10) + 1;
		double random;
		double min;
		double max;
		
	
		do {
			producedNotes.add(notesInFile.get(currentNote));	//add current note to the producedNotes
			
			random = rand.nextInt(sizeAfterNotes[currentNote]);	// random value to determine the next note
			min = 0;
			max = tallyAfterNotes[currentNote][0];

			
			for(int nextNote = 0; nextNote < notesInFile.size(); nextNote++) {
				if(sizeAfterNotes[currentNote] == 0) {			//if note does not have any notes that follow it, choose a random note
					currentNote = rand.nextInt(notesInFile.size());
				}else if(random >= min && random <= max) {	//if note matches random, make it the new note
					currentNote = nextNote;
					break;
				} else if(tallyAfterNotes[currentNote][nextNote + 1] != 0) {	//if note didn't match, and the next choice can occur after current note, adjust the min and max values
					min = max + 1;
					max += tallyAfterNotes[currentNote][nextNote + 1];
				}
			}
			cont = rand.nextInt(10) + 1;
			numNotes++;
		} while (cont != 9);
	}
	
	public ArrayList<Integer> getProducedNotes() {
		return producedNotes;
	}
	
	public int getNumNotes() {
		return numNotes;
	}
}
