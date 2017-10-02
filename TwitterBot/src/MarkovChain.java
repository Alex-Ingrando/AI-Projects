import java.util.*;

public class MarkovChain <E> {
	
	ArrayList<E> data;
	ArrayList<Double> dataTally;
	ArrayList<E> generated;

	int[][] tallyAfterNotes;
	int[] sizeAfterNotes;
	double[][] percentage;
	Random rand;
	int numElements;
	
	MarkovChain(){
		data = new ArrayList<E>();
		dataTally = new ArrayList<Double>();
		generated = new ArrayList<E>();
		rand = new Random();
		numElements = 0;
		
	}
	
	void train(E[] fileInput) {
		boolean match = false;
		
		for(int i = 0; i < fileInput.length; i++) {	//will go through each note in the original melody
			match = false;
			for(int j = 0; j < data.size(); j++) { //will check existing tallies for current note
				if(data.get(j) == fileInput[i]) {	//if matched
					dataTally.set(j, dataTally.get(j) + 1);	//add tally
					match = true;								//change check for match
				}
			}
			if(match == false) {							//if no match found, add new element
				data.add(fileInput[i]);
				dataTally.add(1.0);
			}
		}
		
		tallyAfterNotes = new int[data.size()][data.size()]; //declare and initialize arrays for tallys, sizes, and percentages
		sizeAfterNotes = new int[data.size()];
		percentage = new double [data.size()][data.size()];
		
		for (int i = 0; i < data.size(); i++) {		//set all elements of the arrays to zero 
			for(int j = 0; j < data.size(); j++) {
				tallyAfterNotes[i][j] = 0;
				percentage[i][j] = 0;
			}
			sizeAfterNotes[i] = 0;
		} 
		
		for(int i = 0; i < fileInput.length; i++) {		//loop for every note in the original melody
			for(int currentElement = 0; currentElement < data.size(); currentElement++) {	//loop to find current note in the notesInFile arrayList
				if(data.get(currentElement) == fileInput[i] && i != fileInput.length - 1) { //if found and the note is not the last in the melody
					for(int nextNote = 0; nextNote < data.size(); nextNote++) {	//loop to find the next note in the notesInFile array
						if(data.get(nextNote) == fileInput[i+1]) {	//if found
							sizeAfterNotes[currentElement] += 1;					//increment the number of tallies for the current note
							tallyAfterNotes[currentElement][nextNote] += 1;		//increment the number of tallies for the next note coming after the current note
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
		int currentElement = 0;
				
		int cont = rand.nextInt(10) + 1;
		double random;
		double min;
		double max;
		
	
		do {
			generated.add(data.get(currentElement));	//add current note to the producedNotes
			
			random = rand.nextInt(sizeAfterNotes[currentElement]);	// random value to determine the next note
			min = 0;
			max = tallyAfterNotes[currentElement][0];

			
			for(int nextElement = 0; nextElement < data.size(); nextElement++) {
				if(sizeAfterNotes[currentElement] == 0) {			//if note does not have any notes that follow it, choose a random note
					currentElement = rand.nextInt(data.size());
				}else if(random >= min && random <= max) {	//if note matches random, make it the new note
					currentElement = nextElement;
					break;
				} else if(tallyAfterNotes[currentElement][nextElement + 1] != 0) {	//if note didn't match, and the next choice can occur after current note, adjust the min and max values
					min = max + 1;
					max += tallyAfterNotes[currentElement][nextElement + 1];
				}
			}
			cont = rand.nextInt(10) + 1;
			numElements++;
		} while (cont != 9);
	}
	
	public ArrayList<E> getGenerated() {
		return generated;
	}
	
	public int getNumElements() {
		return numElements;
	}
}
