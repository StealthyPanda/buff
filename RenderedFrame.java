package buff;

import java.awt.*;
import java.awt.color.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class RenderedFrame extends Canvas
{

	Frame rawframe;
	Camera camera;
	double magnification = 100;
	public int width, height; //of final picture
	String renderpath;

	public RenderedFrame(Frame rawframe, Camera parentcamera, int width, int height)
	{
		this.rawframe = rawframe;
		this.camera = parentcamera;
		this.width = width;
		this.height = height;
		this.renderpath = camera.renderpath;
	}

	public void update()
	{

		this.rawframe = camera.frame;
		this.renderpath = camera.renderpath;
		//repaint();

	}



	public void paint(Graphics graphics)
	{
		update();

		Graphics2D thiscanvas = (Graphics2D) graphics;

		setBackground(new Color(89, 236, 255));


		for (int i = 0; i < rawframe.blocks.length; i++) 
		{
			Block currentblock = rawframe.blocks[i];

			double[][] vertexcoordinates = currentblock.projectedvertexcoordinates;

			GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, vertexcoordinates.length);
			for (int j = 0; j < vertexcoordinates.length; j++)
			{
				if (j == 0) 
				{
					polygon.moveTo(Math.round((vertexcoordinates[0][0]/(camera.sensorwidth * camera.kvalue) * width * magnification)), Math.round((vertexcoordinates[0][1]/(camera.sensorheight * camera.kvalue) * height * magnification)));
					continue;
				}
				polygon.lineTo(Math.round((vertexcoordinates[j][0]/(camera.sensorwidth * camera.kvalue) * width * magnification)), Math.round((vertexcoordinates[j][1]/(camera.sensorheight * camera.kvalue) * height * magnification)));
			}

			polygon.closePath();

			thiscanvas.setPaint(new Color(0, 0, 0));
			thiscanvas.draw(polygon);

			thiscanvas.setPaint(new Color(currentblock.material.r, currentblock.material.g, currentblock.material.b));
			thiscanvas.fill(polygon);
		}

	}

	public void saveFrame(String imageName)
	{
		BufferedImage image = new  BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = image.createGraphics();
	    paint(graphics);
	    //graphics.dispose();
	    FileOutputStream out;
	    try {
	        //System.out.println("Exporting image: "+imageName);
	        out = new FileOutputStream(imageName);
	        ImageIO.write(image, "png", out);
	        out.close();
	    } 
	    catch (Exception e)
	    {

	    	System.out.println("Something wrong in RenderedFrame.java in saving function.");
	    	e.printStackTrace();

	    }
	}

}