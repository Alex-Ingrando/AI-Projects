//Alexandra Ingrando
//read data, create the tree's for each data, call the function to determine their patterns, and print patterns based off of different criteria
import java.util.ArrayList;
import java.util.Arrays;

public class MainTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double gSmooth = 1.0 / 12.0;
		unitTestAbra(3, .1, 2, gSmooth);
		 gSmooth = 1.0 / 18.0;
		unitTestPTS(3, .1, 2.0, gSmooth);
		
	}
	
	public static void unitTestAbra(int l, double pMin, double r, double g) {
		
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r', 'a'));
		
		//initialize the tree's for each data
		Tree<Character> funTree = new Tree<Character>(l, pMin, r, data, g);

		
		//receives and reads data to determine patterns
		funTree.train();
		
		funTree.generate();
	}
	
public static void unitTestPTS(int l, double pMin, double r, double g) {
		
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'c', 'c', 'c', 'd', 'a', 'a', 'd', 'c', 'd', 'a', 'a', 'b', 'c', 'a', 'd'));
		
		//initialize the tree's for each data
		Tree<Character> funTree = new Tree<Character>(l, pMin, r, data, g);

		
		//receives and reads data to determine patterns
		funTree.train();
		
		funTree.generate();

	}

	

}