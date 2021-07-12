package buff;

public class Camera
{
	public volatile Vector3 position;
	public volatile Quaternion orientation;

	public Camera()
	{
		this.position = new Vector3(0, 0, 0);
		this.orientation = new Quaternion(1, 0, 0, 0);
	}

	public Camera(Vector3 position)
	{
		this.position = position;
		this.orientation = new Quaternion(1, 0, 0, 0);
	}

	public Camera(Vector3 position, Quaternion orientation)
	{
		this.position = position;
		this.orientation = orientation;
	}

	
}