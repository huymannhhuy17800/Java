/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;
/**
 *
 * @author DELL
 */
public class HappyFrog implements ActionListener, KeyListener{

	public static HappyFrog happyFrog;

	public final int WIDTH = 800, HEIGHT = 800;

	public Renderer renderer;

	public Rectangle frog;

	public ArrayList<Rectangle> columns;

	public int ticks, yMotion, score;

	public boolean gameOver, started;

	public Random rand;

        // setup
	public HappyFrog(){
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setTitle("Happy Frog");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setVisible(true);

		frog = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

        // add column so that its always even
	public void addColumn(boolean start){
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start){
			columns.add(new Rectangle(WIDTH + width + columns.size() *300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else{                   
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600 , HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

        // paint color for column
	public void paintColumn(Graphics g, Rectangle column){
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

        // for jumping
	public void jump(){
		 if(gameOver){
                     frog = new Rectangle(WIDTH/ 2 -10, HEIGHT/ 2 - 10, 20, 20);
                     columns.clear();
                     yMotion = 0;
                     score = 0;
                     
                     addColumn(true);
                     addColumn(true);
                     addColumn(true);
                     addColumn(true);
                     
                     gameOver = false;
                 }
                 if(!started){
                     started = true;
                 }
                 if(!gameOver && started){
                     if(yMotion > 0){
                         yMotion = 0;
                     }
                     yMotion -= 10;
                 }
	}

        // action when press key
        @Override
	public void actionPerformed(ActionEvent e){
		int speed = 10;
                int speed1 = 20;
		ticks++;

		if (started){
			for (int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
                                column.x -= 10;				
			}

			if (ticks % 2 == 0 && yMotion < 15){
				yMotion += 2;
			}
                        
			for (int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0){
					columns.remove(column);

					if (column.y == 0){
						addColumn(false);
					}
				}
			}

			frog.y += yMotion;

			for (Rectangle column : columns){
				if (column.y == 0 && frog.x + frog.width / 2 > column.x + column.width / 2 - 10 && frog.x + frog.width / 2 < column.x + column.width / 2 + 10)
				{
					score++;
				}

				if (column.intersects(frog)){
					gameOver = true;
				}
			}
                       
			 if(frog.y > HEIGHT -120 || frog.y < 0){
                            gameOver = true;
                        }
                        if (frog.y + yMotion >= HEIGHT - 120){
				gameOver = true;
			}
		}

		renderer.repaint();
	}

        // paint when play
	public void repaint(Graphics g){
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT -120 , WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT -120, WIDTH, 20);

		g.setColor(Color.red);
		g.fillRect(frog.x, frog.y, frog.width, frog.height);

		for (Rectangle column : columns)
		{
			paintColumn(g, column);
		}

		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 50));

		if (!started){
			g.drawString("Press space!", WIDTH-550, HEIGHT / 2 - 50);
		}
		if (gameOver){
			g.drawString("Game Over!", WIDTH - 550, HEIGHT/2 - 50);
		}
		if (!gameOver && started){
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	public static void main(String[] args){
		happyFrog = new HappyFrog();
	}


	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			jump();
		}
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    jump();
                }
	}
	

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

}
