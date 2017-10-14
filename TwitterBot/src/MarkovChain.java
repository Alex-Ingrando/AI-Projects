import java.util.*;

public class MarkovChain <E> {
	
	ArrayList<E> data;
	ArrayList<Double> dataTally;
	ArrayList<E> generated;

	int[][] tallyAfter;
	int[] sizeAfter;
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
	
	void train(ArrayList<E> fileInput) {
		boolean match = false;
		
		for(int i = 0; i < fileInput.size(); i++) {	//will go through each element in the original data
			match = false;
			for(int j = 0; j < data.size(); j++) { //will check existing tallies for current note
				if(data.get(j).equals(fileInput.get(i))) {	//if matched
					dataTally.set(j, dataTally.get(j) + 1);	//add tally
					match = true;								//change check for match
				}
			}
			if(match == false) {							//if no match found, add new element
				data.add(fileInput.get(i));
				dataTally.add(1.0);
			}
		}
		
		tallyAfter = new int[data.size()][data.size()]; //declare and initialize arrays for tallys, sizes, and percentages
		sizeAfter = new int[data.size()];
		percentage = new double [data.size()][data.size()];
		
		for (int i = 0; i < data.size(); i++) {		//set all elements of the arrays to zero 
			for(int j = 0; j < data.size(); j++) {
				tallyAfter[i][j] = 0;
				percentage[i][j] = 0;
			}
			sizeAfter[i] = 0;
		} 
		
		for(int i = 0; i < fileInput.size(); i++) {		//loop for every element for the original data
			for(int currentElement = 0; currentElement < data.size(); currentElement++) {	//loop to find current element in the data arrayList
				if(data.get(currentElement).equals(fileInput.get(i)) && i != fileInput.size() - 1) { //if found and the element is not the last in the data
					for(int nextElement = 0; nextElement < data.size(); nextElement++) {	//loop to find the next element in the data array
						if(data.get(nextElement).equals(fileInput.get(i + 1))) {	//if found
							sizeAfter[currentElement] += 1;					//increment the number of tallies for the current element
							tallyAfter[currentElement][nextElement] += 1;		//increment the number of tallies for the next element coming after the current element
							//System.out.println(data.get(nextElement) + " happens " + tallyAfter[currentElement][nextElement] + " times after " + data.get(currentElement));
							//System.out.println(data.get(currentElement) + " occurs " + sizeAfter[currentElement] + " times.");
						}
					}
				}
			}	
		}
	}
	
	
	void generate() {
		//determine the seed
		int currentElement = 0;
				
		int cont = rand.nextInt(10) + 1;
		double random;
		double min;
		double max;
		String period = ".";
		int counter = 0;		
		generated.add(data.get(currentElement));	//add first element to the generated
		numElements = 1;
		
		do {
			random = rand.nextInt(sizeAfter[currentElement]);	// random value to determine the next element
			min = 0;
			max = tallyAfter[currentElement][0];
			
			for(int nextElement = 0; nextElement < data.size(); nextElement++) {
				//make sure element actually occurs ever
				if(tallyAfter[currentElement][nextElement] != 0) {
					//if element matches random, make it the new element
					if(random >= min && random <= max) {
						currentElement = nextElement;
						//add current element to the generated
						generated.add(data.get(currentElement));
						numElements++;
						break;
					} else {
						//adjust the min and max for the next word if not the match
						min = max + 1;
					}
				}
				max += tallyAfter[currentElement][nextElement + 1];
			}
			
		} while (data.get(currentElement).equals(period) != true);
		
		System.out.println(generated);
		
	}
	
	public ArrayList<E> getGenerated() {
		return generated;
	}
	
	public int getNumElements() {
		return numElements;
	}
}
