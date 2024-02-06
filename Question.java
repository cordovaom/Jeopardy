/*
	Question class to hold all data associated with the questions. ie catagory, point value, question, answer.
*/
public class Question{

		private String question;
		private String answer;
		private String catagory;
		private int points;
		
		/*
			input: Sting question, string answer, string catagory
					int points.
			output: new question object.
		*/
		public Question(String question, String answer, String catagory, int points){
			this.question = question;
			this.answer = answer;
			this.catagory = catagory;
			this.points = points;
		}
		
		/*
			input:
			output: string value stored in question.
		*/
		public String getQuestion(){
			return question;
		}
		
		/*
			input:
			output: string value stored in answer.
		*/
		public String getAnswer(){
			return answer;
		}
		
		/*
			input:
			output: string value stored in catagory.
		*/
		public String getCatagory(){
			return catagory;
		}
		
		/*
			input:
			output: int value stored in points.
		*/
		public int getPoints(){
			return points;
		}
		
		/*
			input:
			output: string representation of question object.
		*/
		@Override
		public String toString(){
			return String.format("Catagory: %s \nPoint Value: %s \nQuestion: %s \nAnswer: %s", catagory, points, question, answer);
		}
}