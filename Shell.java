package buff;

public class Shell
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
