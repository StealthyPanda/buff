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

	public Quaternion(double w, double x, double y, double z)
	{

		this.w = w;
		imaginarypart = new Vector3(x, y, z);

	}

	public Quaternion(double realpart, Vector3 imaginarypart)
	{

		this.w = w;
		this.imaginarypart = imaginarypart;

	}

	public static Quaternion add(Quaternion q1, Quaternion q2)
	{
		return new Quaternion(q1.w + q2.w, Vector3.add(q1.imaginarypart, q2.imaginarypart));
	}

	public static Quaternion multiply(double val)
	{
		this.w *= val;
		this.imaginarypart.multiply(val);
	}

}