package buff;

public class Sphere extends Shell
{

	public double radius;
	public int latitudes, longitudes;

	
	public Sphere(double radius, int latitudes, int longitudes)
	{
		this.position = new Vector3();
		this.radius = radius;
		this.orientation = new Quaternion(1, 0, 0, 0);
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		populateVertices();
	}

	public Sphere(double radius, int latitudes, int longitudes, Vector3 position)
	{
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		this.position = position;
		this.radius = radius;
		this.orientation = new Quaternion(1, 0, 0, 0);
		populateVertices();
	}

	public Sphere(double radius, int latitudes, int longitudes, Vector3 position, Quaternion orientation)
	{
		this.latitudes = latitudes;
		this.longitudes = longitudes;
		this.position = position;
		this.radius = radius;
		this.orientation = orientation;
		populateVertices();
	}

	//todo: fix this
	void populateVertices()
	{
		Vector3 yaxis = new Vector3(0, 1, 0);
		Vector3 zaxis = new Vector3(0, 0, 1);



		Vector3 pointer = new Vector3(0, 0, this.radius);
		//buffer[0][0] = new Vector3(pointer); //north pole
		//buffer[this.latitudes] = new Vector3(pointer.getMultiplied(-1)); //south pole
		Vector3 northpole = new Vector3(pointer);
		Vector3 southpole = new Vector3(pointer.getMultiplied(-1));

		double yincrements = Math.PI/(this.latitudes + 1);
		double zincrements = (2 * Math.PI)/(this.longitudes);


		Vector3[][] buffer = new Vector3[this.latitudes + 2][this.longitudes];
		Vector3[] tempuno = {northpole}; buffer[0] = tempuno;
		Vector3[] tempdos = {southpole}; buffer[this.latitudes + 1] = tempdos;

		pointer.rotate(yaxis, yincrements);
		for (int i = 1 ; i < this.latitudes + 1; i++) 
		{
			
			for (int j = 0; j < this.longitudes; j++ ) 
			{
				
				buffer[i][j] = new Vector3(pointer);
				pointer.rotate(zaxis, zincrements);

			}

			pointer.rotate(yaxis, yincrements);

		}

		//this.vertices = buffer;
		Vector3[] vertexbuffer = new Vector3[(this.latitudes * this.longitudes) + 2];
		int[][] mapping = new int[this.latitudes+2][this.longitudes];

		//System.out.println(mapping.length);

		int k = 0;
		for (int i = 0; i < buffer.length; i++) 
		{
			//System.out.println("Current layer's length: " + Integer.toString(buffer[i].length));
			for (int j = 0; j < buffer[i].length; j++) 
			{
				//System.out.println("J val: " + Integer.toString(j));
				vertexbuffer[k] = buffer[i][j];
				mapping[i][j] = k;
				k++;
			}
			//k++;
		}

		//System.out.println(pointer);
		int[][] buff = new int[(this.latitudes + 1) * this.longitudes][];

		int counter = 0;
		for (int i = 1; i < this.latitudes + 1; i++) 
		{
			for (int j = 0; j < this.longitudes; j++) 
			{
				if (i == 1)
				{
					if (j == (this.longitudes-1))
					{
						int[] temp = {mapping[i][j], mapping[0][0], mapping[i][0]}; buff[counter] = temp;
						counter++;
						continue;
					}

					int[] temp = {mapping[i][j], mapping[i][j + 1], mapping[0][0]}; buff[counter] = temp;
					counter++;
					continue;

				}

				if (j == (this.longitudes - 1))
				{
					int[] temp = {mapping[i][j], mapping[i - 1][j], mapping[i][0], mapping[i - 1][0]}; buff[counter] = temp;
					counter++;
					continue;
				}

				int[] temp = {mapping[i][j], mapping[i][j + 1], mapping[i - 1][j], mapping[i-1][j+1]}; buff[counter] = temp;
				counter++;
				continue;
			}
			//counter++;
		}

		this.faces = buff;
		this.vertices = vertexbuffer;

	}




}