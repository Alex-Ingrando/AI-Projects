//Alexandra Ingrando
//This class creates the nodes based off of patterns
import java.util.ArrayList;
import java.util.*;

public class Tree<E> {
	int l;
	double pMin;
	double r;
	int dataSize;
	double g;
	ArrayList<TreeNode<E>> allPatterns = new ArrayList<TreeNode<E>>();
	ArrayList<TreeNode<E>> empiricalPatterns = new ArrayList<TreeNode<E>>();
	ArrayList<TreeNode<E>> conditionalPatterns = new ArrayList<TreeNode<E>>();
	ArrayList<E> generated = new ArrayList<E>();
	ArrayList<E> data;
	TreeNode<E> root = new TreeNode<E>();
	
	Tree(){
		l = 0;
		pMin = 0;
		r = 0;
		dataSize = 0;
	}
	
	Tree(int len, double min, double check, ArrayList<E> d, double gSmooth){
		l = len;
		pMin = min;
		r = check;
		data = d;
		dataSize = data.size();
		g = gSmooth;

	}
	
	void train() {
		boolean recorded;
		int patternLength;
		E follow;
		ArrayList<E> pattern = new ArrayList<E>();
		ArrayList<E> singles = new ArrayList<E>();
		for(int i = 0; i < l; i++) {
			patternLength = i + 1;
			//System.out.println("Searching for patterns of size " + patternLength);
			for(int j = 0; j < data.size(); j++) {
				if (j + patternLength <= data.size()) {
					recorded = false;
					follow = null;
					
					for(int h = 0; h < patternLength; h++) {
						pattern.add(data.get(j + h));
					}
					if (j + patternLength < data.size()) {
						follow = data.get(j + patternLength);
					}
					
					recorded = checkRecord(pattern, root, follow);
					//System.out.println(pattern);
					if(recorded != true) {
						addTreeNode(pattern, root, 1, follow, singles);
						
					}
					
				}
				pattern.clear();
			}
			//finished finding all one length elements
			if(i == 0) {
				for(int j = 0; j < root.getConnectedNodes().size(); j++) {
					singles.add(root.getConnectedNodes().get(j).getCurrentNode().get(0));
				}
				for(int j = 0; j < root.getConnectedNodes().size(); j++) {
					root.getConnectedNodes().get(j).resetFollowing(singles);
				}
			}
		}
		System.out.println("All patterns");
		printTree(root, "");
		
		System.out.println("Empirical Patterns");
		root.testEmpirical(pMin, data.size());
		printTree(root, "");
		
		System.out.println("Conditional Patterns");
		root.testConditional(r);
		printTree(root, "");
	}
	
	void generate() {
		root.smooth(g, dataSize);
		TreeNode<E> lastNode = null;
		TreeNode<E> currentNode = null;
		E seed = root.getSeed();
		E current = seed;
		
		generated.add(seed);
		
		boolean infinite;

		Random rand = new Random();
		int counter = 0;
		while(counter < 20) {
			counter++;
			
			//find the seed/currentPattern's matching node
			//if no match is found, will return null
			
			ArrayList<E> pattern = new ArrayList<E>();
			currentNode = null;
			
			//checking the biggest possible patterns first, if found it will break, but if it returns null
			//thus not found, it will decrease the pattern size
			for(int i = l; i > 0 && currentNode == null; i--) {
				pattern.clear();
				if(generated.size() >= i) {
					for(int j = i; j > 0; j--) {
						pattern.add(generated.get(generated.size() - j));
					}
					currentNode = matchNode(pattern, root);
				}
			}

			
			//if no match found, and thus can't find the next element, rollback or generate from empty
			if (currentNode == null) {
				//determine if generate from empty or rollback
				int emptyContext = rand.nextInt(5);
					
				//generate from empty (check for if not possible to rollback)
				if(emptyContext <= 3 || lastNode != null) {
					pattern.clear();
					current = root.getSeed();
					pattern.add(current);
					currentNode = matchNode(pattern, root);
					System.out.println("EmptyContext");
				} else if (emptyContext > 3) { //rollback to the last recordedNode
					currentNode = lastNode;
					System.out.println("RollBack");
				}		
			}
			
			//when the matching node is found to generate the next one, generate and store the node generated from
			current = currentNode.getNext();
			lastNode = currentNode;
			
			ArrayList<E> recurring = new ArrayList<E>();
			int patternSize;
			for(int i = 0; i < l; i++) {
				recurring.clear();
				//2 for length 1, 5 for length 2, etc
				if(generated.size() > 2 + (3 * i)) {
					for(int j = i; j > 0; j--) {
						recurring.add(generated.get(generated.size() - j));
					}
					recurring.add(current);
					infinite = checkInfiniteLooping(recurring, (i + 1), 2);
					if(infinite == true) {
						//compare current to last element of recurring as it is the element yet to be added
						while(current == recurring.get(recurring.size() - 1)) {
							currentNode = lastNode;
							current = currentNode.getNext();
						}
					}
				}
			}
			
			generated.add(current);
		}
		
		System.out.println(generated);
	}
	
	
	TreeNode<E> matchNode(ArrayList<E> pattern, TreeNode<E> base){
		TreeNode<E> match = null;
		int compareSize;
		boolean recorded;
		int patternSize;
		//check each of the connected nodes of the base to see if pattern matches, or is contained
		for(int i = 0; i < base.getConnectedNodes().size(); i++) {
			//compare each element of the pattern from the bottom to see if it is same/suffixed
			compareSize = base.getConnectedNodes().get(i).getCurrentNode().size() - 1;
			patternSize = pattern.size() - 1; 
			for(int j = 0; j < base.getConnectedNodes().get(i).getCurrentNode().size(); j++) {
				//breaks out of inner for loop to go back to outer for loop since a letter in current compare does not match
				if(pattern.get(patternSize - j).equals(base.getConnectedNodes().get(i).getCurrentNode().get(compareSize - j)) == false) {
					break;
				}
				//if last iteration and loop has not broken yet
				if(j == compareSize) {
					//if same length and thus same pattern
					if(patternSize == compareSize) {
						recorded = true;
						match = base.getConnectedNodes().get(i);
					} else {
						match = matchNode(pattern, base.getConnectedNodes().get(i));
					}
				}
			}
		}
		return match;
	}
	
