package buff;

import java.awt.*;
import java.awt.color.*;
import java.awt.geom.*;

public class RenderedFrame extends Canvas
{

	Frame rawframe;
	Camera camera;
	double magnification = 10;
	double width, height; //of final picture

	public RenderedFrame(Frame rawframe, Camera parentcamera, double width, double height)
	{
		this.rawframe = rawframe;
		this.camera = parentcamera;
		this.width = width;
		this.height = height;
	}



	public void paint(Graphics graphics)
	{

		Graphics2D thiscanvas = (Graphics2D) graphics;


		for (int i = 0; i < rawframe.blocks.length; i++) 
		{
			
			Block currentblock = rawframe.blocks[i];

			double[][] vertexcoordinates = currentblock.projectedvertexcoordinates;

			GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, vertexcoordinates.length);
			for (int j = 0; j < vertexcoordinates.length; j++)
			{
				if (j == 0) 
				{
					polygon.moveTo((vertexcoordinates[0][0] * width * magnification)/(camera.sensorwidth * camera.kvalue), (vertexcoordinates[0][1] * width * magnification)/(camera.sensorwidth * camera.kvalue));
					continue;
				}
				polygon.lineTo((vertexcoordinates[j][0] * width * magnification)/(camera.sensorwidth * camera.kvalue), (vertexcoordinates[j][0] * width * magnification)/(camera.sensorwidth * camera.kvalue));
			}

			polygon.closePath();

			thiscanvas.setPaint(new Color(0, 0, 0));
			thiscanvas.draw(polygon);

			thiscanvas.setPaint(new Color(currentblock.material.r, currentblock.material.g, currentblock.material.b));
			thiscanvas.fill(polygon);
		}

	}

}