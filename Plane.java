package buff;

public class Plane
{

	double threshhold = Math.pow(10, -7);

	public Vector3[] vertices;
	public Vector3 position, normal;
	public int[][] edges;
	public boolean bounded = true;
	public double area;
	public Material material;


	public Plane(Vector3 a, Vector3 b, Vector3 c)
	{
		Vector3[] buff = {a, b, c}; this.vertices = buff;
		position = Vector3.add(Vector3.add(a, b), c).getMultiplied(1/3);
		int[][] buffedges = {{0,1}, {1,2}, {2,0}}; this.edges = buffedges;
		normal = getNormal();
		material = new Material();
	}

	//NOTE: a,b,c,d must in a cyclic order other plane will be very fuqy
	public Plane(Vector3 a, Vector3 b, Vector3 c, Vector3 d) throws NotCoplanarException
	{
		double box = Vector3.boxProduct(Vector3.add(b, a.getMultiplied(-1)), Vector3.add(c, a.getMultiplied(-1)), Vector3.add(d, a.getMultiplied(-1)));
		//System.out.println(box);
		
		if (Vector3.mod(box) > threshhold)
		{
			throw new NotCoplanarException("Given points are not coplanar and cannot form a plane.");
		}
		else
		{
			Vector3[] buff = {a, b, c, d}; this.vertices = buff;
			position = Vector3.add(Vector3.add(a, b), Vector3.add(c, d)).getMultiplied(1/4);
			//System.out.println("Idhar: " + position.toString());
			int[][] buffedges = {{0,1}, {1,2}, {2,3}, {3,1}}; this.edges = buffedges;
			normal = getNormal();
		}
		material = new Material();
	}

	public Plane(Vector3 position, Vector3 normal)
	{
		this.position = position;
		this.normal = normal;
		this.bounded = false;
	}


	//returns normalised normal;
	public Vector3 getNormal()
	{
		Vector3 bminusa = Vector3.add(vertices[1], vertices[0].getMultiplied(-1));
		Vector3 cminusa = Vector3.add(vertices[2], vertices[0].getMultiplied(-1));
		return Vector3.crossproduct(bminusa, cminusa).normalise();
	}

	public double getArea()
	{
		double areabuff = 0;

		areabuff = Vector3.crossproduct( Vector3.add(vertices[1], vertices[0].getMultiplied(-1)) , Vector3.add(vertices[2], vertices[0].getMultiplied(-1)) ).getMagnitude()/2;

		if (vertices.length == 4)
		{
			areabuff += Vector3.crossproduct( Vector3.add(vertices[2], vertices[0].getMultiplied(-1)) , Vector3.add(vertices[3], vertices[0].getMultiplied(-1)) ).getMagnitude()/2;
		}

		return areabuff;
	}

	public Plane getUnbounded()
	{
		return new Plane(this.position, this.normal);
	}

	public Plane setMaterial(Material material)
	{
		this.material = material;
		return this;
	}

	public Vector3 calculatePosition()
	{
		Vector3 newposition = new Vector3();

		for (int i = 0; i < vertices.length; i++)
		{
			newposition = Vector3.add(newposition, vertices[i]);
		}

		return newposition.divide(vertices.length);
	}

	public void translate(Vector3 movement)
	{
		for (int i = 0; i < vertices.length; i++)
		{
			vertices[i] = Vector3.add(vertices[i], movement);
		}
		//position = Vector3.add(Vector3.add(vertices[0], vertices[1]), Vector3.add(vertices[2], vertices[3])).getMultiplied(1/4);
		this.position = calculatePosition();
		//System.out.println("Inside out: " + position.toString());
	}

	public void translateTo(Vector3 newposition)
	{
		this.position = calculatePosition();
		//System.out.println("current position = " + this.position.toString());
		Vector3 movement = Vector3.add(newposition, this.position.getMultiplied(-1));
		//System.out.println("movement = " + movement.toString());
		translate(movement);
	}

	public void rotateLocal(Vector3 axis, double angleinradians)
	{
		//calculatePosition();
		Vector3 resettoorigin = this.position.getMultiplied(-1);
		translate(resettoorigin);
		for (int i = 0; i < vertices.length; i++) 
		{
			vertices[i] = Quaternion.rotate(vertices[i], axis, angleinradians);
		}
		this.normal = getNormal();
		translate(resettoorigin.getMultiplied(-1));
	}

	public void rotateLocal(Quaternion rotor)
	{
		//calculatePosition();
		Vector3 resettoorigin = this.position.getMultiplied(-1);
		translate(resettoorigin);
		for (int i = 0; i < vertices.length; i++) 
		{
			vertices[i] = Quaternion.rotate(vertices[i], rotor);
		}
		this.normal = getNormal();
		translate(resettoorigin.getMultiplied(-1));
	}

	public void rotateAbout(Vector3 pivot, Vector3 axis, double angleinradians)
	{
		
		for (int i = 0; i < vertices.length; i++ ) 
		{
			vertices[i] = Quaternion.rotateAbout(vertices[i], pivot, axis, angleinradians);
		}
		this.position = calculatePosition();
		this.normal = getNormal();

	}

	public void rotateAbout(Vector3 pivot, Quaternion rotor)
	{
		
		for (int i = 0; i < vertices.length; i++ ) 
		{
			vertices[i] = Quaternion.rotateAbout(vertices[i], pivot, rotor);
		}
		this.position = calculatePosition();
		this.normal = getNormal();

	}

	

	//public void rotateTo(Vector3)
}