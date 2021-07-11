package buff;

public class Plane
{

	double threshhold = Math.pow(10, -10);

	public Vector3[] vertices;
	public Vector3 position, normal;
	public int[][] edges;
	public boolean bounded = true;

	public Plane(Vector3 a, Vector3 b, Vector3 c)
	{
		Vector3[] buff = {a, b, c}; this.vertices = buff;
		position = Vector3.add(Vector3.add(a, b), c).getMultiplied(1/3);
		int[][] buffedges = {{0,1}, {1,2}, {2,0}}; this.edges = buffedges;
		normal = getNormal();
	}

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
			int[][] buffedges = {{0,1}, {1,2}, {2,3}, {3,1}}; this.edges = buffedges;
			normal = getNormal();
		}
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

	public Plane getUnbounded()
	{
		return new Plane(this.position, this.normal);
	}
}