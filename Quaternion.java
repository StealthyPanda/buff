package buff;


public class Quaternion
{

	public volatile double w;
	public volatile Vector3 imaginarypart;

	public Quaternion()
	{

		w = 0;
		imaginarypart = new Vector3(1, 0, 0);

	}

	public Quaternion(Quaternion q)
	{
		this.w= q.w;
		this.imaginarypart = q.imaginarypart;
	}

	public Quaternion(double w, double x, double y, double z)
	{

		this.w = w;
		imaginarypart = new Vector3(x, y, z);

	}

	public Quaternion(Vector3 imaginarypart)
	{

		this.w = 0;
		this.imaginarypart = imaginarypart;

	}

	public Quaternion(double realpart, Vector3 imaginarypart)
	{

		this.w = realpart;
		this.imaginarypart = imaginarypart;

	}

	public static Quaternion add(Quaternion q1, Quaternion q2)
	{
		return new Quaternion(q1.w + q2.w, Vector3.add(q1.imaginarypart, q2.imaginarypart));
	}

	public Quaternion multiply(double val)
	{
		this.w *= val;
		this.imaginarypart.multiply(val);
		return this;
	}

	public Quaternion getMultiplied(double val)
	{
		return new Quaternion(this).multiply(val);
	}

	public static Quaternion multiply(Quaternion q1, Quaternion q2)
	{

		Quaternion pureconst = new Quaternion(q1.w * q2.w, 0, 0, 0);
		Quaternion w1vect = new Quaternion(q2.imaginarypart.getMultiplied(q1.w));
		Quaternion w2vect = new Quaternion(q1.imaginarypart.getMultiplied(q2.w));
		Quaternion vectproduct = new Quaternion(Vector3.dotproduct(q1.imaginarypart, q2.imaginarypart) * -1, Vector3.crossproduct(q1.imaginarypart, q2.imaginarypart));
		
		Quaternion finalproduct = Quaternion.add( Quaternion.add( Quaternion.add(pureconst, w1vect), w2vect ), vectproduct );

		return finalproduct;
		
	}

	public String toString()
	{
		String retter = "";
		retter += "(" + this.w + ", " + this.imaginarypart.x + ", " + this.imaginarypart.y + ", " + this.imaginarypart.z + ")";
		return retter;
	}

}