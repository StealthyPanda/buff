package buff;

import java.awt.*;
import java.awt.color.*;
import javax.swing.JFrame;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;


public class Camera
{
	public volatile Vector3 position;
	public volatile Quaternion orientation;
	public World targetworld;
	public Shell[] rendergroup;
	public volatile Plane sensor;
	public double sensorwidth = 16, sensorheight = 9, sensordistance = 0.2;
	public Vector3 localx, localy;
	public double kvalue = 1f;
	public volatile Frame frame;
	public Vector3 sunlight;
	public String renderpath = "C:\\Users\\pc\\Desktop\\renderpath";
	public int blocklimit = 200;
	public RenderedFrame renderedframe;
	public String name = "0";

	void initiateLocalAxes()
	{
		localx = new Vector3(0, -1, 0);
		localy = new Vector3(0, 0, 1);

		localx.rotate(orientation);
		localy.rotate(orientation);
	}

	void updateLocalAxes(Vector3 axis, double angleinradians)
	{
		this.localx.rotate(axis, angleinradians);
		this.localy.rotate(axis, angleinradians);
	}

	void updateLocalAxes(Quaternion rotor)
	{
		this.localx.rotate(rotor);
		this.localy.rotate(rotor);
	}

	void updateLocalAxes()
	{
		this.localx = new Vector3(0, -1, 0).rotate(orientation);
		this.localy = new Vector3(0, 0, 1).rotate(orientation);
	}

	Plane generateSensor()
	{
		Vector3 topright = new Vector3(0, -(sensorwidth * kvalue)/2, (sensorheight * kvalue)/2);
		Vector3 topleft = new Vector3(0, (sensorwidth * kvalue)/2, (sensorheight * kvalue)/2);
		Vector3 bottomleft = new Vector3(0, (sensorwidth * kvalue)/2, -(sensorheight * kvalue)/2);
		Vector3 bottomright = new Vector3(0, -(sensorwidth * kvalue)/2, -(sensorheight * kvalue)/2);

		Plane plane = null;

		try
		{
			//plane = new Plane(topright, topleft, bottomleft, bottomright);
			plane = new Plane(bottomright, bottomleft, topleft, topright);
			plane.translate(new Vector3(sensordistance, 0, 0));
		} 
		catch(NotCoplanarException e)
		{
			System.out.println("Couldnt generate sensor.");
		}

		plane.rotateLocal(orientation);
		plane.translate(position);

		return plane;
	}

	public Camera() //testing purposes only
	{
		this.position = new Vector3(0, 0, 0);
		this.orientation = new Quaternion(1, 0, 0, 0);
		//this.targetworld = targetworld;
		//this.rendergroup = this.targetworld.rendergroup;
		sensor = generateSensor();
		initiateLocalAxes();
		this.frame = new Frame(blocklimit);
		this.sunlight = targetworld.light;
	}

	public Camera(World targetworld)
	{
		this.position = new Vector3(0, 0, 0);
		this.orientation = new Quaternion(1, 0, 0, 0);
		this.targetworld = targetworld;
		this.rendergroup = this.targetworld.rendergroup;
		sensor = generateSensor();
		initiateLocalAxes();
		this.frame = new Frame(blocklimit);
		this.sunlight = targetworld.light;
	}

	public Camera(World targetworld, Vector3 position)
	{
		this.position = position;
		this.orientation = new Quaternion(1, 0, 0, 0);
		this.targetworld = targetworld;
		this.rendergroup = this.targetworld.rendergroup;
		sensor = generateSensor();
		initiateLocalAxes();
		this.frame = new Frame(blocklimit);
		this.sunlight = targetworld.light;
	}

	public Camera(World targetworld, Vector3 position, Quaternion orientation)
	{
		this.position = position;
		this.orientation = orientation;
		this.targetworld = targetworld;
		this.rendergroup = this.targetworld.rendergroup;
		sensor = generateSensor();
		initiateLocalAxes();
		this.frame = new Frame(blocklimit);
		this.sunlight = targetworld.light;
	}

	public void translate(Vector3 movement)
	{
		this.position = Vector3.add(this.position, movement);
		this.sensor.translate(movement);
	}

	public void translateTo(Vector3 destination)
	{
		Vector3 movement = Vector3.add(destination, this.position.getMultiplied(-1));
		translate(movement);
	}

	public void rotateLocal(Quaternion rotor)
	{
		this.orientation = Quaternion.multiply(this.orientation, rotor);
		this.sensor.rotateAbout(this.position, rotor);
		updateLocalAxes(rotor);
	}

	public void rotateLocal(Vector3 axis, double angleinradians)
	{
		this.orientation = Quaternion.multiply(this.orientation, Quaternion.getRotor(axis, angleinradians));
		this.sensor.rotateAbout(this.position, axis, angleinradians);
		updateLocalAxes(axis, angleinradians);
	}

	public void printInfo()
	{
		System.out.println("Camera position: " + this.position.getClean().toString());
		System.out.println("Camera orientation: " + this.orientation.getClean().toString());
		System.out.println("Camera orientation vect: " + this.orientation.getVectorOrientation().getClean().toString());
		System.out.println("Sensor position: " + this.sensor.position.getClean().toString());
		System.out.println("Sensor orientation: " + this.sensor.normal.getClean().toString());
		System.out.println("Sensor distance: " + Vector3.add(this.sensor.position, this.position.getMultiplied(-1)).getClean().toString());
		System.out.println();
	}

	

