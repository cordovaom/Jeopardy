import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import java.io.File;

public class Display{
	
	private List<JButton> buttons = new ArrayList<JButton>();
	private List<JLabel> labels = new ArrayList<JLabel>();
	private List<Question> dailyDouble = new ArrayList<Question>();
	private List<Question> questions;
	private JPanel mainPanel;
	private JPanel questionPanel;
	private JLabel questionLabel;
	private JLabel dailyDoubleLabel;
	private JFrame frame;
	private CardLayout frameLayout;
	private KeyListener kl;
	private int screenX = 1920;
	private int screenY = 1080;
	private float reselutionChange;
	private Question curent;
	private Boolean answer = true;
	
	/*
		input:
		output: constructor that creates the main JFrame for the application.
				Adds basic object to frame that will be needed.
	*/
	public Display(){
		frame = new JFrame();
		chooseScreen(0, frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenX, screenY);//600,600);
		frame.setUndecorated(true);
		
		
		frame.getContentPane().setBackground(Color.BLACK);
		frameLayout = new CardLayout();
		frame.getContentPane().setLayout(frameLayout);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(6, 6, 10, 10));
		mainPanel.setOpaque(false);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		questionPanel = new JPanel();
		questionPanel.setLayout(frameLayout);
		
		questionLabel = new JLabel("", JLabel.CENTER);
		questionLabel.setFont(new Font("Times New Roman", Font.PLAIN, (int)Math.floor(55*reselutionChange)));
		questionLabel.setForeground(Color.white);
		questionLabel.setOpaque(true);
		questionLabel.addMouseListener(new MouseAdapter() { 
								public void mousePressed(MouseEvent e) { 
									showAnswer();
								} 
							} );
		questionLabel.setBackground(new Color(1, 10, 120));
		questionPanel.add(questionLabel);
		
		dailyDoubleLabel = new JLabel(new ImageIcon("images/dailyDouble.png"));
		dailyDoubleLabel.addMouseListener(new MouseAdapter() { 
								public void mousePressed(MouseEvent e) { 
									frameLayout.next(questionPanel);
								} 
							} );
		dailyDoubleLabel.setOpaque(true);
		questionPanel.add(dailyDoubleLabel);
		
		frame.add(mainPanel);
		frame.add(questionPanel);
		questionPanel.setVisible(false);
		
		kl = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27)
					frame.dispose();
				else
					System.out.println(e.getKeyCode());
			}
		};
		
		questionLabel.addKeyListener(kl);
		questionPanel.addKeyListener(kl);
		
		frame.addKeyListener(kl);
		frame.setVisible(true); 
	}
	
	/*
		input: list of catagories form main file, jeopardy.java.
		output: creates and adds to frame list of labels to show the catagories.
	*/
	public void setMainLabels(String catagories[]){
		for (String i : catagories){
			labels.add(new JLabel(String.format("<html><div WIDTH=%d; style = 'text-align:center'>%s</div></html>", 312, i), JLabel.CENTER));
		}
		
		for (JLabel l : labels){
			l.setForeground(Color.white);
			l.setFont(new Font("Impact", Font.PLAIN, (int)Math.floor(55*reselutionChange)));
			l.setBackground(new Color(1, 10, 120));
			l.setOpaque(true);
			l.addKeyListener(kl);
			mainPanel.add(l);
		}
		
		frame.setVisible(false);
		frame.setVisible(true);
	}
	
	/*
		input: int value of total number of questions and list of string values, point values of each question.
		output: creates and adds to frame list of buttons to represent possible question choices.
	*/
	public void setButtons(int numButtons, String values[]){
		for (int i = 0; i < numButtons; i++){
			// i%values.length is used to repeat the value stored in values array in the correct oreder the correct amount of times.
			buttons.add(new JButton("$"+values[i % values.length]));
		}
		
		/*
			Buttons added to frame in specific order so point values are dissplayed correctly and order in list is maintained.
		*/
		for (int i = 0; i < values.length; i++){
			for (int j = i; j <= numButtons-values.length+i; j+=5){
				mainPanel.add(buttons.get(j));
				buttons.get(j).setForeground(new Color(215, 160, 75));
				buttons.get(j).setFont(new Font("Impact", Font.PLAIN, (int)Math.ceil(95*reselutionChange)));
				buttons.get(j).setBackground(new Color(1, 10, 120));
				buttons.get(j).addKeyListener(kl);
				buttons.get(j).addActionListener(new ActionListener() { 
								public void actionPerformed(ActionEvent e) { 
									questionChoice(e.getSource());
								} 
							} );
			}
		}
		
		frame.setVisible(false);
		frame.setVisible(true);
	}
	
	/*
		input: buttom object that was clicked to activeate the action listener.
		output: changes panel being dissplayed and changes questionlabel text to selected question.
				makes button inactive once you are returned to main screen.
	*/
	public void questionChoice(Object b){
		curent = questions.get(buttons.indexOf(b));
		for (Question q : dailyDouble)
			System.out.println(q);
		if (dailyDouble.contains(curent)){
			frameLayout.next(questionPanel);
			try{
				AudioInputStream ai = AudioSystem.getAudioInputStream(new File("Audio/dailyDouble.wav"));
				Clip c = AudioSystem.getClip();
				c.open(ai);
				c.start();
			} catch (Exception e) {
				System.out.println(e);
			}
			
		}
		questionLabel.setText(String.format("<html><div WIDTH=%d; style = 'text-align:center'>%s</div></html>", 312, curent.getQuestion()));
		answer = true;
		buttons.get(buttons.indexOf(b)).setEnabled(false);
		
		frameLayout.next(frame.getContentPane());
	}
	
	/*
		input:
		output: if answer is not being shown changes questionLabel text to the answer of current question.
				else changes panel back to main panel.
	*/
	public void showAnswer(){
		if(answer){
			questionLabel.setText(String.format("<html><div WIDTH=%d; style = 'text-align:center'>%s</div></html>", 312, curent.getAnswer()));
			answer = false;
		}
		else
			frameLayout.next(frame.getContentPane());
			frame.requestFocus();
	}
	
	/*
		input: list of questions from main file, jeopardy.java.
		output: creates local copy of inputed list.
	*/
	public void setQuestions(List<Question> q){
		this.questions = q;
	}
	
	/*
		input: int number of questions total on the board.
		output: picks a random question to be the dailyDouble.
	*/
	public void setDailyDouble(int numQuestions){
		Random rand = new Random();
		dailyDouble.add(questions.get(rand.nextInt(numQuestions)));
		System.out.println(dailyDouble.get(0));	
	}
	
	/*
		input: screen id int, main JFrame.
		output: move application to selected physical screen on your system.
	*/
	public void chooseScreen( int screen, JFrame frame ){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if( screen >= 0 && screen < gd.length )
		{	
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x, frame.getY());
			int disX = gd[screen].getDisplayMode().getWidth();
			int disY = gd[screen].getDisplayMode().getHeight();
			reselutionChange = (float)disX/screenX;
			screenX = disX;
			screenY = disY;
		}
		else if( gd.length > 0 )
		{
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x, frame.getY());
		}
		else
		{
			throw new RuntimeException( "No Screens Found" );
		}
	}
	
	/*
		Main function for testing only.
	*/
	public static void main(String args[]){
		new Display();
	}
}