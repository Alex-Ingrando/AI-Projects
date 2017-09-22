//Alexandra Ingrando
//This class holds the info for each pattern
import java.util.ArrayList;

public class TreeNode<E> {
	//variables
	//arrayList to hold all elements of the current pattern
	ArrayList<E> currentNode = new ArrayList<E>();
	//array list of branches from this node
	ArrayList<TreeNode<E>> connectedNodes = new ArrayList<TreeNode<E>>(); 
	//Does it have a preceding node?
	boolean preceding;
	
	TreeNode(){
		preceding = false;
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

	
}
