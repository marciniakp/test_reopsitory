package Tanks;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

import Levels.*;


public class Tanks extends JPanel implements ActionListener, KeyListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int HEIGHT = 600, WIDTH = 600;
	public static Tanks tanks;
	private Renderer renderer;
	private Rectangle tank, bullet, star;
	private int motion = 2, score=0, normalShotSpeed = 150, ticks = 0,lives = 3, shootingTank = -1, flag, ticks2 = 0, endTicks=0;
	private Timer timer, timer2;
	private Random rand;
	private ArrayList<Rectangle> alltanks, playerBullets, stars, enemiesBullets;
	private boolean movingLeft, movingRight, normalShoot, started, gameOver, destroyed, playAgain = false;
	private BufferedImage rocket, enemyRocket, hearth;
	private File rocketImageFile, enemyImageFile, hearthImageFile;
	public static int level;
	Level1 level1;
	Level2 level2;
	Level3 level3;
	
	
	public Tanks() throws IOException		//konstruktor
	{
		
		PrepareGame();
		
		CreateWindow();
				
		CreateStars();
		
	}
	

	public void repaint(Graphics g) 	//f. wywolywana przez renderer
	{
		
				
		PaintBackground(g);
		
		PaintRockets(g);
		
		PaintBullets(g);
		
		
		
		if(!started)
		{
			StartGame(g);
		}
		
		if(started)
		{
			ViewScoreAndLives(g);
		}
		
		PaintLevels(g);
		
		
	}
	

	public static void main(String[] args) throws IOException 
	{
		tanks = new Tanks();
	}


	@Override
	public void actionPerformed(ActionEvent e)  	//obsluga timerow
	{
		
		if(e.getSource()==timer) //odswiezanie obrazu
		{
			if(started && !gameOver){
			ticks ++; //zmienna do predkosci strzalow
			///ruch do krawedzi
				if(tank.x>20)
				{
			
					if(movingLeft == true)
					{
						tank.x-=motion;
					}
				}
				
				if(tank.x<550)
				{
					if(movingRight == true)
					{
						tank.x+=motion;
					}
				}
				/////////////////////////
			
			
				///strzal
				if(ticks > normalShotSpeed && normalShoot==true)
				{
					ticks = 0;
					int pociskX = tank.x+12;
					int pociskY = tank.y-5;
					bullet = new Rectangle(pociskX, pociskY, 4, 8);
					playerBullets.add(bullet);
					normalShoot = false;
				}
				
				for(Rectangle k : playerBullets)
				{
					k.y-=2; 
				}
				
				for(Rectangle k : enemiesBullets)
				{
					k.y+=1; 
				}
				
				
				for(int i = 0; i < playerBullets.size();i++)
				{
					Rectangle pociskk = playerBullets.get(i);
					if(pociskk.y<-10)
						playerBullets.remove(pociskk);
				}
				
				for(int i = 0; i < enemiesBullets.size();i++)
				{
					Rectangle bullet = enemiesBullets.get(i);
					if(bullet.y>650)
						enemiesBullets.remove(bullet);
					if(bullet.intersects(tank)&&!destroyed)
					{
						enemiesBullets.remove(bullet);
						lives--;
						destroyed = true;
						
					}
						
				}
				
				
				for (int i=0;i<alltanks.size();i++)
				{
					
					Rectangle tanky=alltanks.get(i);
					
		
					if(tank.intersects(tanky)&&!destroyed)
					{
						lives--;
						destroyed = true;
					}
					
					for(int j = 0; j < playerBullets.size();j++)
					{
						Rectangle pociskk = playerBullets.get(j);
						if(pociskk.intersects(tanky))
						{
							alltanks.remove(tanky);
							playerBullets.remove(j);
							score+=10;
						 	
						}
					}
				}
				
				for(Rectangle gw : stars)
				{
					gw.y+=1;
					
					if(gw.y>590)
					{
						gw.y = 0;
						gw.x = rand.nextInt(600);
					}
					
				}
				
				if(lives==0)
					gameOver = true;
				if(level==1)
					level1.Gameplay(alltanks);
				if(level==2)
					level2.Gameplay(alltanks);
				if(level==3)
					level3.Gameplay(alltanks);
				
				EnemiesShots();
				
				renderer.repaint();

			}
			//chwilowa przerwa po przegranej
			if(gameOver)
			{
				endTicks ++;
					
				if(endTicks>300)
				{
							
					endTicks = 0;
					playAgain = true;
					
				}				
			}
			//////////////////////////////
		}
		
		if(e.getSource()==timer2)
		{
			//utrata zycia/////////////////////////////////////////////////////////////////////
			if(destroyed)
			{
				if(flag == 1)
				{
					tank.width=0;
					tank.height=0;
					flag = 0;
					ticks2++; // czas trwania mrugania
				}
				else
				{
					tank.width = 30;
					tank.height = 45;
					flag = 1;
				}
				if(ticks2 == 10)
				{
					tank.width = 30;
					tank.height = 45;
					destroyed = false;
					ticks2 = 0;
				}
			}
			
			
						
		}
		//////////////////////////////////////////////////////////////////////////////////////
	}
	
	
	@Override
	public void keyPressed(KeyEvent key) 		//klawisz wcisniety
	{
		
		if(key.getKeyCode()==KeyEvent.VK_LEFT)
		{
			
			movingLeft=true;
			
		}
		
		if(key.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			
			movingRight=true;
			
		}

		if(key.getKeyCode()==KeyEvent.VK_SPACE)
		{
			normalShoot = true;
		}
		
		if(!started)
		{
			started = true;
			level = 1;
		}
		
		if(gameOver && playAgain)
		{
			PlayAgain();
		}
	}


	@Override
	public void keyReleased(KeyEvent key) 		//klawisz puszczony
	{
		
			
		if(key.getKeyCode()==KeyEvent.VK_LEFT)
		{
			movingLeft=false;
		}
		
		if(key.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			
			movingRight=false;
			
		}
		
		if(key.getKeyCode()==KeyEvent.VK_SPACE)
		{
			normalShoot = false;
		
		}
	
	}
	
	
	public void PaintBackground(Graphics g)		//rysowanie tla
	{
		
		g.setColor(new Color(10,10,10));
		g.fillRect(0, 0, WIDTH, HEIGHT);
			
		for(Rectangle gw : stars)
		{
			g.setColor(new Color(rand.nextInt(150)+100,rand.nextInt(150)+100,rand.nextInt(150)+100));
			g.fillOval(gw.x, gw.y, gw.width, gw.height);
		}
	}
	
	public void PaintRockets(Graphics g)	//rysowanie czolgow(rakiet)
	{
	//wrogowie
		for (Rectangle tanks:alltanks)
		{	
			g.drawImage(enemyRocket,tanks.x,tanks.y,tanks.width,tanks.height, this);
		}
			g.drawImage(rocket,tank.x,tank.y,tank.width,tank.height, this); // rakieta gracza
		
	}
	
	public void PaintBullets(Graphics g)	//rysowanie pociskow
	{
		
		for (Rectangle e : playerBullets){
				
			g.setColor(Color.CYAN);
			g.fillOval(e.x, e.y,e.width,e.height);
		}
		
		
		for (Rectangle e : enemiesBullets)
		{
			g.setColor(Color.RED);
			g.fillOval(e.x, e.y,e.width,e.height);
		}
		
		
	}
	
	private void EnemiesShots()		//f. odpowiedzialna za strzaly przeciwnika
	{
		int shot = rand.nextInt(80); //czestotliwosc strzalow(100), im mniejsza wartosc tym czestsze strzaly

		if(shot==1)
		{
			shootingTank = rand.nextInt(alltanks.size()+1)-1; // zabezpiezpiecznie gdy size = 0
			shot=0;
		}
		
		if(shootingTank>0)
		{
			Rectangle e = alltanks.get(shootingTank);
			enemiesBullets.add(new Rectangle(e.x+25, e.y+40,5,7));
			shootingTank=-1;
		}
	}
	
	private void ViewScoreAndLives(Graphics g)		//pokaz wynik oraz szanse
	{
		g.setColor(Color.white); 
		g.setFont(new Font("Arial",12,15));
		g.drawString("Score: " + score, 520, 594);
			
		for (int i=0;i<lives;i++)
		{
			g.drawImage(hearth, 20+i*18, 580, 15, 15, this);
		}
		
		if(gameOver)
		{
			g.setColor(Color.cyan); 
			g.setFont(new Font("Arial",12,43));
			g.drawString("Press any key to play again", 30, 200);
			
		}
	}
	
	private void StartGame(Graphics g)		//komunikat poczatkowy
	{
		g.setColor(Color.cyan);
		g.setFont(new Font("Arial",12,50));
		g.drawString("Press any key", 150, 200);
	}
	
	private void PlayAgain()		//rozpoczecie po przegranej
	{
		if(playAgain)
		{
			
			try {
				PrepareGame();
				CreateStars();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			level = 1;
			lives = 3;
			score = 0;
			endTicks = 0;
			gameOver = false;
		}
	}
	
	private void CreateStars()		//tworzenie listy zawierajacej gwiazdy
	{
		//gwiazdy
		for(int i=0;i<200;i++)
		{
			star = new Rectangle(rand.nextInt(600) , rand.nextInt(600) ,2,3);
			stars.add(star);
		}
	}
	
	private void CreateWindow()		//tworzenie okna glownego
	{
		
		renderer = new Renderer();
		JFrame jframe = new JFrame("Tanks");
		JPanel jpanel = new JPanel(new BorderLayout());
				
		jpanel.add(renderer);
		jpanel.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		jframe.getContentPane().add(jpanel);
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		jframe.pack();
		jframe.addKeyListener(this);
		
	}
	
	
	private void PrepareGame() throws IOException	//tworzenie instancji list, timerow 
	{
		
		tank = new Rectangle(WIDTH/2 - 15,HEIGHT - 70 ,30, 45);
		rand = new Random();
		alltanks = new ArrayList<Rectangle>();
		playerBullets = new ArrayList<Rectangle>(); 
		stars = new ArrayList<Rectangle>();
		enemiesBullets = new ArrayList<Rectangle>();
		
		timer = new Timer(3,this);
		timer2 = new Timer(200, this);
		level1 = new Level1();
		level2 = new Level2();
		level3 = new Level3();
		
		
		
		
		
		rocketImageFile = new File("rocket.jpg");  
		enemyImageFile = new File("spaceship.png");
		hearthImageFile = new File("hearth.jpg");
		
		
		rocket = ImageIO.read(rocketImageFile);
		enemyRocket = ImageIO.read(enemyImageFile);
		hearth = ImageIO.read(hearthImageFile);
		
		
		timer.start();
		timer2.start();
		
	}
	
	
	private void PaintLevels(Graphics g) 	//rozpoczynanie odpowiedniego levelu 
	{

		
		if(started && alltanks.size()>0)
		{
			if(alltanks.get(0).x<-50)
			{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Times New Roman",100,100));
			g.drawString("Level " + level,150,200);
			}
		}
		
		
		if(level == 1)
		{
			alltanks = level1.GetTanks();
			if(alltanks.size()==0)
				level++;
			
		}
		else if(level == 2)
		{
			alltanks = level2.GetTanks();
			if(alltanks.size()==0)
				level++;
		}
		
		else if(level == 3)
		{
			alltanks = level3.GetTanks();
			if(alltanks.size()==0)
				level++;
		}
		
		else if(level == 4)
		{
			
		}
		
		else if(level == 5)
		{
			
		}
		
		else if(level == 6)
		{
			
		}
	
		
	}


	@Override
	public void keyTyped(KeyEvent key) 
	{

	}
	
	
}
