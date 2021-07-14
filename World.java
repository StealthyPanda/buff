package buff;
import java.util.*;

public class World
{
	//private Vector3[] levects;

	public Primitive[] leobjects;
	public Shell[] shells;
	public Shell[] rendergroup;
	public Plane[] rendergroupplanes;
	public Camera camera;
	public Vector3 origin;
	public volatile boolean physics = true;
	public int timedelayinmillisecs = 1;
	public static final double gvalue = 9.8;
	public volatile boolean gravity = false;


	VelocityThread vt;
	ForceThread ft;

	AddGravityThread agt;
	RemoveGravityThread rgt;



	public World()
	{
		origin = new Vector3();

		vt = new VelocityThread(this);
		ft = new ForceThread(this);

		//leobjects = {};
		camera = new Camera(this);
	}

	public World(Primitive[] prims)
	{
		origin = new Vector3();

		vt = new VelocityThread(this);
		ft = new ForceThread(this);

		leobjects = prims;

		camera = new Camera(this);
	}

	public void startPhysics()
	{
		vt.start();
		ft.start();
	}

	public void startPhysics(boolean gravitydefault)
	{
		vt.start();
		ft.start();
		if (gravitydefault) agt.start();
	}

	public boolean startGravity()
	{
		if (!gravity) 
		{
			new AddGravityThread(this).start(); gravity = true; return true;
		}
		return false;
	}

	public boolean stopGravity()
	{
		if (gravity)
		{
			new RemoveGravityThread(this).start(); gravity = false; return true;
		}
		return false;
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

class ForceThread extends Thread
{
	World targetworld;

	long prevtime, currtime;

	public ForceThread(World world)
	{
		targetworld = world;
	}

	public void run()
	{

		prevtime = System.nanoTime();

		while (true)
		{
			
			currtime = System.nanoTime();

			if (targetworld.physics)
			{

				for (Primitive obj : targetworld.leobjects) 
				{
					if (obj == null) continue;
					obj.velocity = Vector3.add(obj.velocity, obj.netforce.getMultiplied((1/obj.mass) * (currtime - prevtime) * Math.pow(10, -9)));	
				}


			}

			prevtime = currtime;
		}


	}
}

class AddGravityThread extends Thread
{

	World targetworld;

	public AddGravityThread(World world)
	{

		targetworld = world;

	}

	public void run()
	{

		targetworld.gravity = true;
		//System.out.println("HEre boi");

		for (Primitive obj : targetworld.leobjects) 
		{

			if (obj == null) continue;

			obj.netforce = Vector3.add(obj.netforce, new Vector3(0, -1 * World.gvalue * obj.mass, 0));
			
		}

	}
}

class RemoveGravityThread extends Thread
{

	World targetworld;

	public RemoveGravityThread(World world)
	{

		targetworld = world;

	}

	public void run()
	{

		targetworld.gravity = false;

		for (Primitive obj : targetworld.leobjects) 
		{

			if (obj == null) continue;

			obj.netforce = Vector3.add(obj.netforce, new Vector3(0, 1 * World.gvalue * obj.mass, 0));
			
		}

	}
}