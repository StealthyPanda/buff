package buff;

public class Cube extends Shell //which is in sphere.java
{

	public double sidelength;

	public Cube(double sidelength)
	{
		this.sidelength = sidelength;
		this.position = new Vector3();
		this.orientation = new Quaternion(1, 0, 0, 0);
		populate();
	}

	public Cube(double sidelength, Vector3 position)
	{
		this.sidelength = sidelength;
		this.position = position;
		this.orientation = new Quaternion(1, 0, 0, 0);
		populate();
	}

	public Cube(double sidelength, Vector3 position, Quaternion orientation)
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
					vertices[i] = Vector3.add(new Vector3(bx, by, bz).multiply(sidelength/2).rotate(orientation), this.position); i++;
				}				
			}
		}

		//translate(position);


		/*Plane[] buffer = new Plane[6]; faces = buffer;

		try
		{
			faces[0] = new Plane(vertices[0], vertices[1], vertices[2], vertices[3]);
			faces[1] = new Plane(vertices[2], vertices[3], vertices[7], vertices[6]);
			faces[2] = new Plane(vertices[6], vertices[7], vertices[5], vertices[4]);
			faces[3] = new Plane(vertices[4], vertices[5], vertices[1], vertices[0]);
			faces[4] = new Plane(vertices[1], vertices[5], vertices[7], vertices[3]);
			faces[5] = new Plane(vertices[0], vertices[2], vertices[6], vertices[4]);
		}
		catch (NotCoplanarException e)
		{
			System.out.println("Couldn't generate cube.");
		}*/

		int[][] buff = { {0, 1, 2, 3},
						 {2, 3, 7, 6},
						 {6, 7, 5, 4},
						 {4, 5, 1, 0},
						 {1, 5, 7, 3},
						 {0, 2, 6, 4} }; faces = buff;



	}

}