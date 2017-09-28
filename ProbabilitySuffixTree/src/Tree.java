//Alexandra Ingrando
//This class creates the nodes based off of patterns
import java.util.ArrayList;

public class Tree<E> {
	int l;
	double pMin;
	double r;
	int dataSize;
	ArrayList<TreeNode<E>> allPatterns = new ArrayList<TreeNode<E>>();
	ArrayList<TreeNode<E>> empiricalPatterns = new ArrayList<TreeNode<E>>();
	
	Tree(){
		l = 0;
		pMin = 0;
		r = 0;
		dataSize = 0;
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
		dataSize = data.size();
		
		//loop for each element of data, extracting patterns from size 1 to size l
		for(int i = 0; i < data.size(); i ++) {
			
			//boundary check
			if(patternLength + i > data.size()) patternLength--;

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
					temp.setTimes(1);
					allPatterns.add(temp);	
					//check current pattern against all existing patterns for connection
					for(int m = 0; m < allPatterns.size(); m++) {
						if(checkIfConnected(pattern, m) == true) {
							allPatterns.get(m).addConnectedNode(temp);
							temp.setPreceding(true);
						}
					}
				} else {
					boolean match = false;
					for(int m= 0; m < allPatterns.size(); m++) {
						match = checkSameNodes(pattern, allPatterns.get(m).getCurrentNode());
						if(match == true) {
							allPatterns.get(m).setTimes(allPatterns.get(m).getTimes() + 1);
							break;
						}
					}
				}
					

			}
			//clear pattern to prepare for the next element
			pattern.clear();
		}
		checkForOverlappingBranches();
		eliminatePMin();
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
						for(int m = 0; m < temp.size(); m++) {
							//check it until duplicated connection is found
							for(int k = 0; k < allPatterns.get(i).getConnectedNodes().size(); k++) {
								//delete when found
								if(checkSameNodes(allPatterns.get(i).getConnectedNodes().get(k).getCurrentNode(), temp.get(m).getCurrentNode()) == true)
									allPatterns.get(i).deleteConnection(k);
								
							}
						}
					}
				}
				
			}
			delete = false;
		}
	}
	
	void eliminatePMin() {
		double nom= 0;
		double denom = dataSize;
		for(int i = 0; i < allPatterns.size(); i ++) {
			//if the first pattern in the branch
			if(allPatterns.get(i).getPreceding() == false) {
				//find the empirical prob of the current node
				nom = allPatterns.get(i).getTimes();
				allPatterns.get(i).setEmpirical(nom/denom);
				//if it is bigger than pMin, add to the arraylist
				if(allPatterns.get(i).getEmpirical() > pMin) {
					empiricalPatterns.add(allPatterns.get(i));
					//if it has connections, check those of pMin
					if(allPatterns.get(i).getConnectedNodes().isEmpty() == false)
						eliminateConnectedPMin(i, 1);
				}
			}
		}
	}
	
	void eliminateConnectedPMin(int index, int mod) {
		double nom = 0;
		double denom = dataSize - mod;
		//check PMin for each connection
		for(int i = 0; i < allPatterns.get(index).getConnectedNodes().size(); i++) {
			nom = allPatterns.get(index).getConnectedNodes().get(i).getTimes();
			allPatterns.get(index).getConnectedNodes().get(i).setEmpirical(nom/denom);
			//If it passes, add it the the arraylist
			if(allPatterns.get(index).getConnectedNodes().get(i).getEmpirical() > pMin) {
				empiricalPatterns.add(allPatterns.get(index).getConnectedNodes().get(i));
				
				//if it has connections
				if(allPatterns.get(index).getConnectedNodes().get(i).getConnectedNodes().isEmpty() == false) {
					//find the current nodes location in allPatterns, and then check its connections
					for(int j = 0; j < allPatterns.size(); j++) {
						if (checkSameNodes(allPatterns.get(index).getConnectedNodes().get(i).getCurrentNode(), allPatterns.get(j).getCurrentNode()) == true) {
							eliminateConnectedPMin(j, 1);
							break;
							}
						}
					}
				}
			}
	}
	
	void printAllPatterns(){
		for(int i = 0; i < allPatterns.size(); i++) {
			System.out.println(allPatterns.get(i).getCurrentNode());
		}
	}
	
	void printFirstBranch() {
		//check each pattern
		for(int i = 0; i < allPatterns.size(); i++) {
			//if no precedent i.e. first in line
			if(allPatterns.get(i).getPreceding() == false) {
				System.out.println(allPatterns.get(i).getCurrentNode());
				//if there is connections, print the connections
				if(allPatterns.get(i).getConnectedNodes().isEmpty() == false) {
					printConnected(i, "--");
				}
			}
		}
	}
	void printConnected(int index, String arrow) {
		//print each node that is connected
		for(int i = 0; i < allPatterns.get(index).getConnectedNodes().size(); i++) {
			System.out.println(arrow + allPatterns.get(index).getConnectedNodes().get(i).getCurrentNode());
			//if node has connections
			if(allPatterns.get(index).getConnectedNodes().get(i).getConnectedNodes().isEmpty() == false) {
				//find the current nodes location in allPatterns, and then print its connections
				for(int j = 0; j < allPatterns.size(); j++) {
					if (checkSameNodes(allPatterns.get(index).getConnectedNodes().get(i).getCurrentNode(), allPatterns.get(j).getCurrentNode()) == true) {
						printConnected(j, arrow + "--");
						break;
					}
				}
			}
		}
	}
	
	void printEmpirical() {
		for(int i = 0; i < empiricalPatterns.size(); i++) {
			System.out.println(empiricalPatterns.get(i).getCurrentNode());
		}
	}
}