package Levels;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;


public class Level1{

	
	private ArrayList<Rectangle> alltanks;
	boolean move = true;
	
	public Level1(){
		
		alltanks = new ArrayList<Rectangle>();
		new Random();
		for (int i=0;i<8;i++)
		{
			for(int j=0;j<1;j++)
			{
			alltanks.add(new Rectangle(-620-i*67, 60+j*60, 50, 60));
			}
		}
	}
	
	public void Gameplay(ArrayList<Rectangle> tankies)
	{
		
		for (Rectangle tank : tankies)
		{
			if(move == true)
			{ 	
				tank.x +=1;
				 if(tank.x >500)
					 move = false;
			}
		}
	}
	
	public ArrayList<Rectangle> GetTanks()
	{
		return alltanks;
	}
}

