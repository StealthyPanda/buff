package buff;

public class Cube extends Shell //which is in sphere.java
{

	public double sidelength;

	public Cube(double sidelength)
	{
		this.sidelength = sidelength;
		this.position = new Vector3();
		this.orientation = new Vector3(1, 0, 0);
		populate();
	}

	public Cube(double sidelength, Vector3 position)
	{
		this.sidelength = sidelength;
		this.position = position;
		this.orientation = new Vector3(1, 0, 0);
		populate();
	}

	public Cube(double sidelength, Vector3 position, Vector3 orientation)
	{
		this.sidelength = sidelength;
		this.position = position;
		this.orientation = orientation;
		populate();
	}

	/*void populate()
	{
		vertices = new Vector3[8];

		Vector3 xaxis = new Vector3(1, 0, 0);
		Vector3 yaxis = new Vector3(0, 1, 0);
		Vector3 zaxis = new Vector3(0, 0, 1);
		System.out.println((Math.pow(3, 0.5) * sidelength)/2);
		Vector3 pointer = new Vector3(1, 0, 0).multiply((Math.pow(3, 0.5) * sidelength)/2);
		System.out.println(pointer);
		System.out.println();
		pointer.rotate(zaxis, Math.PI/4);
		pointer.rotate(yaxis.multiply(-1).rotate(new Vector3(0, 0, 1), Math.PI/4), Math.PI/4);
		System.out.println(pointer);
		System.out.println();


		vertices[0] = new Vector3(pointer);

		for (int i  = 1; i < 4 ; i++) 
		{
			pointer.rotate(xaxis, Math.PI/2);
			vertices[i] = new Vector3(pointer);
		}

		pointer.rotate(zaxis, Math.PI/2);
		vertices[4] = new Vector3(pointer);

		for (int i  = 5; i < 8 ; i++) 
		{
			pointer.rotate(xaxis, Math.PI/2);
			vertices[i] = new Vector3(pointer);
		}
	}*/

	void populate()
	{

		vertices = new Vector3[8];

		int i = 0;

		for (int bx = -1; bx <= 1; bx += 2) 
		{
			for (int by = -1; by <= 1; by += 2) 
			{
				for (int bz = -1; bz <= 1; bz += 2) 
				{
					vertices[i] = new Vector3(bx, by, bz).multiply(sidelength/2); i++;
				}				
			}
		}

	}

}