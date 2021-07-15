import java.util.*;
import buff.*;

class testyboi
{
	/*public static void main(String[] args)
	{
		
		World world = new World();

		Camera thecam = world.camera;


		System.out.println("Position: " + world.camera.position.getClean().toString());
		System.out.println("Orientation: " + world.camera.orientation.getClean().toString());
		System.out.println("Orientation vector: " + world.camera.orientation.getVectorOrientation().getClean().toString());
		System.out.println("Sensor postion: " + world.camera.sensor.position.getClean().toString());
		System.out.println("Sensor distance: " + Vector3.add(world.camera.sensor.position, world.camera.position.multiply(-1)).getClean().toString());
		System.out.println("Sensor orientation: " + world.camera.sensor.normal.getClean().toString());
		System.out.println("Sensor orientation vector: " + world.camera.sensor.normal.getClean().toString());
		System.out.println();

		thecam.translate(new Vector3(2, 0, 0));
		thecam.rotateLocal(new Vector3(0, 0, 1), Math.PI/2);


		System.out.println("Position: " + world.camera.position.getClean().toString());
		System.out.println("Orientation: " + world.camera.orientation.getClean().toString());
		System.out.println("Orientation vector: " + world.camera.orientation.getVectorOrientation().getClean().toString());
		System.out.println("Sensor postion: " + world.camera.sensor.position.getClean().toString());
		System.out.println("Sensor distance: " + Vector3.add(world.camera.sensor.position, world.camera.position.multiply(-1)).getClean().toString());
		System.out.println("Sensor orientation: " + world.camera.sensor.normal.getClean().toString());
		System.out.println("Sensor orientation vector: " + world.camera.sensor.normal.getClean().toString());
		System.out.println();

		thecam.translate(new Vector3(2, 0, 0));
		thecam.rotateLocal(new Vector3(0, 0, 1), Math.PI/2);


		System.out.println("Position: " + world.camera.position.getClean().toString());
		System.out.println("Orientation: " + world.camera.orientation.getClean().toString());
		System.out.println("Orientation vector: " + world.camera.orientation.getVectorOrientation().getClean().toString());
		System.out.println("Sensor postion: " + world.camera.sensor.position.getClean().toString());
		System.out.println("Sensor distance: " + Vector3.add(world.camera.sensor.position, world.camera.position.multiply(-1)).getClean().toString());
		System.out.println("Sensor orientation: " + world.camera.sensor.normal.getClean().toString());
		System.out.println("Sensor orientation vector: " + world.camera.sensor.normal.getClean().toString());
		System.out.println();

		/*for(int i = 0; i < 4; i++)
		{
			System.out.println(world.camera.sensor.vertices[i].getClean());
		}
		

	}*/

	//Cube cube;

	public static void main(String[] args) 
	{
		
		Cube cube = new Cube(2);

		cube.material = new Material();

		cube.translate(new Vector3(14, -2, 0));
		cube.rotateLocal(new Vector3(0, 0, 1), Math.PI/4);

		World world = new World();

		Cube[] buff = {cube}; world.rendergroup = buff;

		//world.camera.rotateLocal(new Vector3(0, -1, 0), Math.toRadians(1.5));
		world.camera.translate(new Vector3(0, 0, 0.03));
		world.camera.captureFrame();

		rotater rot = new rotater(cube, world.camera);
		rot.start();
		//Plane plane = null;
		//try { plane = new Plane(new Vector3(4, 0, 0), new Vector3(4, 0, 1), new Vector3(4, 1, 1), new Vector3(4, 1, 0)); }
		//catch (NotCoplanarException e) {System.out.println("Surprise");}

		//world.camera.projectOnFrame(plane);

		//System.out.println(world.camera.frame.blocks[0].projectedvertexcoordinates.length);
		//System.out.println(world.camera.frame.blocks[1].projectedvertexcoordinates.length);

	}



	
	
	
}

class rotater extends Thread
{
	Cube cube;
	Camera camera;

	public rotater(Cube cube, Camera camera)
	{
		this.cube = cube; this.camera = camera;
	}

	public void run()
	{

		while (true)
		{
			try {Thread.sleep(10);} catch (Exception e) {}
			this.cube.rotateLocal(new Vector3(1, 1, 1), Math.toRadians(1));
			//System.out.println(this.cube.orientation.getVectorOrientation());
			camera.recapture();
		}

	}
}