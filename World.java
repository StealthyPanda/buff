package buff;
import java.util.*;

public class World
{
	//private Vector3[] levects;

	public Primitive[] leobjects;
	public Vector3 origin;
	public volatile boolean physics = true, gravity = true;
	public int timedelayinmillisecs = 1;

	VelocityThread vt;



	public World()
	{
		origin = new Vector3();
		vt = new VelocityThread(this);
		//leobjects = {};
	}

	public World(Primitive[] prims)
	{
		origin = new Vector3();
		vt = new VelocityThread(this);
		leobjects = prims;
	}

	public void startPhysics()
	{
		vt.start();
	}
}

class VelocityThread extends Thread
{
	World targetworld;

	long prevtime, currtime;

	long bufftime;

	int r = 0;

	public VelocityThread(World world)
	{
		targetworld = world;
	}

	public void run()
	{
		prevtime = System.nanoTime();
		//bufftime = System.nanoTime();
		while(true)
		{
			currtime = System.nanoTime();
			if (targetworld.physics)
			{
				//System.out.print("Called "); System.out.print(r);
				for (Primitive obj : targetworld.leobjects) 
				{
					if(obj == null) continue;
					//System.out.println()
					obj.move(obj.velocity.getMultiplied((currtime - prevtime) * Math.pow(10, -9)));	
				}



				//delayer.
				//try { Thread.sleep(targetworld.timedelayinmillisecs); } catch (Exception e) { System.out.println(e.getStackTrace()); }

				r++;
			}
			prevtime = currtime;
			//bufftime = System.nanoTime();

		}
		
	}
}