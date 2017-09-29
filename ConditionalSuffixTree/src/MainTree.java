//Alexandra Ingrando
//read data, create the tree's for each data, call the function to determine their patterns, and print patterns based off of different criteria
import java.util.ArrayList;
import java.util.Arrays;

public class MainTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		unitTestAbra(3, .1, 2);
		unitTestPST(3, .1, 2);
		
	}
	
	public static void unitTestAbra(int l, double pMin, double r) {
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r', 'a'));

		
		//initialize the tree's for each data
		Tree<Character> abracadabra = new Tree<Character>(l, pMin, r);

		
		//receives and reads data to determine patterns
		abracadabra.train(data);

		
		
		System.out.println(data);
		
		//prints all patterns in their connected branches
		abracadabra.printFirstBranch();
		
		//prints the patterns that have an empirical probability greater than pMin
		//abracadabra.printEmpirical();

	}
	
	public static void unitTestPST(int l, double pMin, double r) {
		ArrayList<Character> data1 = new ArrayList<Character>();
		data1.addAll(Arrays.asList('a', 'b', 'c', 'c', 'c', 'd', 'a', 'a', 'd', 'c', 'd', 'a', 'a', 'b', 'c', 'a', 'd'));
		
		//initialize the tree's for each data
		Tree<Character> pst = new Tree<Character>(l, pMin, r);
		
		//receives and reads data to determine patterns
		pst.train(data1);
	
		System.out.println(data1);
		
		//prints all patterns in their connected branches
		pst.printFirstBranch();
		
		//prints the patterns that have an empirical probability greater than pMin
		//pst.printEmpirical();
	}
	

}