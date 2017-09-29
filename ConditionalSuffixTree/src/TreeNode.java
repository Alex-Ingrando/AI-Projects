//Alexandra Ingrando
//This class holds the info for each pattern
import java.util.ArrayList;

public class TreeNode<E> {
	//variables
	//arrayList to hold all elements of the current pattern
	ArrayList<E> currentNode = new ArrayList<E>();
	//arrayList to hold all the elements that came after this one and how many times
	ArrayList<E> followingElements = new ArrayList<E>();
	ArrayList<Double> followingAppearance = new ArrayList<Double>();
	//array list of branches from this node
	ArrayList<TreeNode<E>> connectedNodes = new ArrayList<TreeNode<E>>(); 
	//Does it have a preceding node?
	boolean preceding;
	boolean empiricalPassed;
	double numTimes;
	double possibleTimes;
	//holds the conditional for each element that follows it
	ArrayList<Double> conditionalCompare = new ArrayList<Double>();
	double empirical;
	double conditional;
	//holds the index of the followingElements with the highest conditional
	double conditionalIndex;
	
	
	TreeNode(){
		preceding = false;
		empiricalPassed = false;
		numTimes = 0;
		empirical = 0;
		conditional = 0;

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
	
	E getPatternParts(int index){
		return currentNode.get(index);
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
	
	void setPreceding(boolean t) {
		preceding = t;
	}
	
	boolean getPreceding() {
		return preceding;
	}
	
	void setTimes(double t) {
		numTimes = t;
	}
	
	double getTimes() {
		return numTimes;
	}
	
	void setPossibleTimes(double t) {
		possibleTimes = t;
	}
	
	double getPossibleTimes() {
		return possibleTimes;
	}
	
	void setEmpirical(double e) {
		empirical = e;
	}
	
	double getEmpirical() {
		return empirical;
	}
	
	void determineConditionalCompare() {
		conditional = 0;
		if(conditionalCompare.isEmpty() == false) {
			conditionalCompare.clear();
		}
		for(int i = 0; i < followingElements.size(); i++) {
			
			conditional = followingAppearance.get(i) / numTimes;
			conditionalCompare.add(conditional);
		}
	}
	
	ArrayList<Double> getConditionalCompare() {
		return conditionalCompare;
	}
	double getConditionalCompare(int index) {
		return conditionalCompare.get(index);
	}
	
	void setConditional(double c, int index) {
		conditional = c;
		conditionalIndex = index;
	}
	
	double getConditional() {
		return conditional;
	}
	
	void addFollowing(E element) {
		if(followingElements.isEmpty() == true ) {
			followingElements.add(element);
			followingAppearance.add(1.0);
		} else {
			boolean found = false;
			for(int i = 0; i < followingElements.size(); i++) {
				if (followingElements.get(i) == element) {
					found = true;
					followingAppearance.set(i, followingAppearance.get(i) + 1.0);
					break;
				}
			}
			if(found == false) {
				followingElements.add(element);
				followingAppearance.add(1.0);
			}
		}
		
		
	}
	
	ArrayList<E> getFollowing() {
		return followingElements;
	}
	
	ArrayList<Double> getFollowingAppearance() {
		return followingAppearance;
	}
	
	E getSpecificFollowing(int index) {
		return followingElements.get(index);
	}
	
	double getSpecificFollowingAppearance(int index) {
		return followingAppearance.get(index);
	}
	
	void setEmpiricalPassed(boolean pass) {
		empiricalPassed = pass;
	}
	
	boolean getEmipiricalPassed() {
		return empiricalPassed;
	}
}