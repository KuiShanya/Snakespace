import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class SnakeSpace {
	public static void main(String args[]) throws InterruptedException {
		//Sets up the file to import highest score and best time
		Scanner in = null;
		File newFile = new File("Top Score.txt");
		if (!newFile.exists()){
			try {newFile.createNewFile();}
			catch (IOException e2) {e2.printStackTrace();}
			}
		try {in = new Scanner(newFile);}  //Imports high score
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		if (!in.hasNext()) {
			PrintWriter out = null;
			try {out = new PrintWriter(newFile);} 
			catch (FileNotFoundException e) {e.printStackTrace();}
			out.println("0"); out.println("0");
			try {in = new Scanner(newFile);}  //Imports high score
			catch (FileNotFoundException e1) {e1.printStackTrace();}
			out.close(); 
			}
		int highScore = in.nextInt();
		double bestTime = in.nextDouble();
		double time = 0;
		double startTime = 0;
		boolean pause = false;
		double elapsedTime = 0;
		in.close();
		
		//Program Loop
		while (true) {
			boolean skip = false;
			
			while (!skip) {
				
				//Creates start screen
				StdDraw.setXscale(0, 25);
				StdDraw.setYscale(0, 25);
				StdDraw.picture(12.5, 12.5, "SnakeSpace.png");
				StdDraw.show(0);
				
				//Waits at start screen (112 = F1, 113 = F2, 114 = F3, 115 = F4, 116 = F5)
				while (!StdDraw.isKeyPressed(112) && !StdDraw.isKeyPressed(113) && !StdDraw.isKeyPressed(114) && !StdDraw.isKeyPressed(115) && !StdDraw.isKeyPressed(116)) {}
				
				//Begins game
				if (StdDraw.isKeyPressed(112)) {skip = true;}
				
				//How To Play Screen
				else if (StdDraw.isKeyPressed(113)) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5,12.5,15);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(12.5, 24, "How to Play");
					StdDraw.text(12.5, 14, "The goal is to fill the entire screen with blocks");
					StdDraw.text(12.5, 13, "Use W,A,S,D to move around");
					StdDraw.text(12.5, 12, "Every time you move over a yellow block your chain grows");
					StdDraw.text(12.5, 11, "If you hit the edge of the screen or yourself you lose");
					StdDraw.text(12.5, 10, "When you get longer you need to plan your moves more");
					StdDraw.setPenColor(StdDraw.RED);
					StdDraw.text(12.5, 0, "Press ESC to return to Main Menu");
					StdDraw.show();
					while (!StdDraw.isKeyPressed(27)) {}
					}
				
				
				//High Score Screen
				else if (StdDraw.isKeyPressed(114)) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5,12.5,15);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(12.5,24,"High Score");
					if (highScore == 675) StdDraw.text(12.5, 14, "You've beaten the game, but your best time was " + bestTime);
					else StdDraw.text(12.5, 14, "Your best score is  " + highScore);
					StdDraw.setPenColor(StdDraw.RED);
					StdDraw.text(12.5, 0, "Press ESC to return to Main Menu");
					StdDraw.show();
					while (!StdDraw.isKeyPressed(27)) {}
					}
				
				//Options Screen
				else if (StdDraw.isKeyPressed(115)) {
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5,12.5,15);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(12.5, 24, "Options");
					StdDraw.text(12.5, 14, "1: Delete High Scores");
					StdDraw.text(12.5, 13, " ");
					StdDraw.show();
					while (!StdDraw.isKeyPressed(27)) {}
				}
				
				//Quit Screen
				else if (StdDraw.isKeyPressed(116)) {System.exit(0);}
				}
				
				boolean victory = false; //sets true if you win
				boolean end = false; //boolean to run the active game
				int cx = 12; int cy = 12; //head of the snake
				int vx = 1;	int vy = 0; //Velocity of the head of the snake
				int px = 0;	int py = 0; //dot postion
				ArrayList<Integer[]> snake = new ArrayList<Integer[]>(); //the snake
				Integer[] snakeBit = new Integer[4]; //each individual part of the snake
				Integer[] oldsnakeBit = new Integer[4]; //used to make each piece follow each ohter
				snakeBit = new Integer[] {cx,cy,0,0}; // creates head
				snake.add(snakeBit);
				int count = 0; //count for running the game
				boolean snakePoint = false; //to detect running into yourself
				int blockCount = 0; //adds multiple lengths to the snake per dot get
				startTime = System.currentTimeMillis(); //used for timer
				
				//Game Loop
				while (!end) {
					StdDraw.clear();	
				
					//Game Screen
					StdDraw.setPenColor(StdDraw.DARK_GRAY);
					StdDraw.filledSquare(12.5, 12.5, 15);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5, 12.5, 13);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(23,25.8, "Score:" + (snake.size() - 1));
					if (highScore == 675) StdDraw.text(2, 25.8, "Best Time:" + bestTime);
					else StdDraw.text(2, 25.8, "High Score:" + highScore);
					
					//Timer
					time = ( (double)Math.round(((System.currentTimeMillis() - startTime) /1000) *10) /10);
					StdDraw.text(12.5, 25.8, "Time: " + time);
				
					//Dot
					StdDraw.setPenColor(StdDraw.YELLOW);
					StdDraw.filledCircle(px, py, .4);
					
					//Victory Detection
					if (snake.size() == 675) {
						victory = true;
						end = true;
						}
			
					//Advances the snake forward only once for every 30 ticks in the game
					for (int i = 0; i < snake.size(); i++) {
						snakeBit = snake.get(i);
						if (count < 1) {
							snakeBit[2] = snakeBit[0];
							snakeBit[3] = snakeBit[1];
							}
						//For the head
						if (i == 0 ) {
							if (count < 1) {
								cx = cx + vx;
								cy = cy + vy;
								}
							snakeBit[0] = cx;
							snakeBit[1] = cy;
							}
						//For the body
						else {
							oldsnakeBit = snake.get(i-1);
							snakeBit[0] = oldsnakeBit[2];
							snakeBit[1] = oldsnakeBit[3];
							}
						StdDraw.setPenColor(StdDraw.RED);
						StdDraw.filledSquare(snakeBit[0], snakeBit[1], .5);
						}
					
					//Detects if you collect a dot
					if (px == cx && py == cy) {blockCount = blockCount + 9; px = 0; py = 0;}
					
					//Creates new blocks
					if (blockCount > 0) {
						oldsnakeBit = snake.get(snake.size() - 1);
						snakeBit = new Integer[] {oldsnakeBit[2],oldsnakeBit[3],-100,-100};
						snake.add(snakeBit);
						}
					
					//Creates new dots
					if (px == 0 && py == 0) {
						snakePoint = false;
						while (!snakePoint) {
							px = new Random().nextInt(25); py = new Random().nextInt(25);
							for (int i = 0; i < snake.size(); i++) {
								snakeBit = snake.get(i);
								if (snakeBit[0] == px && snakeBit[1] == py) break;
								if (i + 1 == snake.size()) snakePoint = true;
								}
							}
						}
				
					//Controls
					if (StdDraw.isKeyPressed(65)) {vx = -1; vy = 0;} //a
					if (StdDraw.isKeyPressed(68)) {vx = 1; vy = 0;} //d
					if (StdDraw.isKeyPressed(87)) {vy = 1; vx = 0;} //w
					if (StdDraw.isKeyPressed(83)) {vy = -1; vx = 0;} //s
					
					//Death Checks
					if (cx > 25 || cx < 0 || cy > 25 || cy < 0) end = true;
					for (int i = 0; i < snake.size(); i++) {
						//For hitting yourself
						snakeBit = snake.get(i);
						if (i == 0) {}
						else if(snakeBit[0] == cx && snakeBit[1] == cy) end = true;
						}
					//Count for movement
					count++;
					if (count == 30) count = 0;
					
					//Count for creating blocks
					if (blockCount > 0) blockCount--;
					
					//detects if high score is met
					if (snake.size() - 1 > highScore) highScore = snake.size() - 1;
					
					//Shows screen
					StdDraw.show(0);
					
					if (StdDraw.isKeyPressed(27) && !pause) {
						elapsedTime = System.currentTimeMillis();
						StdDraw.setPenColor(StdDraw.WHITE);
						StdDraw.text(12.5, 12.5, "PAUSED");
						StdDraw.show(0);
						Thread.sleep(100);
						while(!StdDraw.isKeyPressed(27)) {}
						Thread.sleep(100);
						pause = true;
						startTime = startTime + (System.currentTimeMillis() - elapsedTime);
						}
					else if (!StdDraw.isKeyPressed(27)) {pause = false;}
					}
				
				//Creates high score file
				try {
					File outFile = new File("Top Score.txt");
					if (outFile.exists()) {outFile.delete();}
					PrintWriter out = new PrintWriter(outFile);
					out.println(highScore);
					out.print(bestTime);
					outFile.setReadOnly();
					out.close();
					}
				catch (FileNotFoundException e) {e.printStackTrace();}
				
				//Victory Screen
				if (victory) {
					if (time < bestTime) bestTime = time;
					StdDraw.clear();
					StdDraw.setPenColor(StdDraw.DARK_GRAY);
					StdDraw.filledSquare(12.5, 12.5, 15);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5,12.5,13);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(12.5, 12.5, "Your final score was " + (snake.size() -1));
					StdDraw.text(12.5, 11, "Press Esc to try again");
					StdDraw.show();
					}
				
				//Defeat Screen
				else {
					StdDraw.clear();
					StdDraw.setPenColor(StdDraw.DARK_GRAY);
					StdDraw.filledSquare(12.5, 12.5, 15);
					StdDraw.setPenColor(StdDraw.BLACK);
					StdDraw.filledSquare(12.5,12.5,13);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(12.5, 12.5, "Your final score was " + (snake.size() -1));
					StdDraw.text(12.5, 11, "Press Esc to retry");
					StdDraw.show();
					}
				
				//27 = Escape
				while (!StdDraw.isKeyPressed(27)) {}
			}
		}
	}