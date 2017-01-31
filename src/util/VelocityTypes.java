package util;

import java.util.HashMap;

import javax.vecmath.Vector3d;

import entities.ParticleSystem;
import entitytypes.ParticleSystemType.e_VelocityType;

public class VelocityTypes {
	
	public static HashMap<e_VelocityType, IVelocityType> TYPES = new HashMap<e_VelocityType, IVelocityType>();
	
	public interface IVelocityType {		
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir);
	}
	
	public static IVelocityType VEL_NONE = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			return new Vector3d(0.0, 0.0, 0.0);
		}		
	};
	
	public static IVelocityType VEL_ORTHO = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			float x = MathUtil.getRandomFloat(sys.type.VelOrthoX);
			float y = MathUtil.getRandomFloat(sys.type.VelOrthoY);
			float z = MathUtil.getRandomFloat(sys.type.VelOrthoZ);				
			return new Vector3d(x, y, z);
		}		
	};
	
	public static IVelocityType VEL_OUTWARD = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			Vector3d velocity = new Vector3d(0.0, 0.0, 0.0);
			switch (sys.type.VolumeType) {
			case CYLINDER:
				velocity = new Vector3d(dir);
				velocity.scale(MathUtil.getRandomFloat(sys.type.VelOutward));
				velocity.add(new Vector3d(0.0, 0.0, MathUtil.getRandomFloat(sys.type.VelOutwardOther)));
				break;
			case LINE:
				Vector3d dir1 = new Vector3d();
				Vector3d dir2 = new Vector3d();
				Vector3d up = new Vector3d(0.0, 0.0, 1.0);
				if (dir.dot(up) <= 0.01) {
					up = new Vector3d(0.0, 1.0, 0.0);
				}			
				dir1.cross(dir, up);
				dir2.cross(dir1, dir);				
				dir1.scale(MathUtil.getRandomFloat(sys.type.VelOutward));
				dir2.scale(MathUtil.getRandomFloat(sys.type.VelOutwardOther));
				velocity.add(dir1);
				velocity.add(dir2);
				break;
			case POINT:
				velocity = MathUtil.randomDir();
				break;
			case BOX:
			case SPHERE:
			default:
				velocity = new Vector3d(dir);
				velocity.scale(MathUtil.getRandomFloat(sys.type.VelOutward));
				break;			
			}
			return velocity;
		}		
	};
	
	public static IVelocityType VEL_SPHERICAL = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			Vector3d velocity = MathUtil.randomDir();
			velocity.scale(MathUtil.getRandomFloat(sys.type.VelSpherical));
			return velocity;
		}		
	};
	
	public static IVelocityType VEL_HEMISPHERICAL = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			Vector3d velocity = MathUtil.randomDir();
			velocity.z = Math.abs(velocity.z);
			velocity.scale(MathUtil.getRandomFloat(sys.type.VelHemispherical));
			//System.out.println("VelHemi_Length="+velocity.length());
			return velocity;		
		}		
	};
	
	public static IVelocityType VEL_CYLINDRICAL = new IVelocityType(){
		@Override
		public Vector3d getVelocity(ParticleSystem sys, Vector3d dir) {
			Vector3d velocity = new Vector3d(MathUtil.getRandomFloat(sys.type.VelCylindricalRadial), 0.0, 0.0);
			MathUtil.rotateVectorZ(velocity, MathUtil.randomAngleRad());
			velocity.z = MathUtil.getRandomFloat(sys.type.VelCylindricalNormal);
			return velocity;		
		}		
	};
	
	static {
		TYPES.put(e_VelocityType.NONE, VEL_NONE);
		TYPES.put(e_VelocityType.CYLINDRICAL, VEL_CYLINDRICAL);
		TYPES.put(e_VelocityType.HEMISPHERICAL, VEL_HEMISPHERICAL);
		TYPES.put(e_VelocityType.ORTHO, VEL_ORTHO);
		TYPES.put(e_VelocityType.OUTWARD, VEL_OUTWARD);
		TYPES.put(e_VelocityType.SPHERICAL, VEL_SPHERICAL);
	}
}
