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

	public double getMagnitude()
	{
		double mag = 0;
		mag += (w*w) + (imaginarypart.x*imaginarypart.x) + (imaginarypart.y*imaginarypart.y) + (imaginarypart.z*imaginarypart.z);
		mag = Math.pow(mag, 0.5);
		return mag;
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

	public void normalise()
	{
		this.multiply(1/this.getMagnitude());
	}

	public Quaternion getNormalised()
	{
		return new Quaternion(this).multiply(1/this.getMagnitude());
	}

	public Quaternion getConjugate()
	{
		return new Quaternion(this.w, this.imaginarypart.getMultiplied(-1));
	}

	public Vector3 getVector()
	{
		return new Vector3(this.imaginarypart);
	}

	public static double mod(double val)
	{
		if (val < 0) return -1*val;
		return val;
	}

	/*public Quaternion getClean()
	{
		Quaternion retter = new Quaternion(this);
		if (mod(retter.w) <= Math.pow(10, -15)) retter.w = 0;
		if (mod(retter.imaginarypart.x) <= Math.pow(10, -15)) retter.imaginarypart.x = 0;
		if (mod(retter.imaginarypart.y)<= Math.pow(10, -15)) retter.imaginarypart.y = 0;
		if (mod(retter.imaginarypart.z) <= Math.pow(10, -15)) retter.imaginarypart.z = 0;
		return retter;
	}*/

	public Quaternion clean()
	{
		this.w = (float) this.w;
		this.imaginarypart.x = (float) this.imaginarypart.x;
		this.imaginarypart.y = (float) this.imaginarypart.y;
		this.imaginarypart.z = (float) this.imaginarypart.z;
		if (mod(this.w) <= Math.pow(10, -10)) this.w = 0;
		if (mod(this.imaginarypart.x) <= Math.pow(10, -10)) this.imaginarypart.x = 0;
		if (mod(this.imaginarypart.y) <= Math.pow(10, -10)) this.imaginarypart.y = 0;
		if (mod(this.imaginarypart.z) <= Math.pow(10, -10)) this.imaginarypart.z = 0;
		return this;
	}
	public Quaternion getClean()
	{
		return new Quaternion(this).clean();
	}

	public String toString()
	{
		String retter = "";
		retter += "(" + this.w + ", " + this.imaginarypart.x + ", " + this.imaginarypart.y + ", " + this.imaginarypart.z + ")";
		return retter;
	}

	public static Vector3 rotate(Vector3 point, Vector3 axis, double angleinradians)
	{
		Vector3 rotated;
		angleinradians = angleinradians/2;
		Quaternion rotor = new Quaternion(Math.cos(angleinradians), axis.getNormalised().getMultiplied(Math.sin(angleinradians)));

		rotated = Quaternion.multiply(Quaternion.multiply(rotor, new Quaternion(point)), rotor.getConjugate()).getVector();
		return rotated;
	}

	public Quaternion getInverse()
	{
		return this.getConjugate().multiply(Math.pow(this.getMagnitude(), 2));
	}

	//returns the quaternion rotor that results in the orientation FROM (1, 0, 0) vector;

	public static Quaternion getOrientation(Vector3 pointer)
	{
		return Quaternion.getQuaternion(new Vector3(1, 0, 0), pointer);
	}

	public static Quaternion getRotor(Vector3 axis, double angleinradians)
	{
		return new Quaternion(Math.cos(angleinradians), axis.getNormalised().getMultiplied(Math.sin(angleinradians)));
	}

	//combined in the order rotor1 then rotor2
	public static Quaternion combineRotations(Quaternion rotor1, Quaternion rotor2)
	{
		//Vector3 combinedresult = new Vector3(1, 0, 0).rotate(rotor1).rotate(rotor2);
		return Quaternion.multiply(rotor1, rotor2);
	}

	public Vector3 getVectorOrientation()
	{
		return new Vector3(1, 0, 0).rotate(this);
	}


	//IMPORTANT: MAKE SURE the vectors are at origin;
	public static Quaternion getQuaternion(Vector3 currorientation, Vector3 finorientation)
	{

		Vector3 delta = Vector3.add(finorientation, currorientation.getMultiplied(-1));
		Vector3 axis = Vector3.crossproduct(delta, currorientation.getMultiplied(-1));

		double angle = Math.acos((Vector3.dotproduct(finorientation, currorientation))/(finorientation.getMagnitude() * currorientation.getMagnitude()));
		angle = angle/2;
		Quaternion rotor = new Quaternion(Math.cos(angle), axis.getNormalised().getMultiplied(Math.sin(angle)));

		return rotor;
	}

	public static Vector3 rotate(Vector3 point, Quaternion rotor)
	{
		return Quaternion.multiply(Quaternion.multiply(rotor, new Quaternion(point)), rotor.getConjugate()).getVector();
	}

}