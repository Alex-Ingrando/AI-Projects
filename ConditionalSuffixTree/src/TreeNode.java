//Alexandra Ingrando
//This class holds the info for each pattern
import java.util.*;
import java.util.Random;


public class TreeNode<E> {
	//variables
	//arrayList to hold all elements of the current pattern
	ArrayList<E> currentNode = new ArrayList<E>();
	//arrayList to hold all the elements that came after this one and how many times
	ArrayList<E> followingElements = new ArrayList<E>();
	ArrayList<Double> followingAppearance = new ArrayList<Double>();
	//array list of branches from this node
	ArrayList<TreeNode<E>> connectedNodes = new ArrayList<TreeNode<E>>(); 
	double numTimes;
	//holds the conditional for each element that follows it
	ArrayList<Double> conditionalCompare = new ArrayList<Double>();
	ArrayList<Double> followingProbability = new ArrayList<Double>();
	double empirical;
	
	
	TreeNode(){
		numTimes = 0;
		empirical = 0;

	}
	
	E getSeed() {
		E seed = null;
		Random rand = new Random();
		int randIndex = rand.nextInt(connectedNodes.size());
		seed = connectedNodes.get(randIndex).getCurrentNode().get(0);
		return seed;
		
	}
	E getNext() {
		E next = null;
		Random rand = new Random();
		double randNum = 0.0;
		boolean probBetween = false;
		
		for(int i = 0; i < followingProbability.size(); i++) {
			randNum += (followingProbability.get(i) * 100.0);
		}
		int  n = rand.nextInt((int)randNum);
		double prob = (int) n / 100.0;	
		 
		System.out.println("Generating from " + currentNode + " with probabilities " + followingProbability +  " with random gen of " + prob);
		
		double min = 0.0;
		double max = 0.0;
		for(int i = 0; i < followingProbability.size() && probBetween == false; i++) {
			max = max + followingProbability.get(i);
			if(prob >= min && prob <= max) {
				next = followingElements.get(i);
				probBetween = true;
			}
		}

		return next;
	}
	
	void smooth(double g, int t) {
		double temp;
		double n;
		double times = (double) t;
		double fractionTimes = 1.0 / times;
		
		if(g < 0 || g > (fractionTimes)) {
			g = 1.0 / (times + (times / 4.0));
			System.out.println("g is out of range");
		}
		for(int i = 0; i < connectedNodes.size(); i++) {
			for(int j = 0; j < connectedNodes.get(i).conditionalCompare.size(); j++) {
				n = times;
				temp = ((1- (n * g)) * connectedNodes.get(i).conditionalCompare.get(j)) + g;
				connectedNodes.get(i).addFollowingProbability(temp);
			}
			if(connectedNodes.get(i).getConnectedNodes().isEmpty() == false) {
				connectedNodes.get(i).smooth(g, t);
			}
		}
	}
	
	void addFollowingProbability (double num) {
		followingProbability.add(num);
	}
	
	ArrayList<Double> getFollowingProbability(){
		return followingProbability;
	}
	
	void setCurrentNode(ArrayList<E> node) {
		//fill currentNode with a pattern pre-determined
		for(int i = 0; i < node.size(); i ++) {
			currentNode.add(node.get(i));
		}
	}
	
	ArrayList<E> getCurrentNode(){
		return currentNode;
	}
	
	void addConnectedNode(TreeNode<E> nextNode) {
		connectedNodes.add(nextNode);
	}
	
	ArrayList<TreeNode<E>> getConnectedNodes(){
		return connectedNodes;
	}
	
	void deleteConnection(int i) {
		connectedNodes.remove(i);
	}
	
	void setTimes(double t) {
		numTimes = t;
	}
	
	double getTimes() {
		return numTimes;
	}
	
