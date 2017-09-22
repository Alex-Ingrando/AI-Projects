//Alexandra Ingrando
//This class creates the nodes based off of patterns
import java.util.ArrayList;

public class Tree<E> {
	int l;
	double pMin;
	double r;
	ArrayList<TreeNode<E>> allPatterns = new ArrayList<TreeNode<E>>();
	
	Tree(){
		l = 0;
		pMin = 0;
		r = 0;
	}
	
	Tree(int len, double min, double check){
		l = len;
		pMin = min;
		r = check;
	}
	
	void train(ArrayList<E> data) {
		
		ArrayList<E> pattern = new ArrayList<E>();
		boolean recorded = false;
		int patternLength = l;
		
		//loop for each element of data, extracting patterns from size 1 to size l
		for(int i = 0; i < data.size(); i ++) {
			
			//boundary check
			if(patternLength + i >= data.size()) patternLength--;

			for(int j = 0; j < patternLength; j++) {
				//create a new node for each one of the pattern
				TreeNode<E> temp = new TreeNode<E>();
				
				//add elements to pattern until of size l
				pattern.add(data.get(i + j));
				
				//check to see if the pattern already exists
				recorded = checkRecorded(pattern);

				
				//if the pattern does not exist yet
				if (recorded == false) {	
					//pass pattern at each lengths from 1-l to a new node
					temp.setCurrentNode(pattern);
					allPatterns.add(temp);	
					//check current pattern against all existing patterns for connection
					for(int m = 0; m < allPatterns.size(); m++) {
						if(checkIfConnected(pattern, m) == true) {
							allPatterns.get(m).addConnectedNode(temp);
							temp.setPreceding(true);
						}
					}
				}

			}
			//clear pattern to prepare for the next element
			pattern.clear();
		}
		checkForOverlappingBranches();
	}
	
	void printAllPatterns(){
		for(int i = 0; i < allPatterns.size(); i++) {
			System.out.println(allPatterns.get(i).getCurrentNode());
		}
	}
	
	void printConnectedPatterns() {
		for(int i = 0; i < allPatterns.size(); i ++) {
			//if has connections, but no preceding (i.e. not printed yet)
			if(allPatterns.get(i).getConnectedNodes().isEmpty() == false && allPatterns.get(i).getPreceding() == false) {
				//print current node
				System.out.println(allPatterns.get(i).getCurrentNode());
				for(int j = 0; j < allPatterns.get(i).getConnectedNodes().size(); j++) {
					System.out.println("-->" + allPatterns.get(i).getConnectedNodes().get(j).getCurrentNode());
					//if this connected node has connections, print them
					if (allPatterns.get(i).getConnectedNodes().get(j).getConnectedNodes().isEmpty() != true) {
						for(int k = 0; k < allPatterns.get(i).getConnectedNodes().get(j).getConnectedNodes().size(); k++) {
							System.out.println("---->" + allPatterns.get(i).getConnectedNodes().get(j).getConnectedNodes().get(k).getCurrentNode());
						}
					}
				}
			}
		}
	}
	
	boolean checkSameNodes(ArrayList<E> one, ArrayList<E> two) {
		boolean same = false;
		
		//if same size
		if(one.size() == two.size()) {
			//check each element of node
			for(int i = 0; i < one.size(); i++) {
				//if difference break the loop
				if(one.get(i) != two.get(i)) break;
				//if last element, and still in the loop, they are the same
				else if(i + 1 == one.size()) same = true;
			}
		}
		
		return same;
	}
	
	boolean checkRecorded(ArrayList<E> pattern) {
		boolean found = false;
		
		for(int k= 0; k < allPatterns.size(); k++) {
			found = checkSameNodes(allPatterns.get(k).getCurrentNode(), pattern);
			if(found == true) break;
		}
		
		return found;
	}
	
	boolean checkIfConnected(ArrayList<E> pattern, int i) {
		boolean connect = false;
		//if pattern could contain a previous pattern
		if(pattern.size() > allPatterns.get(i).getCurrentNode().size()) {
			//check each element of the smaller pattern to the new pattern
			for(int j = 0; j < allPatterns.get(i).getCurrentNode().size(); j++) {
				if(allPatterns.get(i).getPatternParts(j) != pattern.get(j)){
					break;
				} else if (j + 1 == allPatterns.get(i).getCurrentNode().size()) {
					connect = true;
				}
			}
		}
		return connect;
	}
	

	
	void checkForOverlappingBranches() {
		
		boolean delete = false;
		ArrayList<TreeNode<E>> temp = new ArrayList<TreeNode<E>>();
		//check for each pattern
		for(int i = 0; i < allPatterns.size(); i++) {
			//if the pattern has connections
			if(allPatterns.get(i).getConnectedNodes().isEmpty() == false) {
				//check each connection
				for(int j = 0; j < allPatterns.get(i).getConnectedNodes().size(); j++) {
					//if connected node also has connections
					if(allPatterns.get(i).getConnectedNodes().get(j).getConnectedNodes().isEmpty() == false) {
						//store the elements it is connected to 
						temp = allPatterns.get(i).getConnectedNodes().get(j).getConnectedNodes();
						delete = true;
					}
					//if connection was found
					if(delete == true) {
						//for each node it is connected to
						for(int l = 0; l < temp.size(); l++) {
							//check it until duplicated connection is found
							for(int k = 0; k < allPatterns.get(i).getConnectedNodes().size(); k++) {
								//delete when found
								if(checkSameNodes(allPatterns.get(i).getConnectedNodes().get(k).getCurrentNode(), temp.get(l).getCurrentNode()) == true)
									allPatterns.get(i).deleteConnection(k);
								
							}
						}
					}
				}
				
			}
			delete = false;
		}
	}
}