	boolean checkInfiniteLooping(ArrayList<E> pattern, int patternSize, int maxRepeat) {
		//recurring is the last pattern in the currently generated

		ArrayList<E> checkAgainst = new ArrayList<E>();
		boolean infinite = true;
		
		//start checking from the end
		int currentIndex = generated.size() - (patternSize + (patternSize - 1));
		for(int i = 0; i < maxRepeat && infinite == true; i++) {
			checkAgainst.clear();
			for(int j = 0; j < patternSize; j++) {
				checkAgainst.add(generated.get(currentIndex + j));
			}

			for(int j = 0; j < patternSize; j++) {
				if(checkAgainst.get(j).equals(pattern.get(j)) == false) {
					infinite = false;
				}
			}
			currentIndex = currentIndex - patternSize;
		}
		
		return infinite;
	}
	
	boolean checkRecord(ArrayList<E> pattern, TreeNode<E> base, E follow) {
		boolean recorded = false;
		int compareSize;
		int patternSize;
		//check each of the connected nodes of the base to see if pattern matches, or is contained
		for(int i = 0; i < base.getConnectedNodes().size(); i++) {
			//compare each element of the pattern from the bottom to see if it is same/suffixed
			compareSize = base.getConnectedNodes().get(i).getCurrentNode().size() - 1;
			patternSize = pattern.size() - 1; 
			for(int j = 0; j < base.getConnectedNodes().get(i).getCurrentNode().size(); j++) {
				//breaks out of inner for loop to go back to outter for loop since a letter in current compare does not match
				if(pattern.get(patternSize - j).equals(base.getConnectedNodes().get(i).getCurrentNode().get(compareSize - j)) == false) {
					break;
				}
				//if last iteration and loop has not broken yet
				if(j == compareSize) {
					//if same length and thus same pattern
					if(patternSize == compareSize) {
						recorded = true;
						base.getConnectedNodes().get(i).setTimes(base.getConnectedNodes().get(i).getTimes() + 1);
						base.getConnectedNodes().get(i).addFollowing(follow);
					} else {
						recorded = checkRecord(pattern, base.getConnectedNodes().get(i), follow);
					}
				}
			}
		}
		return recorded;
	}
	
	void addTreeNode(ArrayList<E> pattern, TreeNode<E> base, int len, E follow, ArrayList<E> singles) {	
		if(pattern.size() == len) {
			ArrayList<Double> empty = new ArrayList<Double>();
			for(int i = 0; i < singles.size(); i++) {
				empty.add(0.0);
			}
			TreeNode<E> temp = new TreeNode<E>();
			temp.setCurrentNode(pattern);
			temp.setTimes(1.0);
			if(singles.isEmpty() == false) {
				temp.setFollowing(singles);
				temp.setAppearance(empty);
			}
			temp.addFollowing(follow);
			base.addConnectedNode(temp);
			
		} else {
			int compareSize;
			int patternSize;
			for(int i = 0; i < base.getConnectedNodes().size(); i++) {
				//compare each element of the pattern from the bottom to see if it is same/suffixed
				compareSize = base.getConnectedNodes().get(i).getCurrentNode().size() - 1;
				patternSize = pattern.size() - 1; 
				for(int j = 0; j < base.getConnectedNodes().get(i).getCurrentNode().size(); j++) {
					if(pattern.get(patternSize - j).equals(base.getConnectedNodes().get(i).getCurrentNode().get(compareSize - j)) == false) {
						break;
					}
					//if last iteration and loop has not broken yet
					if(j == compareSize) {
						//recursive function adding to the length each time
						addTreeNode(pattern, base.getConnectedNodes().get(i), len + 1, follow, singles);
					}
				}
			}
		}
	}
	
	void printTree(TreeNode<E> base, String tab) {
		String add = "--";
		add += tab;
		for(int i = 0; i < base.getConnectedNodes().size(); i++) {
			System.out.println(tab + base.getConnectedNodes().get(i).getCurrentNode());
			//if it has connections call the recursive function
			if(base.getConnectedNodes().get(i).getConnectedNodes().isEmpty() == false) {
				printTree(base.getConnectedNodes().get(i), add);
			}
		}
	}
	
}

	