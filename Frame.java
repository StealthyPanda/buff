package buff;

public class Frame
{
	public Block[] blocks = null;
	public int index = 0;

	public Frame(int numberofblocks)
	{
		Block[] buff = new Block[numberofblocks]; blocks = buff;
	}

	public void addBlock(Block block)
	{
		if (blocks == null) return;
		blocks[index] = block;
		index++;
	}
	
	
}