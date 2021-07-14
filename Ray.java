package buff;

public class Ray
{

	public Vector3 direction, start;

	public Ray(Vector3 start, Vector3 direction)
	{
		this.start = start;
		this.direction = direction.getNormalised();
	}

	public static Ray getRay(Vector3 from, Vector3 to)
	{
		return new Ray(from, Vector3.add(to, from.getMultiplied(-1)).normalise());
	}

	public Vector3 getIntersection(Plane p)
	{
		double poi = (Vector3.dotproduct(p.position, p.normal) - Vector3.dotproduct(start, p.normal))/(Vector3.dotproduct(direction, p.normal));
		//System.out.println(poi);
		Vector3 finalans = Vector3.add(start, direction.getMultiplied(poi));
		//System.out.println(finalans);
		if (poi >= 0)
		{
			if (p.bounded)
			{
				double area = 0;
				area += Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[0], finalans.getMultiplied(-1)), Vector3.add(p.vertices[1], finalans.getMultiplied(-1))).getMagnitude()/2);
				//System.out.println(Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[0], finalans.getMultiplied(-1)), Vector3.add(p.vertices[1], finalans.getMultiplied(-1))).getMagnitude()/2));
				area += Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[1], finalans.getMultiplied(-1)), Vector3.add(p.vertices[2], finalans.getMultiplied(-1))).getMagnitude()/2);
				if (p.vertices.length == 3) area += Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[2], finalans.getMultiplied(-1)), Vector3.add(p.vertices[0], finalans.getMultiplied(-1))).getMagnitude()/2);
				if (p.vertices.length == 4)
				{
					area += Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[2], finalans.getMultiplied(-1)), Vector3.add(p.vertices[3], finalans.getMultiplied(-1))).getMagnitude()/2);
					area += Vector3.mod(Vector3.crossproduct(Vector3.add(p.vertices[3], finalans.getMultiplied(-1)), Vector3.add(p.vertices[0], finalans.getMultiplied(-1))).getMagnitude()/2);
				}
				//System.out.print("Big brian: ");
				//System.out.println(p.getArea());
				//System.out.println(area);
				//System.out.println(Vector3.mod(area - p.getArea()));
				if (Vector3.mod(area - p.getArea()) < Math.pow(10, -10))
				{
					return finalans;
				}
				else
				{
					return null;
				}
			}
			return finalans;
		}
		//System.out.println("Were here boi");
		return null;
	}

}