	void addFollowing(E element) {
		if(element != null) {
			if(followingElements.isEmpty() == true ) {
				followingElements.add(element);
				followingAppearance.add(1.0);
				//System.out.println(element + " added to the following for " + currentNode);
			} else {
				boolean found = false;
				for(int i = 0; i < followingElements.size(); i++) {
					if (followingElements.get(i) == element) {
						found = true;
						followingAppearance.set(i, followingAppearance.get(i) + 1.0);
						//System.out.println(element + " appearance added for " + currentNode);
						break;
					}
				}
				if(found == false) {
					followingElements.add(element);
					followingAppearance.add(1.0);
					//System.out.println(element + " added to the following for " + currentNode);
				}
			}
		}
	}
	void resetFollowing (ArrayList<E> singles) {
		ArrayList<Double> temp = new ArrayList<Double>();
		for(int i = 0; i < singles.size(); i++) {
			temp.add(0.0);
		}
		if(followingElements.isEmpty() == false) {
			
			for(int i = 0; i < singles.size(); i++) {
				for(int j = 0; j < followingElements.size(); j++) {
					if(singles.get(i).equals(followingElements.get(j))) {
						temp.set(i, followingAppearance.get(j));
					}
				}
			}
			setFollowing(singles);
			setAppearance(temp);
		}
	}
	void setEmpirical(double e) {
		empirical = e;
	}
	
	double getEmpirical() {
		return empirical;
	}
	
	void setFollowing(ArrayList<E> follow){
		followingElements = follow;
	}
	
	ArrayList<E> getFollowing() {
		return followingElements;
	}
	
	void setAppearance(ArrayList<Double> appear) {
		followingAppearance = appear;
	}
	
	ArrayList<Double> getFollowingAppearance() {
		return followingAppearance;
	}
	
	void testEmpirical(double pMin, double numElements) {
		boolean passed;
		ArrayList<Integer> deleteEmpirical = new ArrayList<Integer>();
		for(int i = 0; i < connectedNodes.size(); i++) {
			passed = false;
			
			//determine empirical and test if it passes
			connectedNodes.get(i).setEmpirical(connectedNodes.get(i).getTimes() / numElements);
			
			if(connectedNodes.get(i).getEmpirical() >= pMin) {
				passed = true;
			} 
			
			//if passed, test its connections, else delete the node
			if(passed == false) {
				deleteConnection(i);
				i--;
			} else {
				connectedNodes.get(i).testEmpirical(pMin, numElements - 1);
			}
		}
	}
	
	void determineConditionalCompare() {
		double conditional = 0;
		if(conditionalCompare.isEmpty() == false) {
			conditionalCompare.clear();
		}
		for(int i = 0; i < followingAppearance.size(); i++) {
			
			conditional = followingAppearance.get(i) / numTimes;
			conditionalCompare.add(conditional);
		}
		if(connectedNodes.isEmpty() == false) {
			for (int i = 0; i < connectedNodes.size(); i++) {
				connectedNodes.get(i).determineConditionalCompare();
			}
		}
	}
	
	ArrayList<Double> getConditionalCompare() {
		return conditionalCompare;
	}
	
	void testConditional(double r) {
		for(int i = 0; i < connectedNodes.size(); i++) {
			connectedNodes.get(i).determineConditionalCompare();
			connectedNodes.get(i).compareConditional(connectedNodes.get(i).getConditionalCompare(), r);
		}
	}
	
	void compareConditional(ArrayList<Double> baseCompare, double r) {
		double cond;
		boolean passed;
		for(int i = 0; i < connectedNodes.size(); i++) {
			passed = false;
			for(int j = 0; j < connectedNodes.get(i).getConditionalCompare().size() && passed == false; j++) {
				if(connectedNodes.get(i).getConditionalCompare().get(j) == 0 && baseCompare.get(j) == 0) {
					cond = 0.0;
				} else {
					cond = connectedNodes.get(i).getConditionalCompare().get(j) / baseCompare.get(j);
				}
				if(cond >= r) {
					passed = true;
				}
			}
			if(passed == false) {
				deleteConnection(i);
				i--;
			} else {
				connectedNodes.get(i).compareConditional(connectedNodes.get(i).getConditionalCompare(), r);
			}
		}
	}
	
}