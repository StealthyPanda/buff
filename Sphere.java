package buff;

class Shell
{
	public volatile Vector3 position;
	public volatile Quaternion orientation;
	public Vector3[] vertices;
	public int[][] faces;
	public Material material;


	public Plane getFace(int[] vertexindices) throws NotCoplanarException
	{
		if (vertexindices.length == 3) return new Plane(vertices[vertexindices[0]], vertices[vertexindices[1]], vertices[vertexindices[2]]).setMaterial(this.material);
		if (vertexindices.length == 4) return new Plane(vertices[vertexindices[0]], vertices[vertexindices[1]], vertices[vertexindices[2]], vertices[vertexindices[3]]).setMaterial(this.material);
		return null;
	}

	public Plane[] getAllFaces()
	{
		Plane[] allfaces = new Plane[faces.length];

		for (int i = 0; i < faces.length ; i++) 
		{
			try
			{
				allfaces[i] = getFace(faces[i]);
			}
			catch (NotCoplanarException e)
			{
				System.out.println("Couldn't build the following face: ");
				for (int x = 0; x < faces[i].length ; x++) 
				{
					System.out.println("-> " + vertices[faces[i][x]]);
				}
			}
			
		}

		return allfaces;
	}

	public void translate(Vector3 movement)
	{
		for (int i = 0; i < vertices.length; i++)
		{
			vertices[i] = Vector3.add(vertices[i], movement);
		}
		position = Vector3.add(position, movement);
		//if (faces == null) return;
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
	public int latitudes, longitudes;

	
	public Sphere(double radius, int latitudes, int longitudes)
	{
		this.position = new Vector3();
		this.radius = radius;
		this.orientation = new Quaternion(1, 0, 0, 0);
		populateVertices();
	}

	public Sphere(double radius, int latitudes, int longitudes, Vector3 position)
	{
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		this.position = position;
		this.radius = radius;
		this.orientation = new Quaternion(1, 0, 0, 0);
		populateVertices();
	}

	public Sphere(double radius, int latitudes, int longitudes, Vector3 position, Quaternion orientation)
	{
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		this.position = position;
		this.radius = radius;
		this.orientation = orientation;
		populateVertices();
	}

	
	void populateVertices()
	{
		Vector3 yaxis = new Vector3(0, 1, 0);
		Vector3 zaxis = new Vector3(0, 0, 1);



		Vector3 pointer = new Vector3(0, 0, this.radius);
		//buffer[0][0] = new Vector3(pointer); //north pole
		//buffer[this.latitudes] = new Vector3(pointer.getMultiplied(-1)); //south pole
		Vector3 northpole = new Vector3(pointer);
		Vector3 southpole = new Vector3(pointer.getMultiplied(-1));

		double yincrements = Math.PI/(this.latitudes + 1);
		double zincrements = (2 * Math.PI)/(this.longitudes);


		Vector3[][] buffer = new Vector3[this.latitudes + 2][this.longitudes];
		buffer[0] = {northpole};
		buffer[this.latitudes + 1] = {southpole};

		pointer.rotate(yaxis, yincrements);
		for (int i = 0 ; i < this.latitudes; i++) 
		{
			
			for (int j = 0; j < this.longitudes; j++ ) 
			{
				
				buffer[i + 1][j] = new Vector3(pointer);
				pointer.rotate(zaxis, zincrements);

			}

			pointer.rotate(yaxis, yincrements);

		}

		//this.vertices = buffer;

		//System.out.println(pointer);
		int[][] buff = new int[(this.latitudes + 1) * this.longitudes][];

		int counter = 0;
		for (int i = 1; i < this.latitudes + 1; i++) 
		{
			for (int j = 0; j < this.longitudes; j++) 
			{
				if (i == 1)
				{

					buff[counter] = {}

				}
				counter++;
			}
			counter++;
		}

	}




}