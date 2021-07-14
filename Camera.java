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

	/*public void projectOnFrame(Plane face)
	{
		Vector3 intersection;
		double localxcoord = 0, localycoord = 0;
		double depth, normalangle;
		Ray ray;
		Material mat;
		Vector3 sunlightvect = this.sunlight.getMultiplied(-1);
		double[][] projectedvertexcoordinates = new double[][face.vertices.length];

		mat = face.material;
		normalangle = Vector3.angle(face.normal, sunlightvect);
		
		for (int i = 0; i < face.vertices.length; i++) 
		{
			ray = new Ray(face.vertices[i], this.position);
			intersection = ray.getIntersection();
			localxcoord = Vector3.dotproduct(intersection, localx);
			localycoord = Vector3.dotproduct(intersection, localy);
			if (i == 0) 
			{
				depth = ray.getMagnitude();
			}
			else
			{
				if (ray.getMagnitude() < depth) depth = ray.getMagnitude();
			}
			double[] coordinates = {localx, localy};
			projectedvertexcoordinates[i] = coordinates;
			//todo: this.frame.addBlock(new Block(coordinates), )
		}

		this.frame.addBlock(new Block(projectedvertexcoordinates, mat, depth, normalangle));



	}*/

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

		for (int i = 0; i < face.vertices.length; i++) 
		{
			ray = new Ray(face.vertices[i], this.position);
			intersection = ray.getIntersection(this.sensor);

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


	public void project()
	{
		updateRendergroup();
		//System.out.print("No. of rendergroup Objects: "); System.out.println(rendergroup.length);

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
	}



	public void captureFrame()
	{



	}
}
