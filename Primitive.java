package buff;

public class Primitive
{
	public volatile Vector3 position;
	public volatile Vector3 netforce, velocity;//somehow implement orientation
	public volatile double mass;
	public volatile Quaternion orientation;

	public Primitive()
	{
		this.position = new Vector3();
		this.velocity = new Vector3();
		this.netforce = new Vector3();
		this.orientation = new Quaternion(1, 0, 0, 0);
	}

	public Primitive(Vector3 position)
	{
		this.position = position;
		this.velocity = new Vector3();
		this.netforce = new Vector3();
		this.orientation = new Quaternion(1, 0, 0, 0);
	}

	public Primitive(Vector3 position, Quaternion orientation)
	{
		this.position = position;
		this.velocity = new Vector3();
		this.netforce = new Vector3();
		this.orientation = orientation;
	}

	public void moveTo(Vector3 destination)
	{
		this.position = destination;
	}

	public void move(Vector3 movement)
	{
		this.position = Vector3.add(this.position, movement);
	}
}