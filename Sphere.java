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
		//if (faces == null) return;
		try
		{
			for (int i = 0; i < faces.length; i++) 
			{
				faces[i].position = Vector3.add(faces[i].position, movement);
			}
		}
		catch (NullPointerException e)
		{
			return;
		}
	}

	public void translateTo(Vector3 newposition)
	{
		calculatePosition();
		//System.out.println("current position = " + this.position.toString());
		Vector3 movement = Vector3.add(newposition, this.position.getMultiplied(-1));
		//System.out.println("movement = " + movement.toString());
		translate(movement);
	}

	public void calculatePosition()
	{
		if (this.vertices == null) return;
		Vector3 buff = new Vector3();
		for (int i = 0; i < vertices.length; i++) 
		{
			buff = Vector3.add(buff, vertices[i]);
		}
		this.position = buff.divide(vertices.length);
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
		this.orientation = Quaternion.multiply(this.orientation, Quaternion.getRotor(axis, angleinradians)).clean();
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
		this.orientation = Quaternion.multiply(this.orientation, rotor).clean();
	}


	public void rotateAbout(Vector3 pivot, Vector3 axis, double angleinradians)
	{
		
		for (int i = 0; i < vertices.length; i++ ) 
		{
			vertices[i] = Quaternion.rotateAbout(vertices[i], pivot, axis, angleinradians);
		}
		this.position.rotateAbout(pivot, axis, angleinradians);
		//this.normal = getNormal();
		this.orientation = Quaternion.multiply(this.orientation, Quaternion.getRotor(axis, angleinradians));

	}

	public void rotateAbout(Vector3 pivot, Quaternion rotor)
	{
		
		for (int i = 0; i < vertices.length; i++ ) 
		{
			vertices[i] = Quaternion.rotateAbout(vertices[i], pivot, rotor);
		}
		this.position.rotateAbout(pivot, rotor);
		this.orientation = Quaternion.multiply(this.orientation, rotor);
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