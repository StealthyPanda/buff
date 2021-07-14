package buff;

abstract class Vector
{

	public double x, y, z;

	abstract public double getMagnitude();
	//abstract public double dotwith(Vector3 v);
	//abstract public Vector3 crosswith(Vector3 v);


}


public class Vector3 extends Vector
{
	public static Vector3 xaxis, yaxis, zaxis;
	//only initialised if getmag below called at least once;
	public double magnitude;

	static void initaxes()
	{
		xaxis = new Vector3(1, 0, 0);
		yaxis = new Vector3(0, 1, 0);
		zaxis = new Vector3(0, 0, 1);
	}

	public String toString()
	{
		String retter = "";
		retter += "(" + this.x + ", " + this.y + ", " + this.z + ")";
		return retter;
	}

	public Vector3()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	public Vector3(double val)
	{
		this.x = val;
		this.y = val;
		this.z = val;
	}
	public Vector3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3(Vector3 v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public Vector3 multiply(double factor)
	{
		this.x = this.x * factor;
		this.y = this.y * factor;
		this.z = this.z * factor;
		return this;
	}

	public Vector3 divide(double factor)
	{
		factor = Math.pow(factor, -1);
		this.x = this.x * factor;
		this.y = this.y * factor;
		this.z = this.z * factor;
		return this;
	}

	public Vector3 getMultiplied(double factor)
	{
		return new Vector3(this).multiply(factor);
	}

	public Vector3 getDivided(double factor)
	{
		factor = Math.pow(factor, -1);
		return new Vector3(this).multiply(factor);
	}

	public double getMagnitude()
	{
		double mag = 0;
		mag += (x*x) + (y*y) + (z*z);
		mag = Math.pow(mag, 0.5);
		return mag;
	}

	public Vector3 normalise()
	{
		this.divide(this.getMagnitude());
		return this;
	}

	public Vector3 getNormalised()
	{
		Vector3 normalised = new Vector3(this); normalised.divide(normalised.getMagnitude());
		return normalised;
	}

	public double dotwith(Vector3 v)
	{
		double mag = 0;
		mag += (v.x * this.x) + (v.y * this.y) + (v.z * this.z);
		//mag = Math.Pow(mag, 0.5);
		return mag;
	}

	public static double dotproduct(Vector3 v1, Vector3 v2)
	{
		double mag = 0;
		mag += (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
		//mag = Math.Pow(mag, 0.5);
		return mag;
	}

	public static Vector3 crossproduct(Vector3 v1, Vector3 v2)
	{
		return new Vector3((v1.y * v2.z) - (v2.y * v1.z), (v2.x * v1.z) - (v1.x * v2.z), (v1.x * v2.y) - (v2.x * v1.y));
	}

	public static Vector3 add(Vector3 v1, Vector3 v2)
	{
		Vector3 sum = new Vector3();
		sum.x = v1.x + v2.x;
		sum.y = v1.y + v2.y;
		sum.z = v1.z + v2.z;
		return sum;
	}

	public boolean equals(Vector3 v)
	{
		return ((this.x == v.x) && (this.y == v.y) && (this.z == v.z));
	}

	public static double mod(double val)
	{
		if (val < 0) return -1*val;
		return val;
	}

	/*public Vector3 getClean()
	{
		Vector3 retter = new Vector3(this);
		//if (retter.w <= Math.pow(10, -15)) retter.w = 0;
		if (mod(retter.x) <= Math.pow(10, -15)) retter.x = 0;
		if (mod(retter.y) <= Math.pow(10, -15)) retter.y = 0;
		if (mod(retter.z) <= Math.pow(10, -15)) retter.z = 0;
		return retter;
	}*/

	public Vector3 clean()
	{
		double threshhold = Math.pow(10, -7);
		this.x = (float) this.x;
		this.y = (float) this.y;
		this.z = (float) this.z;
		if (mod(this.x) <= threshhold) this.x = 0;
		if (mod(this.y) <= threshhold) this.y = 0;
		if (mod(this.z) <= threshhold) this.z = 0;
		return this;
	}

	public Vector3 getClean()
	{
		return new Vector3(this).clean();
	}

	public Vector3 rotate(Vector3 axis, double angleinradians)
	{
		Vector3 rotated = Quaternion.rotate(this, axis, angleinradians);
		this.x = rotated.x;
		this.y = rotated.y;
		this.z = rotated.z;
		return this;
	}

	public Vector3 rotate(Quaternion rotor)
	{
		Vector3 rotated = Quaternion.rotate(this, rotor);
		this.x = rotated.x;
		this.y = rotated.y;
		this.z = rotated.z;
		return this;
	}

	public Vector3 rotateAbout(Vector3 pivot, Vector3 axis, double angleinradians)
	{

		Vector3 rotated = Quaternion.rotateAbout(this, pivot, axis, angleinradians);
		this.x = rotated.x;
		this.y = rotated.y;
		this.z = rotated.z;
		return this;

	}

	public Vector3 rotateAbout(Vector3 pivot, Quaternion rotor)
	{

		Vector3 rotated = Quaternion.rotateAbout(this, pivot, rotor);
		this.x = rotated.x;
		this.y = rotated.y;
		this.z = rotated.z;
		return this;

	}

	public static double boxProduct(Vector3 a, Vector3 b, Vector3 c)
	{
		return Vector3.dotproduct(Vector3.crossproduct(a, b), c);
	}

	public static double angle(Vector3 v1, Vector3 v2)
	{
		return Vector3.dotproduct(v1, v2)/(v1.getMagnitude() * v2.getMagnitude());
	}
}