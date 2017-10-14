import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Tokenizer {

	String fileName;
	String input;
	ArrayList<String> tokens = new ArrayList<String>();
	
	Tokenizer(){
		fileName = "C:\\Users\\alexa\\Desktop\\CRCP3330\\CRCP3330\\TwitterBot\\file.txt";
	}
	
	Tokenizer(String s){
		fileName = s;
	}
	
	void readFile() {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				if(input == null) {
					input = sCurrentLine;
				} else {
					input += sCurrentLine;
				}
			}
			//System.out.println(input);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		createTokens();
	}
	
	void createTokens() {
		int lastChar = 0;
		int counter = 0;
		for(int i = 0; i < input.length(); i++) {
			//System.out.println(i + " " + input.charAt(i));
			if(i + 1 < input.length()) {
				if(input.charAt(i + 1) == ' ' || input.charAt(i + 1) == '.' ||
				   input.charAt(i + 1) == ',' || input.charAt(i + 1) == '?' ||
				   input.charAt(i + 1) == '!' || input.charAt(i + 1) == '-') {
					
					tokens.add(input.substring(lastChar, i + 1));
					//System.out.println(tokens.get(counter));
					counter++;
					if(input.charAt(i + 1) != ' ') {
						tokens.add(input.substring(i + 1, i + 2));
						//System.out.println(tokens.get(counter));
						counter++;
						lastChar = i + 3;
						i = i + 3;
					} else {
						lastChar = i + 2;
						i = i + 2;
					}
				}
			}
		}
	}
	
	ArrayList<String> getTokens(){
		return tokens;
	}
}
