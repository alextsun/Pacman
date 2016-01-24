//3 levels, every level, change monsters
//on first level, 8 monsters, 500ms, random movement
//on second level, 11 monsters, 450ms, track movement
//on third level, 1 monsters, 200ms, track movement, shoot lasers

//each level lasts 20 seconds, and then you clear the level
//no cheese necessary, last level, monster will shoot lasers as you

//includes a timer at the top
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Pacman6
{
	private JFrame frame;
	private drawingPanel panel;

	public static void main (String[] args)
	{
		Pacman6 pacman = new Pacman6();
		pacman.run();
	}

	public void run()
	{
		frame = new JFrame("Pacman Jr");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create JPanel and add to frame

		panel = new drawingPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);    // add panelto frame

		frame.setSize(672, 752);        // 660*740
		frame.setVisible(true);        // set to false to make invisible
	}

	class drawingPanel extends JPanel implements KeyListener
	{
		private int level1Monsters = 14, level2Monsters = 5, level3Monsters = 1;
		private boolean mouthOpen;
		private int[] monsterX;
		private int[] monsterY;
		private int pacmanX, pacmanY;
		private char lastCharTyped = 'd'; //determines which direction mouth opens
		//whether a place on the grid has cheese or not
		private boolean gameOver = false;
		private boolean laserOn; //not used yet
		private int seconds, minutes;
		private boolean level1 = true, level2 = false, level3 = false; 
		private int numMonst = 0;
		private int numLives = 3;

		private MouthMover mouthMover = new  MouthMover();
		private Monster1Mover monster1Mover = new Monster1Mover();
		private Monster2Mover monster2Mover = new Monster2Mover();
		private Monster3Mover monster3Mover = new Monster3Mover();
		private LaserShooter laserShooter = new LaserShooter();
		private TimeMover timeMover = new TimeMover();
		private Timer mouthTimer = new Timer(200, mouthMover);
		private Timer monster1Timer = new Timer(1000,monster1Mover);
		private Timer monster2Timer = new Timer(600,monster2Mover);
		private Timer monster3Timer = new Timer(200,monster3Mover);
		private Timer laserTimer = new Timer(200, laserShooter);
		private Timer timeTimer = new Timer(1000, timeMover);

		public drawingPanel()
		{
			init();
			addKeyListener(this);
		}

		public void init()
		{
			if(level3)
			{
				monsterX = new int[level3Monsters];
				monsterY = new int[level3Monsters];
				numMonst = level3Monsters;
				seconds = 10;
				laserOn = true;
			}
			else if(level2)
			{
				monsterX = new int[level2Monsters];
				monsterY = new int[level2Monsters];
				numMonst = level2Monsters;
				seconds = 10;
			}
			else if(level1)
			{
				monsterX = new int[level1Monsters];
				monsterY = new int[level1Monsters];
				numMonst = level1Monsters;
				seconds = 10;
			}
			stopTimers();
			initializePacman();
			initializeMonsters();
			startTimers();
			minutes = 0;
			laserOn = false;
		}

		public void startTimers()
		{
			mouthTimer.start();
			timeTimer.start();
			if(level3)
			{
				monster3Timer.start();
				laserTimer.start();
			}
			else if(level2)
			{
				monster2Timer.start();
			}
			else if(level1)
			{
				monster1Timer.start();
			}
		}

		public void stopTimers()
		{
			mouthTimer.stop();
			timeTimer.stop();
			if(level3)
			{
				monster3Timer.stop();
				laserTimer.stop();
			}
			else if(level2)
			{
				monster2Timer.stop();
			}
			else if(level1)
			{
				monster1Timer.stop();
			}
		}

		public boolean gameOver()
		{
			if(winGame()&&numLives>0)
			{
				stopTimers();
				return true;
			}
			if(numLives==0)
			{
				stopTimers();
				return true;
			}
				
			return false;
		}

		public boolean winGame()
		{
			if(numLives>0 && level3==true && seconds==0)
				return true;
			return false;

		}

		public void loseLife()
		{
			for(int i = 0; i < numMonst; i++)
			{
				if((monsterX[i]==pacmanX)&&(monsterY[i]==pacmanY)||((monsterX[i]==pacmanX)
						||(monsterY[i]==pacmanY)&&(level3)&&laserOn))
				{
					numLives--;
					init();
					//return true;
				}
			}
			//return false;
		}

		private class MouthMover implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				mouthOpen = !mouthOpen;
				repaint();
			}
		}

		private class TimeMover implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				seconds--;
				if(seconds<0)
				{
					seconds = 0;
				}
				repaint();
			}
		}

		private class Monster1Mover implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				newDirection();
				for(int i = 0; i < numMonst; i++)
				{
					if(pacmanX==monsterX[i]&&pacmanY==monsterY[i])
						loseLife();
					
				}
				repaint();
			}
		}

		private class Monster2Mover implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				newDirection();
				for(int i = 0; i < numMonst; i++)
				{
					if(pacmanX==monsterX[i]&&pacmanY==monsterY[i])
						loseLife();
				}
				repaint();
			}
		}

		private class Monster3Mover implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				newDirection();
				for(int i = 0; i < numMonst; i++)
				{
					if(pacmanX==monsterX[i]&&pacmanY==monsterY[i])
						loseLife();
				}
				repaint();
			}
		}

		private class LaserShooter implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				laserOn = !laserOn;
				if((pacmanX==monsterX[0]||pacmanY==monsterY[0])&&(laserOn)&&level3)
					loseLife();
				repaint();
			}
		}

		public void newDirection() //tells which direction for monster to move
		{
			if(level1)
			{
				for(int i = 0; i < numMonst; i++)
				{
					int random = (int)(4*Math.random());
					if(random==0)
					{
						moveLeft(true,i);
					}
					else if(random==1)
					{
						moveLeft(false,i);
					}
					else if(random==2)
					{
						moveUp(true,i);
					}
					else if(random==3)
					{
						moveUp(false,i);
					}
				}
			}
			else
			{
				for(int i = 0; i < numMonst; i++)
				{
					int xDist = monsterX[i]-pacmanX;
					int yDist = monsterY[i]-pacmanY;
					int absXDist = Math.abs(xDist);
					int absYDist = Math.abs(yDist);
					if(absXDist>=absYDist)
					{
						if(xDist>0)
							moveLeft(true,i);
						else
							moveLeft(false,i);
					}
					else
					{
						if(yDist>0)
							moveUp(true,i);
						else
							moveUp(false,i);
					}
				}
			}
		}

		public void moveLeft(boolean left, int whichMonsterToMove)
		{
			if(left) //move left
			{
				monsterX[whichMonsterToMove]-=1; //shift left one space
			}
			else if(!left) //move right
			{
				monsterX[whichMonsterToMove]+=1; //shift right one space
			}
			monsterX[whichMonsterToMove]+=10; //in case it is negative
			monsterY[whichMonsterToMove]+=10; //in case it is negative
			monsterX[whichMonsterToMove]%=10; //wrap around
			monsterY[whichMonsterToMove]%=10; //wrap around
			boolean repeat = false;
			for(int i = 0; i < numMonst; i++)
			{
				if(whichMonsterToMove!=i)
				{
					if(monsterX[whichMonsterToMove]==monsterX[i])
					{
						repeat = true;
					}
				}
			}
			if(repeat)
			{
				if(left)
				{
					monsterX[whichMonsterToMove]+=2;
				}
				else
				{
					monsterX[whichMonsterToMove]-=2;
				}
			}
			monsterX[whichMonsterToMove]+=10; //in case it is negative
			monsterY[whichMonsterToMove]+=10; //in case it is negative
			monsterX[whichMonsterToMove]%=10; //wrap around
			monsterY[whichMonsterToMove]%=10; //wrap around
		}

		public void moveUp(boolean up, int whichMonsterToMove)
		{
			if(up) //move up
			{
				monsterY[whichMonsterToMove]-=1; //shift up one space
			}
			else if(!up) //move down
			{
				monsterY[whichMonsterToMove]+=1; //shift down one space
			}
			monsterX[whichMonsterToMove]+=10; //in case it is negative
			monsterY[whichMonsterToMove]+=10; //in case it is negative
			monsterX[whichMonsterToMove]%=10; //wrap around
			monsterY[whichMonsterToMove]%=10; //wrap around
			boolean repeat = false;
			for(int i = 0; i < numMonst; i++)
			{
				if(whichMonsterToMove!=i)
				{
					if(monsterX[whichMonsterToMove]==monsterX[i])
					{
						repeat = true;
					}
				}
			}
			if(repeat)
			{
				if(up)
				{
					monsterX[whichMonsterToMove]+=2;
				}
				else
				{
					monsterX[whichMonsterToMove]-=2;
				}
			}
			monsterX[whichMonsterToMove]+=10; //in case it is negative
			monsterY[whichMonsterToMove]+=10; //in case it is negative
			monsterX[whichMonsterToMove]%=10; //wrap around
			monsterY[whichMonsterToMove]%=10; //wrap around
		}


		public void initializeMonsters()
		{
			boolean[][] hasMonster = new boolean[10][10];

			for(int i = 0; i < numMonst; i++)
			{
				boolean done = false;
				while(!done)
				{
					int row = getRandomPosition();
					int col = getRandomPosition();
					if(!hasMonster[row][col]&&row!=pacmanX&&col!=pacmanY)
					{
						monsterX[i] = row;
						monsterY[i] = col;
						hasMonster[row][col] = true;
						done = true;
					}
				}
			}
		}



		public void initializePacman()
		{
			mouthOpen = false;
			boolean done = false;
			while(!done)
			{
				int row = getRandomPosition();
				int col = getRandomPosition();
				pacmanX = row;
				pacmanY = col;
				done = true;

			}
		}

		public int getRandomPosition()
		{
			int random = (int)(10*Math.random());
			return random;
		}

		public void paintComponent(Graphics g)
		{

			super.paintComponent(g);
			setBackground(Color.WHITE); //set background color to blue


			g.setColor(Color.blue);
			g.fillRect(0,0,660,670);

			g.setColor(Color.white);
			g.fillRect(30,30,600,600);

			g.setColor(Color.red);
			g.fillRect(0,670,660,50);


			g.setColor(Color.gray);
			for(int row = 30; row <= 630; row+=60) //row lines
			{
				g.fillRect(row,30,2,600);
			}

			for(int col = 30; col <= 630; col+=60)
			{
				g.fillRect(30,col,600,2);
			}

			//draw monsters
			g.setColor(Color.red);
			for(int i = 0; i < numMonst; i++)
			{
				int actualXCoord = 60*monsterX[i];
				int actualYCoord = 60*monsterY[i];
				if(!level3)
					g.setColor(Color.cyan);
				else
					g.setColor(Color.black);
				g.fillOval(36+actualXCoord, 36+actualYCoord,50,50); //face
				if(!level3)	
					g.setColor(Color.black);
				else
					g.setColor(Color.white);
				g.fillOval(47+actualXCoord, 50+actualYCoord,8,8); //eye #1
				g.fillOval(68+actualXCoord, 50+actualYCoord,8,8); //eye #2
				if(level1||level2)
				{
					g.fillRect(46+actualXCoord, 70+actualYCoord,32,3);//mouth expression
				}
				else if (level3)
				{
					g.drawLine(46+actualXCoord, 45+actualYCoord, 56+actualXCoord, 47+actualYCoord);
					g.drawLine(67+actualXCoord, 48+actualYCoord, 77+actualXCoord, 44+actualYCoord);
					g.drawArc(46+actualXCoord, 65+actualYCoord,32,10,180,180);
				}
			}


			//timer
			g.setColor(Color.white);
			String minute = "";
			String second = "";
			g.setFont(new Font("Times", Font.BOLD, 30));
			if(minutes<10)
				minute = "0"+minutes;
			else
				minute+=minutes;
			if(seconds<10)
				second = "0"+seconds;
			else
				second+=seconds;
			g.drawString("TIME "+minute+" : "+second, 30, 25);

			//lives
			g.drawString("LIVES : "+numLives, 504,25);

			//draw pacman
			g.setColor(Color.orange);
			if(mouthOpen)
			{
				if(lastCharTyped =='d')
				{
					g.fillArc(36+60*pacmanX, 36+60*pacmanY,50,50,45,270);
				}
				else if(lastCharTyped=='s')
				{
					g.fillArc(36+60*pacmanX, 36+60*pacmanY,50,50,315,270);
				}
				else if(lastCharTyped=='a')
				{
					g.fillArc(36+60*pacmanX, 36+60*pacmanY,50,50,225,270);
				}
				else if(lastCharTyped=='w')
				{
					g.fillArc(36+60*pacmanX, 36+60*pacmanY,50,50,135,270);
				}
			}
			else
			{
				g.fillOval(36+60*pacmanX, 36+60*pacmanY,50,50);
			}

			//laser
			if(laserOn && level3)
			{
				g.setColor(Color.red);
				g.fillRect(0,36+60*monsterY[0]+25,34+60*monsterX[0]+28,4);
				g.fillRect(36+60*monsterX[0]+24,36+60*monsterY[0]+25,4,500);
				g.fillRect(36+60*monsterX[0]+28, 36+60*monsterY[0]+25,600,4);
				g.fillRect(36+60*monsterX[0]+24,0,4,36+60*monsterY[0]+25 );
			}

			//win or lose message
			if(winGame())
			{
				g.setColor(Color.green);
				g.setFont(new Font("Seraph", Font.BOLD, 80));    // font name, style, size
				g.drawString("YOU WIN!",137,390);
			}
			else if(gameOver())
			{
				g.setColor(Color.red);
				g.setFont(new Font("Seraph", Font.BOLD, 80));    // font name, style,size
				g.drawString("YOU LOSE!",137,390);
				g.setColor(Color.white);
				g.setFont(new Font("Times", Font.BOLD, 30));
				g.drawString("LIVES : "+0, 504,25);
			}
			
			gameOver = gameOver();
			if(gameOver)
			{
				stopTimers();
			}

			requestFocus();// Requests that this Component get the input focus

			if(level3&&(seconds==10||seconds==9)&&!gameOver)
			{
				g.setColor(Color.blue);
				g.setFont(new Font("Seraph", Font.BOLD, 80));    // font name, style,size
				g.drawString("LEVEL 3",157,390);
			}
			else if(level2&&(seconds==20||seconds==19)&&!gameOver)
			{
				g.setColor(Color.blue);
				g.setFont(new Font("Seraph", Font.BOLD, 80));    // font name, style,size
				g.drawString("LEVEL 2",157,390);
			}
			else if(level1&&(seconds==20||seconds==19)&&!gameOver)
			{
				g.setColor(Color.blue);
				g.setFont(new Font("Seraph", Font.BOLD, 80));    // font name, style,size
				g.drawString("LEVEL 1",157,390);
			}

			if(level1==true && seconds==0 && !gameOver)
			{
				level2 = true;
				level1 = false;
				init();
			}
			if(level2==true && seconds==0 && !gameOver)
			{
				level3 = true;
				level2 = false;
				level1 = false;
				init();
			}
			gameOver = gameOver(); 
			if(gameOver) //if the game is over
			{
				stopTimers(); //stop the timers
			}
			//draw instructions--need new panel
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 17));    // font name, style, size

			g.drawString("Directions: a = left, d = right, w = up, " +
					"s = down, r = restart level, p = restart game",10,700);
		}

		public void keyPressed(KeyEvent e)
		{
			char ch = e.getKeyChar(); //get the key they typed
			String str = "";
			//empty string,string representation of the char they entered
			str+=ch;  //set string to equal the char

			if(str.equalsIgnoreCase("w")) //north
			{
				lastCharTyped = 'w';
				pacmanY--; //up 5
			}
			else if(str.equalsIgnoreCase("a")) //west
			{
				lastCharTyped = 'a';
				pacmanX--; //left 5
			}
			else if(str.equalsIgnoreCase("d")) //east
			{
				lastCharTyped = 'd';
				pacmanX++;    //right 5
			}
			else if(str.equalsIgnoreCase("s")) //south
			{
				lastCharTyped = 's';
				pacmanY++; //down 5
			}
			else if(str.equalsIgnoreCase("q"))
			{
				laserOn = true;
			}
			else if(str.equalsIgnoreCase("r")) //reset
			{
				init();
				stopTimers();
				startTimers();
			}
			else if(str.equalsIgnoreCase("p"))
			{
				init();
				level1 = true;
				level2 = false;
				level3 = false;
				stopTimers();
				startTimers();
				numLives = 3;
			}
			else if(str.equalsIgnoreCase("1"))
			{
				level1 = true;
				level2 = false;
				level3 = false;
				stopTimers();
				startTimers();
			}
			else if(str.equalsIgnoreCase("2"))
			{
				level2 = true;
				level1 = false;
				level3 = false;
				stopTimers();
				startTimers();
			}
			else if(str.equalsIgnoreCase("3"))
			{
				level3 = true;
				level2 = false;
				level1 = false;
				stopTimers();
				startTimers();
			}
			else if(str.equalsIgnoreCase("0"))
			{
				numLives = 99;
			}
			else
			{
				//if input is not valid, do not use it (o,i,p,etc)
				return;
			}
			pacmanX+=10; //in case it is negative
			pacmanY+=10; //in case it is negative
			pacmanX%=10; //wrap around
			pacmanY%=10; //wrap around
			for(int i = 0; i < numMonst; i++)
			{
				if(pacmanX==monsterX[i]&&pacmanY==monsterY[i])
					loseLife();
			}
			if((pacmanX==monsterX[0]||pacmanY==monsterY[0])&&(laserOn)&&level3)
				loseLife();

			if(!gameOver())
			{
				repaint();    // call paintComponent()
			}
		}

		public void keyReleased(KeyEvent e)
		{

		}

		public void keyTyped(KeyEvent e)
		{

		}
	}
}