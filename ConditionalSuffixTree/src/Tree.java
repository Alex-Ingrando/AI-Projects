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
	ArrayList<TreeNode<E>> conditionalPatterns = new ArrayList<TreeNode<E>>();
	ArrayList<E> data;
	
	Tree(){
		l = 0;
		pMin = 0;
		r = 0;
		dataSize = 0;
	}
	
	Tree(int len, double min, double check, ArrayList<E> d){
		l = len;
		pMin = min;
		r = check;
		data = d;
		dataSize = data.size();
		

	}
	
	void train() {
		
		ArrayList<E> pattern = new ArrayList<E>();
		boolean recorded = false;
		int patternLength = l;;
		
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
					//if there are 15 notes, then the possibility for pattern of size one is 15, size 2 is 14
					temp.setPossibleTimes(dataSize - (pattern.size() - 1));
					allPatterns.add(temp);
					//add the element that follows if not the last element
					if(i + pattern.size() < data.size()) {
						temp.addFollowing(data.get(i + pattern.size()));
					}
					//check current pattern against all existing patterns for connection
					for(int m = 0; m < allPatterns.size(); m++) {
						if(checkIfContains(pattern, m) == true) {
							allPatterns.get(m).addConnectedNode(temp);
							temp.setPreceding(true);
						} if(checkIfContained(pattern, m) == true) {
							temp.addConnectedNode(allPatterns.get(m));
							allPatterns.get(m).setPreceding(true);
						}
					}
				} else {
					boolean match = false;
					for(int m= 0; m < allPatterns.size(); m++) {
						match = checkSameNodes(pattern, allPatterns.get(m).getCurrentNode());
						if(match == true) {
							//add the element that follows if not the last element
							if(i + pattern.size() < data.size()) {
								allPatterns.get(m).addFollowing(data.get(i + pattern.size()));
							}
							//update times appeared
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
		eliminateConditional();
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
	
	//pattern is ada and previous pattern is a
	boolean checkIfContains(ArrayList<E> pattern, int i) {
		boolean connect = false;
		//if pattern could contain a previous pattern
		if(pattern.size() > allPatterns.get(i).getCurrentNode().size()) {
			int temp = allPatterns.get(i).getCurrentNode().size();
			//check the end of the pattern to see if it holds a previous pattern
			for(int j = 0; j < allPatterns.get(i).getCurrentNode().size() ; j++) {
				//comparing a & da / da & ada, if false stop checking
				if(allPatterns.get(i).getPatternParts(j) != pattern.get((pattern.size()) - temp)) {
						break;
				} 
				//if last loop ending, and still have not broken than they are connected
				else if (j +1 == allPatterns.get(i).getCurrentNode().size()) {	
					connect = true;
				}
				temp--;
			}
		}
		return connect;
	}
	
	//pattern is b and previous pattern is rb
	boolean checkIfContained(ArrayList<E> pattern, int i) {
		boolean connect = false;
		//if pattern could be contained by a previous pattern
		if(pattern.size() < allPatterns.get(i).getCurrentNode().size()) {
			int temp = pattern.size();
			//check the end of the pattern to see if it holds a previous pattern
			for(int j = 0; j < pattern.size() ; j++) {
				//comparing a & da / da & ada, if false stop checking
				if(pattern.get(j) != allPatterns.get(i).getPatternParts((allPatterns.get(i).getCurrentNode().size()) - temp)) {
						break;
				} 
				//if last loop ending, and still have not broken than they are connected
				else if (j +1 == pattern.size()) {	
					connect = true;
				}
				temp--;
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
		double denom = 0;
		for(int i = 0; i < allPatterns.size(); i ++) {
			if(allPatterns.get(i).getPreceding() == false) {
				allPatterns.get(i).setEmpiricalPassed(true);
				empiricalPatterns.add(allPatterns.get(i));
			} else {
				nom = allPatterns.get(i).getTimes();
				denom = allPatterns.get(i).getPossibleTimes();
				allPatterns.get(i).setEmpirical(nom/denom);
				if(allPatterns.get(i).getEmpirical() > pMin) {
					allPatterns.get(i).setEmpiricalPassed(true);
					empiricalPatterns.add(allPatterns.get(i));
				}
			}
		}
	}
	
	void eliminateConditional() {
		double temp;
		for(int i = 0; i < empiricalPatterns.size(); i++) {
			//have treeNode calculate all conditionals
			empiricalPatterns.get(i).determineConditionalCompare();
		}
		for(int i = 0; i < empiricalPatterns.size(); i ++) {
			//add all single elements patterns to the conditional
			if (empiricalPatterns.get(i).getPreceding() == false) {
				conditionalPatterns.add(empiricalPatterns.get(i));
				//now check each of its connections
				for(int j = 0; j < empiricalPatterns.get(i).getConnectedNodes().size(); j++) {
					//check each conditional of each connected against each conditional of the original if it passed the empirical
					if(empiricalPatterns.get(i).getConnectedNodes().get(j).empiricalPassed == true) {
						for(int k = 0; k < empiricalPatterns.get(i).getConnectedNodes().get(j).getFollowing().size(); k++) {
							for(int m = 0; m < empiricalPatterns.get(i).getFollowing().size(); m++) {
							
								//if same following elements for both patterns and the connections has already passed the empirical test
								if(empiricalPatterns.get(i).getSpecificFollowing(m) == empiricalPatterns.get(i).getConnectedNodes().get(j).getSpecificFollowing(k)) {
									//calculate da -> b / a -> b

									temp = empiricalPatterns.get(i).getConnectedNodes().get(j).getConditionalCompare(k) / empiricalPatterns.get(i).getConditionalCompare(m);
									
									//see if this is the highest conditional
									if(temp > empiricalPatterns.get(i).getConnectedNodes().get(j).getConditional()) {
										empiricalPatterns.get(i).getConnectedNodes().get(j).setConditional(temp, k);
										//System.out.println(empiricalPatterns.get(i).getConnectedNodes().get(j).getCurrentNode() + " has a conditional of " + empiricalPatterns.get(i).getConnectedNodes().get(j).getConditional());
									}
									break; //break this loop as match has been found
								}
							}
						}
					}
					
					//highest conditional has been found for this current connectedNode
					//if it passes r than add to the conditional arrayList
					if(empiricalPatterns.get(i).getConnectedNodes().get(j).getConditional() >= r && empiricalPatterns.get(i).getConnectedNodes().get(j).empiricalPassed == true) {
						conditionalPatterns.add(empiricalPatterns.get(i).getConnectedNodes().get(j));
					}
				}
			}
		}
	}

	void printAllPatterns(){
		System.out.println("All patterns for data: " + data);
		for(int i = 0; i < allPatterns.size(); i++) {
			System.out.println(allPatterns.get(i).getCurrentNode());
		}
	}
	
	void printFirstBranch() {
		System.out.println("All branches for data: " + data);
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
		System.out.println("All empirical patterns for data: " + data);
		for(int i = 0; i < empiricalPatterns.size(); i++) {
			System.out.println(empiricalPatterns.get(i).getCurrentNode());
		}
	}
	
	void printFollowing() {
		System.out.println("All patterns and their following elements for data: " + data);
		for(int i = 0; i < allPatterns.size(); i++) {
			System.out.println(allPatterns.get(i).getCurrentNode()+ "is followed by " + allPatterns.get(i).getFollowing());
		}
	}
	
	void printConditional() {
		System.out.println("All coniditonal patterns for data: " + data);
		for(int i = 0; i < conditionalPatterns.size(); i++) {
			System.out.println(conditionalPatterns.get(i).getCurrentNode());
		}
	}
}