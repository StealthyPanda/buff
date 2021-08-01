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
		double lambda = (Vector3.dotproduct(p.position, p.normal) - Vector3.dotproduct(start, p.normal))/(Vector3.dotproduct(direction, p.normal));
		Vector3 pointofintersection = Vector3.add(start, direction.getMultiplied(lambda));

		if (lambda >= 0)
		{
			if (p.bounded)
			{

				double calcarea = 0;

				calcarea += Vector3.crossproduct( Vector3.add(p.vertices[0], pointofintersection.getMultiplied(-1)) , Vector3.add(p.vertices[1], pointofintersection.getMultiplied(-1)) ).getMagnitude()/2;
				calcarea += Vector3.crossproduct( Vector3.add(p.vertices[1], pointofintersection.getMultiplied(-1)) , Vector3.add(p.vertices[2], pointofintersection.getMultiplied(-1)) ).getMagnitude()/2;

				if (p.vertices.length == 3)
				{
					calcarea += Vector3.crossproduct( Vector3.add(p.vertices[2], pointofintersection.getMultiplied(-1)) , Vector3.add(p.vertices[0], pointofintersection.getMultiplied(-1)) ).getMagnitude()/2;
				}
				else
				{
					calcarea += Vector3.crossproduct( Vector3.add(p.vertices[2], pointofintersection.getMultiplied(-1)) , Vector3.add(p.vertices[3], pointofintersection.getMultiplied(-1)) ).getMagnitude()/2;
					calcarea += Vector3.crossproduct( Vector3.add(p.vertices[3], pointofintersection.getMultiplied(-1)) , Vector3.add(p.vertices[0], pointofintersection.getMultiplied(-1)) ).getMagnitude()/2;
				}

				
				if (Vector3.mod(calcarea - p.getArea()) < Math.pow(10, -10))
				{
					return pointofintersection;
				}
				else
				{
					return null;
				}
			}
			return pointofintersection;
		}
		else
		{
			return null;
		}
	}
}