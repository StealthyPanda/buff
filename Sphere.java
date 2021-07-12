package buff;

class Shell
{
	public volatile Vector3 position;
	public volatile Quaternion orientation;
	public Vector3[] vertices;
	public Plane[] faces;
	public void translate(Vector3 movement)
	{
		for (int i = 0; i < vertices.length; i++)
		{
			vertices[i] = Vector3.add(vertices[i], movement);
		}
		position = Vector3.add(position, movement);
		if (faces == null) return;
		for (int i = 0; i < faces.length; i++) 
		{
			faces[i].position = Vector3.add(faces[i].position, movement);
		}
	}

	public void calculatePosition()
	{
		Vector3 buff = new Vector3();
		for (int i = 0; i < vertices.length; i++) 
		{
			buff = Vector3.add(buff, vertices[i]);
		}
		this.position = buff.multiply(1/vertices.length);
	}

	public void rotateLocal(Vector3 axis, double angleinradians)
	{
		calculatePosition();
		Vector3 resettoorigin = this.position.getMultiplied(-1);
		for (int i = 0; i < vertices.length; i++) 
		{
			vertices[i] = Quaternion.rotate(Vector3.add(vertices[i], resettoorigin), axis, angleinradians);
		}
		translate(resettoorigin.getMultiplied(-1));
	}

	public void rotateLocal(Quaternion rotor)
	{
		calculatePosition();
		Vector3 resettoorigin = this.position.getMultiplied(-1);
		for (int i = 0; i < vertices.length; i++) 
		{
			vertices[i] = Quaternion.rotate(Vector3.add(vertices[i], resettoorigin), rotor);
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
		orientation = new Quaternion(1, 0, 0, 0);
	}

	public Sphere(Vector3 position, double radius)
	{
		this.position = position;
		this.radius = radius;
		this.orientation = new Quaternion(1, 0, 0, 0);
	}

	public Sphere(Vector3 position, double radius, Quaternion orientation)
	{
		this.position = position;
		this.radius = radius;
		this.orientation = orientation;
	}

	void populatevertices()
	{



	}




}