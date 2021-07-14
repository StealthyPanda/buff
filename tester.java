import java.util.*;
import buff.*;

class testyboi
{
	public static void main(String[] args)
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
		
*/
	}

	
	
	
}