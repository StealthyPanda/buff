package messenger;
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

	int r = 0;

	public VelocityThread(World world)
	{
		targetworld = world;
	}

	public void run()
	{
		while(true)
		{

			if (targetworld.physics)
			{
				//System.out.print("Called "); System.out.print(r);
				for (Primitive obj : targetworld.leobjects) 
				{
					if(obj == null) continue;
					//System.out.println()
					obj.move(obj.velocity.getMultiplied(targetworld.timedelayinmillisecs * 0.001));	
				}



				//delayer.
				try { Thread.sleep(targetworld.timedelayinmillisecs); } catch (Exception e) { System.out.println(e.getStackTrace()); }

				r++;
			}

		}
		
	}
}