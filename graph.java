import java.util.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.ImageIO;

class graph
{
	Canvas place;
	public static void main(String[] args) 
	{

		JFrame frame = new JFrame("BRUH");
		Drawer place = new Drawer();
		place.setSize(1600, 900);
		//place.repaint();
		place.savepic("C:\\Seema_Sep_20\\OneDrive\\Desktop\\touseef\\buff\\thismgiht.png");
		//frame.add(new another());
		frame.add(place);
		frame.setSize(500, 500);
		frame.setVisible(true);

		changer change = new changer(place); change.start();

		//increment();
		//place.repaint();
		
	}

	static void increment()
	{
		Drawer.h += 5;
		Drawer.s += 1;
		Drawer.v += 3;
	}
}

class Drawer extends Canvas
{
	volatile static int h = 0, s = 0, v = 0;
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		//setBackground(new Color(0,0,0));
		//g.fillOval(20, 20, 100, 100);
		int[] lexs = {100, 200, 300};
		int[] leys = {100, 500, 400};
		GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, lexs.length);
		polygon.moveTo(lexs[0], leys[0]);

		for (int i = 1; i < lexs.length ; i++) 
		{
			polygon.lineTo(lexs[i], leys[i]);
		}

		polygon.closePath();
		g2.draw(polygon);
		float[] vals = {h, s, v};
		//float lewhy = 1;
		//System.out.println(h);
		g2.setPaint(new Color(h, s, v));
		g2.fill(polygon);
		//mime group
	}

	public void savepic(String imageName)
	{
		BufferedImage image = new  BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = image.createGraphics();
	    paint(graphics);
	    //graphics.dispose();
	    FileOutputStream out;
	    try {
	        System.out.println("Exporting image: "+imageName);
	        out = new FileOutputStream(imageName);
	        ImageIO.write(image, "png", out);
	        out.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	}
}

class changer extends Thread
{
	Canvas ooooi;
	public changer(Canvas c)
	{
		ooooi = c;
	}
	public void run()
	{

		while (true)
		{
			if (Drawer.h >= 250)
			{
				Drawer.h = 0;
			}
			else
			{
				Drawer.h += 5;
			}

			if (Drawer.s >= 250)
			{
				Drawer.s = 0;
			}
			else
			{
				Drawer.s += 1;
			}

			if (Drawer.v >= 250)
			{
				Drawer.v = 0;
			}
			else
			{
				Drawer.v += 3;
			}

			ooooi.repaint();
			//try { Thread.sleep(10); } catch (Exception e) { e.printStackTrace(); }
		}

	}
}