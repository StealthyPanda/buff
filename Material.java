package buff;

public class Material
{
	public int r, g, b;
	public Material(int r, int g, int b)
	{

		this.r = r;
		this.g = g;
		this.b = b;

	}

	public Material()
	{
		r = 255; g = 211; b = 89; //light blue as default
								  //changed it to light orange as default as sky is light blue
	}
}