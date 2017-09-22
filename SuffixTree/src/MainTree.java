//Alexandra Ingrando
import java.util.ArrayList;
import java.util.Arrays;

public class MainTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Character> data = new ArrayList<Character>();
		ArrayList<Character> data1 = new ArrayList<Character>();
		data.addAll(Arrays.asList('a', 'b', 'r', 'a', 'c', 'a', 'd', 'a', 'b', 'r', 'a'));
		data1.addAll(Arrays.asList('a', 'b', 'c', 'c', 'c', 'd', 'a', 'a', 'd', 'c', 'd', 'a', 'a', 'b', 'c', 'a', 'd'));
		Tree<Character> abracadabra = new Tree<Character>(2, 1, 1);
		Tree<Character> pst = new Tree<Character>(3, 1, 1);
		abracadabra.train(data);
		pst.train(data1);
		
		System.out.println(data);
		abracadabra.printConnectedPatterns();
		System.out.println(data1);
		pst.printConnectedPatterns();
	}

}