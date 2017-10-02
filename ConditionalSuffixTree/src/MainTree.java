//Alexandra Ingrando
//read data, create the tree's for each data, call the function to determine their patterns, and print patterns based off of different criteria
import java.util.ArrayList;
import java.util.Arrays;

public class MainTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		unitTestAbra(3, .1, 2);
		unitTestPTS(3, .1, 2);
		
	}
	
	public static void unitTestAbra(int l, double pMin, double r) {
		
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r', 'a'));
		
		//initialize the tree's for each data
		Tree<Character> funTree = new Tree<Character>(l, pMin, r, data);

		
		//receives and reads data to determine patterns
		funTree.train();
		
		//print every pattern found
		//funTree.printAllPatterns();
		
		//print each branch and connections
		//funTree.printFirstBranch();
		
		//prints the patterns that have an empirical probability greater than pMin
		//funTree.printEmpirical();
		
		//prints the patterns that passed both empirical and conditional test
		funTree.printConditional();

	}
	
public static void unitTestPTS(int l, double pMin, double r) {
		
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'c', 'c', 'c', 'd', 'a', 'a', 'd', 'c', 'd', 'a', 'a', 'b', 'c', 'a', 'd'));
		
		//initialize the tree's for each data
		Tree<Character> funTree = new Tree<Character>(l, pMin, r, data);

		
		//receives and reads data to determine patterns
		funTree.train();
		
		//print every pattern found
		//funTree.printAllPatterns();
		
		//print each branch and connections
		//funTree.printFirstBranch();
		
		//prints the patterns that have an empirical probability greater than pMin
		//funTree.printEmpirical();
		
		//prints the patterns that passed both empirical and conditional test
		funTree.printConditional();

	}

	

}