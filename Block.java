package buff;

public class Block
{

	public double[][] projectedvertexcoordinates;
	public Material material;
	public double depth;
	public double normalangle;

	public Block(double[][] projectedvertexcoordinates, Material material, double depth, double normalangle)
	{
		this.projectedvertexcoordinates = projectedvertexcoordinates;
		this.material = material;
		this.depth = depth;
		this.normalangle = normalangle;
	}

}

