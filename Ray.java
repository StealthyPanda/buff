package buff;

public class Ray
{

	Vector3 direction;

	public Ray(Vector3 direction)
	{
		this.direction = direction.getNormalised();
	}

}