package buff;


public class Vector2 extends Vector3
{
	public Vector2()
	{
		super();
		this.z = 0;
	}

	public Vector2(double val)
	{
		super(val);
		this.z = 0;
	}

	public Vector2(double x, double y)
	{
		super(x, y, 0);
	}

	public static Vector3 crossproduct(Vector2 v1, Vector2 v2)
	{
		return Vector3.crossproduct((Vector3) v1, (Vector3) v2);
	}

	public boolean equals(Vector2 v)
	{
		return ((this.x == v.x) && (this.y == v.y));
	}
}