	public void projectFaceOnSensor(Plane face)
	{

		localx.normalise();
		localy.normalise();

		Material material = face.material;
		//Vector3 sunlightvect = this.sunlight.getMultiplied(-1);
		double normalangle = Vector3.angle(face.normal, sunlight.getMultiplied(-1));

		double[][] projectedvertexcoordinates = new double[face.vertices.length][];
		double depth = 0;
		
		Ray ray;
		double localxcoord;
		double localycoord;
		Vector3 intersection;

		

		for (int i = 0; i < face.vertices.length; i++) 
		{
			ray = new Ray(this.position, face.vertices[i]);

			intersection = ray.getIntersection(this.sensor);
			
			if (intersection == null) continue;

			//localxcoord = Vector3.dotproduct(Vector3.add(intersection, this.sensor.vertices[2].getMultiplied(-1)), localx.getMultiplied(1));
			//localycoord = Vector3.dotproduct(Vector3.add(intersection, this.sensor.vertices[2].getMultiplied(-1)), localy.getMultiplied(-1));

			localxcoord = Vector3.dotproduct(intersection, localx);
			localycoord = Vector3.dotproduct(intersection, localy);

			double[] localcoordinates = {localxcoord, localycoord};
			projectedvertexcoordinates[i] = localcoordinates;

			double raylength = Vector3.add(intersection, face.vertices[i].getMultiplied(-1)).getMagnitude();

			if (i == 0)
			{
				depth = raylength;
			}
			else
			{
				if (raylength < depth) depth = raylength;
			}
		}

		this.frame.addBlock(new Block(projectedvertexcoordinates, material, depth, normalangle));

	}

	void updateRendergroup()
	{
		this.rendergroup = targetworld.rendergroup;
	}

	double getLowestDepthOfBlocks(Block[] blocks)
	{
		double lowestdepth = 0;
		for (int i = 0; i < blocks.length; i++) 
		{

			if (i == 0) lowestdepth = blocks[i].depth;
			if (blocks[i].depth < lowestdepth) lowestdepth = blocks[i].depth;
			
		}
		return lowestdepth;
	}

	public static boolean isSorted(Block[] blocks)
	{
		for (int i = 1; i < blocks.length; i++) 
		{
			if (blocks[i].depth < blocks[i-1].depth) return false;
		}
		return true;
	}

	public static Block[] reverse(Block[] blocks)
	{
		Block[] buffer = new Block[blocks.length];
		for (int i = 0; i < blocks.length; i++) 
		{
			buffer[blocks.length - i - 1] = blocks[i];
		}
		return buffer;
	}

	public static void printBlocks(Block[] blocks)
	{
		System.out.println();
		for (int i = 0; i < blocks.length; i++)
		{
			if (blocks[i] == null)
			{
				System.out.println("NULL");
				continue;
			}
			System.out.println(blocks[i].depth);
		}
		System.out.println();
	}

	
	public static Block[] quickSortBlocks(Block[] blocks)
	{	
		ArrayList<Block> buffer = new ArrayList<Block>();
		for (int i = 0; i < blocks.length ; i++) 
		{
			buffer.add(blocks[i]);
		}
		Collections.sort(buffer, new BlockComparor());
		for (int i = 0; i < blocks.length; i++) 
		{
			blocks[i] = buffer.get(i);
		}
		return blocks;
	}

	
	//this one generates a complete frame
	public void projectWorldOnSensor()
	{
		updateRendergroup();

		this.frame = new Frame(blocklimit);

		for (int i = 0; i < rendergroup.length; i++) 
		{
			
			if(rendergroup[i] == null) continue;
			
			for (int j = 0; j < rendergroup[i].faces.length; j++) 
			{
				if (rendergroup[i].faces[j] == null) continue;

				try
				{
					projectFaceOnSensor(rendergroup[i].getFace(rendergroup[i].faces[j]));
				}
				catch (NotCoplanarException e)
				{
					System.out.println("Couldnt project on the frame.");
				}

			}
			
		}

		this.frame.blocks = Arrays.copyOfRange(this.frame.blocks, 0, this.frame.index);

		this.frame.blocks = quickSortBlocks(this.frame.blocks);
	}

	
	//this one generates a complete RenderedFrame by capturing rawframe
	public RenderedFrame captureFrame(int width, int height)
	{

		projectWorldOnSensor();

		this.renderedframe = new RenderedFrame(this.frame, this, width, height);
		this.renderedframe.setSize((int) width, (int) height);

		return this.renderedframe;

	}

	public void display()
	{
		Display display = new Display(this, this.renderedframe);
		display.enable();
	}

	
}

class Display
{
	JFrame jf;
	public Display(Camera cam, RenderedFrame rf)
	{
		jf = new JFrame("Camera Display - " + cam.name);
		jf.setLayout(null);
		jf.setSize(rf.getWidth(), rf.getHeight());
		jf.add(rf);
	}

	public void enable()
	{
		jf.setVisible(true);
	}

	public void disable()
	{
		jf.setVisible(false);
	}
}




class BlockComparor implements Comparator<Block>
{

	public int compare(Block a, Block b)
	{
		if ((a.depth - b.depth) >= 0) return -1;
		if ((a.depth - b.depth) < 0) return 1;
		return 0;
	}

}