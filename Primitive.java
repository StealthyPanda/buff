package buff;

public class Primitive
{
	public volatile Vector3 position;
	public volatile Vector3 netforce, velocity;//somehow implement orientation
	public volatile double mass;

	public Primitive()
	{
		this.position = new Vector3();
		this.velocity = new Vector3();
		this.netforce = new Vector3();
	}

	public Primitive(Vector3 position)
	{
		this.position = position;
		this.velocity = new Vector3();
		this.netforce = new Vector3();
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