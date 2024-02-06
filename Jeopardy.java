import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Jeopardy{
	
	public static String fileName = "Questions.txt"; // make default questions file and put name here.
	public static List<Question> questions = new ArrayList<Question>();
	public static String[] catagories;
	public static String[] pointValues;
	
	/*
		input:
		output: sets file location for questions file to user input. 
				If no input given defaults to templet.
	*/
	public static void setFileLoc(){
		Scanner input = new Scanner(System.in);
		
		System.out.print("Questions File location: ");
		String userInput = input.nextLine();
		if (!userInput.isEmpty())
			fileName = userInput;
		
		System.out.println();
		
		input.close();
	}
	
	/*
		input:
		output: reads data from questions file. If bad file path given throws IO Exception.
				Save data from file into catagories, pointValues, and questions array.
	*/
	public static void readFile() throws IOException{
		try {
			
			Scanner fileReader = new Scanner(new File(fileName));
			
			catagories = fileReader.nextLine().toUpperCase().split(", ");
			pointValues = fileReader.nextLine().toUpperCase().split(", ");
			fileReader.nextLine();
		
			for (String i : catagories){
				for (String j : pointValues){

					questions.add(new Question(fileReader.nextLine().toUpperCase(), fileReader.nextLine().toUpperCase(), i, Integer.parseInt(j)));
					
					if (fileReader.hasNextLine())
						fileReader.nextLine();
					else{
						if (questions.size() != catagories.length * pointValues.length){
							throw new IOException("Not enough questons provided."); 
						}
					}
				}
			}
			
			fileReader.close();
			
		} catch (FileNotFoundException e){
			System.out.println(String.format("File at %s not found", fileName));
		}
	}
	
	/*
		input:
		output: print list of all questions in questions array.
				For testing perpeses only.
	*/
	public static void print(){
		for (Question q : questions)
			System.out.println(q);

	}
	
	/*
		input:
		output: main function of application.
	*/
	public static void main(String[] args){
		setFileLoc();
		try{
			readFile();
		} catch (IOException e){
			System.out.println(e);
			System.exit(1);
		}
		
		Display display = new Display();
		display.setMainLabels(catagories);
		display.setButtons(catagories.length*pointValues.length, pointValues);
		display.setQuestions(questions);
		display.setDailyDouble(catagories.length*pointValues.length);

		//print();
	}
	
}