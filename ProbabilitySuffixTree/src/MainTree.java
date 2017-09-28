//Alexandra Ingrando
//read data, create the tree's for each data, call the function to determine their patterns, and print patterns based off of different criteria
import java.util.ArrayList;
import java.util.Arrays;

public class MainTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		unitTestAbra();
		unitTestPST();
		
	}
	
	public static void unitTestAbra() {
		ArrayList<Character> data = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r', 'a'));

		
		//initialize the tree's for each data
		Tree<Character> abracadabra = new Tree<Character>(3, .1, 2);

		
		//receives and reads data to determine patterns
		abracadabra.train(data);

		
		//prints the patterns that have an empirical probability greater than pMin
		System.out.println(data);
		abracadabra.printEmpirical();

	}
	
	public static void unitTestPST() {
		ArrayList<Character> data1 = new ArrayList<Character>();
		data1.addAll(Arrays.asList('a', 'b', 'c', 'c', 'c', 'd', 'a', 'a', 'd', 'c', 'd', 'a', 'a', 'b', 'c', 'a', 'd'));
		
		//initialize the tree's for each data
		Tree<Character> pst = new Tree<Character>(3, .1, 2);
		
		//receives and reads data to determine patterns
		pst.train(data1);
		
		//prints the patterns that have an empirical probability greater than pMin

		System.out.println(data1);
		pst.printEmpirical();
	}
	

}