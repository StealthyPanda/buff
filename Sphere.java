package buff;

public class Sphere
{

	public double radius;
	public volatile Vector3 position, orientation;

	public Sphere()
	{
		radius = 1;
		position = new Vector3();
		orientation = new Vector3(Vector3.xaxis);
	}

	public Sphere(Vector3 position, double radius)
	{
		this.position = position;
		this.radius = radius;
		orientation = new Vector3(Vector3.xaxis);
	}

	public Sphere(Vector3 position, double radius, Vector3 orientation)
	{
		this.position = position;
		this.radius = radius;
		this.orientation = orientation;
	}

	


}