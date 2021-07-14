package buff;

import java.awt.Canvas;

public class Camera
{
	public volatile Vector3 position;
	public volatile Quaternion orientation;
	public World targetworld;
	public Shell[] rendergroup;
	public volatile Plane sensor;
	public float sensorwidth = 16, sensorheight = 9, sensordistance = 0.2f;
	public Vector3 localx, localy;
	public float kvalue = 0.1f;
	public volatile Frame frame;
	public Vector3 sunlight;

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
		this.frame = new Frame(1);
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
		this.frame = new Frame(1);
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
		this.frame = new Frame(1);
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
		this.frame = new Frame(1);
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
		Vector3 intersection;
		double localxcoord = 0, localycoord = 0;
		double depth, normalangle;
		Ray ray;
		Material mat;
		Vector3 sunlightvect = this.sunlight.getMultiplied(-1);

		for (int i = 0; i < face.vertices.length; i++) 
		{
			ray = new Ray(face.vertices[i], this.position);
			intersection = ray.getIntersection();
			localxcoord = Vector3.dotproduct(intersection, localx);
			localycoord = Vector3.dotproduct(intersection, localy);
			mat = face.material;
			depth = ray.getMagnitude();
			normalangle = Vector3.angle(face.normal, sunlightvect);
			double[] coordinates = {localx, localy};
			//todo: this.frame.addBlock(new Block(coordinates), )
		}



	}


	public void project()
	{
		for (int i = 0; i < rendergroup.length; i++) 
		{
			
		}
	}



	public void captureFrame()
	{



	}
}

class Block
{

	double[][] projectedvertexcoordinates;
	Material material;
	double depth;
	double normalangle;

	public Block(double[][] projectedvertexcoordinates, Material material, double depth, double normalangle)
	{
		this.projectedvertexcoordinates = projectedvertexcoordinates;
		this.material = material;
		this.depth = depth;
		this.normalangle = normalangle;
	}

}

class Frame
{
	Block[] blocks = null;
	int index = 0;

	public Frame(int numberofblocks)
	{
		Block[] buff = new Block[numberofblocks]; blocks = buff;
	}

	public void addBlock(Block block)
	{
		if (blocks == null) return;
		blocks[index] = block;
		index++;
	}
}