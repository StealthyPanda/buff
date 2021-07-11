package buff;

class Shell
{
	public volatile Vector3 position, orientation;
	public Vector3[] vertices;
	public Plane[] faces;
	public void translate(Vector3 movement)
	{
		for (Vector3 vertex : vertices) 
		{
			vertex = Vector3.add(vertex, movement);
		}
		position = Vector3.add(position, movement);
		for (Plane face: faces) 
		{
			face.position = Vector3.add(position, movement);
		}
	}

	public void calculatePosition()
	{
		Vector3 buff = new Vector3();
		for (Vector3 vertex: vertices) 
		{
			buff = Vector3.add(buff, vertex);
		}
		this.position = buff.multiply(1/vertices.length);
	}

	public void rotateLocal(Vector3 axis, double angleinradians)
	{
		calculatePosition();
		Vector3 resettoorigin = this.position.getMultiplied(-1);
		for (Vector3 vertex: vertices) 
		{
			vertex = Quaternion.rotate(Vector3.add(vertex, resettoorigin), axis, angleinradians);
		}
		translate(resettoorigin.getMultiplied(-1));
	}
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