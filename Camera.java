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
	public double kvalue = 0.1f;
	public volatile Frame frame;
	public Vector3 sunlight;
	public String renderpath = "C:\\Users\\pc\\Desktop\\renderpath";

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
		this.frame = new Frame(100);
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
		this.frame = new Frame(100);
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
		this.frame = new Frame(100);
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
		this.frame = new Frame(100);
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

	

	public void projectOnFrame(Plane face)
	{
		Material material = face.material;
		Vector3 sunlightvect = this.sunlight.getMultiplied(-1);
		double normalangle = Vector3.angle(face.normal, sunlightvect);

		double[][] projectedvertexcoordinates = new double[face.vertices.length][];
		double depth = 0;
		
		Ray ray;
		double localxcoord;
		double localycoord;
		Vector3 intersection;

		//System.out.print("length: ");
		//System.out.println(face.vertices.length);

		for (int i = 0; i < face.vertices.length; i++) 
		{
			ray = new Ray(this.position, face.vertices[i]);
			intersection = ray.getIntersection(this.sensor);
			//System.out.print("intersection: ");
			//System.out.println(intersection);
			if (intersection == null) continue;

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

	double getLowestDepthBlock(Block[] blocks)
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

	

	public void project()
	{
		updateRendergroup();
		//System.out.print("No. of rendergroup Objects: "); System.out.println(rendergroup.length);
		this.frame = new Frame(100);
		for (int i = 0; i < rendergroup.length; i++) 
		{
			//System.out.println("Buh here tho");
			//System.out.println(rendergroup[0]);
			if(rendergroup[i] == null) continue;
			
			for (int j = 0; j < rendergroup[i].faces.length; j++) 
			{
				//System.out.println("reacher dere");
				if (rendergroup[i].faces[j] == null) continue;

				try
				{
					projectOnFrame(rendergroup[i].getFace(rendergroup[i].faces[j]));
				}
				catch (NotCoplanarException e)
				{
					System.out.println("Couldnt project on the frame.");
				}

			}
			
		}

		this.frame.blocks = Arrays.copyOfRange(this.frame.blocks, 0, this.frame.index);

		quickSortBlocks(this.frame.blocks);
	}

	Sketcher sketcher;
	int rcounter = 0;

	public void captureFrame()
	{

		project();

		JFrame viewer = new JFrame("Hope");

		viewer.setSize(800, 450);

		System.out.println(sensorwidth * kvalue);

		sketcher = new Sketcher(this.frame, this.renderpath, sensorwidth * kvalue, sensorheight * kvalue, this);
		sketcher.setSize(1600, 900);


		/*BufferedImage image = new BufferedImage(sketcher.getWidth(),sketcher.getHeight(),BufferedImage.TYPE_INT_RGB);

		sketcher.printAll(image.createGraphics());

		System.out.println("Exporting to: " + "C:\\Users\\pc\\Desktop\\renderpath\\" + Integer.toString(rcounter) + ".png");
		try {ImageIO.write(image, "PNG", new File("C:\\Users\\pc\\Desktop\\renderpath\\" + Integer.toString(rcounter) + ".png"));}
		catch (IOException e) {System.out.println("uwu");}*/



		viewer.add(sketcher);

		viewer.setVisible(true);

		

	}

	public void recapture()
	{
		project();
		sketcher.update();
		sketcher.repaint();
	}

	public void savepic(String filename)
	{

		String filepath = this.renderpath + "\\" + filename + ".png";

		sketcher.savepic(filepath);

	}
}


class Sketcher extends Canvas
{

	Frame frame;
	String renderpath;
	double width, height;
	double scalar = 10;
	Camera camera;
	//World world;

	public Sketcher(Frame frame, String renderpath, double width, double height, Camera camera)
	{
		this.frame = frame;
		this.width = width;
		this.height = height;
		this.renderpath = renderpath;
		this.camera = camera;
	}

	public void update()
	{
		//System.out.println(camera);
		this.frame = camera.frame;
		this.width = camera.sensorwidth * camera.kvalue;
		this.height = camera.sensorheight * camera.kvalue;
		this.renderpath = camera.renderpath;
		this.frame = camera.frame;
	}

	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;

		setBackground(new Color(125, 220, 255));

		//iterating over the blocks in frame
		for (int i = 0; i < frame.index; i++) 
		{
			if (frame.blocks[i] == null) continue;
			Block block = frame.blocks[i];
			g2.setPaint(new Color(0, 0, 0));
			//System.out.print(block.material.r);
			//	System.out.print(" ");
			//System.out.print(block.material.g);
			//	System.out.print(" ");
			//System.out.println(block.material.b);
			double[][] coordinates = block.projectedvertexcoordinates;
			GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, coordinates.length);
			polygon.moveTo(Math.round((coordinates[0][0]/width) * 800 * scalar), Math.round((coordinates[0][1]/height) * 450 * scalar));

			for (int j = 1; j < coordinates.length; j++) 
			{
				//System.out.print(Math.round((coordinates[j][0]/width) * 800 * scalar));
				//System.out.print(" ");
				//System.out.print(width);
				//System.out.print(" ");
				//System.out.println(Math.round((coordinates[j][0]/width) * 450 * scalar));
				polygon.lineTo(Math.round((coordinates[j][0]/width) * 800 * scalar), Math.round((coordinates[j][1]/height) * 450 * scalar));
			}

			polygon.closePath();

			g2.draw(polygon);

			g2.setPaint(new Color(block.material.r, block.material.g, block.material.b));
			g2.fill(polygon);

		}


	}

	public void savepic(String imageName)
	{
		BufferedImage image = new  BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = image.createGraphics();
	    paint(graphics);
	    //graphics.dispose();
	    FileOutputStream out;
	    try {
	        System.out.println("Exporting image: "+imageName);
	        out = new FileOutputStream(imageName);
	        ImageIO.write(image, "png", out);
	        out.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
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