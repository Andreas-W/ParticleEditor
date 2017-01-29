package util;

import java.util.Random;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class MathUtil {
	
	public static final double J3D_COORD_SCALE = 0.01;
	
	public static Random rand = new Random();
	
	public static int getRandomInt(int value1, int value2) {
		if (value1 == value2) return value1;
		int min, max;
		if (value2 >= value1) {
			min = value1;
			max = value2;
		}else {
			min = value2;
			max = value1;
		}
		return (min+rand.nextInt(max-min));
	}
	
	public static int getRandomInt(int[] values) {
		if (values.length >= 2) {
			return getRandomInt(values[0], values[1]);
		}
		return 0;
	}
	
	public static float getRandomFloat(float value1, float value2) {
		if (value1 == value2) return value1;
		float min, max;
		if (value2 >= value1) {
			min = value1;
			max = value2;
		}else {
			min = value2;
			max = value1;
		}
		return (min+rand.nextFloat()*(max-min));
	}
	
	public static float getRandomFloat(float[] values) {
		if (values.length >= 2) {
			return getRandomFloat(values[0], values[1]);
		}
		return 0;
	}
	
	public static void rotateVectorX (Vector3d vec, double angle) {
		rotateVectorCC(vec, new Vector3d(1.0, 0.0, 0.0), angle);
	}
	public static void rotateVectorY (Vector3d vec, double angle) {
		rotateVectorCC(vec, new Vector3d(0.0, 1.0, 0.0), angle);
	}
	public static void rotateVectorZ (Vector3d vec, double angle) {
		rotateVectorCC(vec, new Vector3d(0.0, 0.0, 1.0), angle);
	}
	
	public static void rotateVectorCC(Vector3d vec, Vector3d axis, double theta){
	    double x, y, z;
	    double u, v, w;
	    x=vec.getX();y=vec.getY();z=vec.getZ();
	    u=axis.getX();v=axis.getY();w=axis.getZ();
	    double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(theta)) 
	            + x*Math.cos(theta)
	            + (-w*y + v*z)*Math.sin(theta);
	    double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
	            + y*Math.cos(theta)
	            + (w*x - u*z)*Math.sin(theta);
	    double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
	            + z*Math.cos(theta)
	            + (-v*x + u*y)*Math.sin(theta);
	    vec.x = xPrime;
	    vec.y = yPrime;
	    vec.z = zPrime;
	}
	
	public static Vector3d randomDir()
	{
	    double x, y, z, d2;
	    do {
	        x = rand.nextGaussian();
	        y = rand.nextGaussian();
	        z = rand.nextGaussian();
	        d2 = x*x + y*y + z*z;
	    } while (d2 <= Double.MIN_NORMAL);
	    double s = Math.sqrt(1.0 / d2);
	    return new Vector3d (x*s, y*s, z*s);
	}
	
	public static double randomAngleRad() {
		return Math.PI * 2.0 * rand.nextDouble();
	}

	/**
	 * From Particle (ZH) logic scale to J3D scale
	 * swaps Y and Z coords and multiplies with 1/100;
	 */
	public static Vector3d toJ3DVec(Vector3d vec) {
		return new Vector3d(vec.x * J3D_COORD_SCALE, vec.z * J3D_COORD_SCALE, vec.y * J3D_COORD_SCALE);
	}
}
