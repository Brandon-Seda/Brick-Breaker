import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	
	private boolean play = false; 
	private int score = 0;
	
	private int totalBricks = 21;
	private int brickValue = 5;
	
	private Timer timer;
	private int delay = 8;
	
	private int player1 = 310;
	
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	
	private MapGenerator map;
	
	
	public Gameplay() 
	{
		map = new MapGenerator(4, 12);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);

		timer.start();
	}
	
	@Override
	public void paint(Graphics g) 
	{
		// for background
		g.setColor(Color.black);
		g.fillRect(1,  1, 692, 592);
		
		//for map
		map.draw((Graphics2D) g);
		
		//borders
		g.setColor(Color.white);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//the paddle
		g.setColor(Color.pink);
		g.fillRect(player1, 550, 100, 8);
		
		
		//ball
		g.setColor(Color.white);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" +score , 590, 30);
		
		//win
		if(totalBricks <= 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Win!!", 260, 300);
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press -Enter- to Restart", 230, 350);
			
		}
		
		//lose
		if(ballPosY > 570) 
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score:"+score, 190, 300);
			
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press -Enter- to Restart", 230, 350);
			
		}
			
		
		g.dispose();
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) 
		{
			if(player1 >= 600) 
			{
				player1 = 600;
			} else 
			{
				moveRight();
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) 
		{
			if(player1 < 10) 
			{
				player1 = 10;
			} else 
			{
				moveLeft();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) 
		{
			if(!play) 
			{
				play = true;
				ballPosX = 120;
				ballPosY = 350;
				ballXdir = -1;
				ballYdir = -2;
				player1 = 310;
				score = 0;
				totalBricks = 21;
				map = new MapGenerator(3, 7);
				
				repaint();
				
			}
		}
		
	}
	

	private void moveLeft() 
	{
		play = true;
		player1 -= 20;
		
	}

	private void moveRight() 
	{
		play = true;
		player1 += 20;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		timer.start();
		
		if(play) 
		{
			if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(player1, 550, 30, 8)))
			{
				System.out.println("there");
				ballYdir = -ballYdir;
				ballXdir = -2;
			}
			else if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(player1 + 70, 550, 30, 8)))
			{
				System.out.println("around");
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
			else if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(player1 + 30, 550, 40, 8)))
			{
				System.out.println("nowhere");
				ballYdir = -ballYdir;
			}
			
			// check map collision with the ball		
			A: for(int i = 0; i < map.map.length; i++)
			{
				for(int j = 0; j < map.map[0].length; j++)
				{				
					if(map.map[i][j] > 0)
					{
						//increment scores
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						Rectangle brickRect = rect;
						Rectangle paddleRectTop = new Rectangle(player1, 550, 30, 8);
						Rectangle paddleRectSide = new Rectangle(player1 + 70, 550, 30, 8);
						Rectangle paddleRect = new Rectangle(player1 + 30, 550, 40, 8);
						
						
						if(ballRect.intersects(brickRect))
						{					
							map.setBrickValue(0, i, j);
							score += brickValue;
							
							totalBricks--;
							
							// checks for ball hitting left or right of brick 
							if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// checks for when ball hits top or bottom of brick
							else
							{
								ballYdir = -ballYdir;				
							}
							
							break A;
						}
					}
				}
			}
			
			ballPosX += ballXdir;
			ballPosY += ballYdir;
			
			if(ballPosX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballPosY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballPosX > 670)
			{
				ballXdir = -ballXdir;
			}		
			
			repaint();	
		}
	}


	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	
}
