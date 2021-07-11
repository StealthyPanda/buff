package buff;

class Shell
{
	public volatile Vector3 position, orientation;
	public Vector3[] vertices;
	public Plane[] faces;
}

public class Sphere extends Shell
{

	public double radius;

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
		this.orientation = new Vector3(Vector3.xaxis);
	}

	public Sphere(Vector3 position, double radius, Vector3 orientation)
	{
		this.position = position;
		this.radius = radius;
		this.orientation = orientation;
	}

	void populatevertices()
	{



	}




}