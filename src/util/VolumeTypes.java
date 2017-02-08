package util;

import java.util.HashMap;

import javax.vecmath.Vector3d;

import entities.ParticleSystem;
import entitytypes.ParticleSystemType.e_VolumeType;

public class VolumeTypes {
	
	public static HashMap<e_VolumeType, IVolumeType> TYPES = new HashMap<e_VolumeType, IVolumeType>();
	
	public interface IVolumeType {
		/**
		 * returns a position offset for the next particle to spawn.
		 * dir is the direction vector which is required for some types (this is output, not input)
		 */
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir);
	}
	
	public static IVolumeType VOL_POINT = new IVolumeType() {

		@Override
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir) {
			return new Vector3d(0.0, 0.0, 0.0);
		}
		
	};
	
	
	public static IVolumeType VOL_CYLINDER = new IVolumeType() {
		@Override
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir) {
			float r;
			float z;
			float angle = MathUtil.rand.nextFloat()*(float)Math.PI*2.0f;
			if (sys.type.IsHollow) {
				r = sys.type.VolCylinderRadius;
			}else {
				r = MathUtil.getRandomFloat(0.0f, sys.type.VolCylinderRadius);
			}
			z = MathUtil.rand.nextFloat()*sys.type.VolCylinderLength;
			dir.x = 1.0;
			dir.y = 0.0;
			dir.z = 0.0;
			
			MathUtil.rotateVectorZ(dir, angle);
			
			Vector3d position = new Vector3d(dir.x*r, dir.y*r, z);
			return position;
		}		
	};
	
	
	public static IVolumeType VOL_SPHERE = new IVolumeType() {
		@Override
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir) {
			float r;
			double angle = MathUtil.rand.nextDouble()*2.0*Math.PI;
			double z = 1.0-(MathUtil.rand.nextDouble()*2.0);
			double a = Math.sqrt(1.0-Math.pow(z, 2));
			
			if (sys.type.IsHollow) {
				r = sys.type.VolSphereRadius;
			}else {
				r = MathUtil.getRandomFloat(0.0f, sys.type.VolSphereRadius);
			}

			dir.x = a*Math.cos(angle);
			dir.y = a*Math.sin(angle);	
			dir.z = z;
			
			Vector3d position = new Vector3d(dir.x*r, dir.y*r, dir.z*r);
			return position;
		}		
	};
	
	
	public static IVolumeType VOL_BOX = new IVolumeType() {

		@Override
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir) {
			float x = MathUtil.getRandomFloat(-sys.type.VolBoxHalfSize.x, sys.type.VolBoxHalfSize.x);
			float y = MathUtil.getRandomFloat(-sys.type.VolBoxHalfSize.y, sys.type.VolBoxHalfSize.y);
			float z = MathUtil.getRandomFloat(0.0f, sys.type.VolBoxHalfSize.z*2.0f);
			Vector3d position = new Vector3d(x, y, z);
			
			dir.x = position.x;
			dir.y = position.y;
			dir.z = position.z;
			dir.normalize();
			
			return position;
		}
		
	};
	
	public static IVolumeType VOL_LINE = new IVolumeType() {

		@Override
		public Vector3d getPosition(ParticleSystem sys, Vector3d dir) {
			float x = MathUtil.getRandomFloat(sys.type.VolLineStart.x, sys.type.VolLineEnd.x);
			float y = MathUtil.getRandomFloat(sys.type.VolLineStart.y, sys.type.VolLineEnd.y);
			float z = MathUtil.getRandomFloat(sys.type.VolLineStart.z, sys.type.VolLineEnd.z);
			Vector3d position = new Vector3d(x, y, z);
			
			Vector3d lineDir = sys.type.VolLineEnd.toVec();
			lineDir.sub(sys.type.VolLineStart.toVec());
			lineDir.normalize();
			dir.x = lineDir.x;
			dir.y = lineDir.y;
			dir.z = lineDir.z;
			
			return position;
		}
		
	};
	
	static {
		TYPES.put(e_VolumeType.BOX, VOL_BOX);
		TYPES.put(e_VolumeType.CYLINDER, VOL_CYLINDER);
		TYPES.put(e_VolumeType.LINE, VOL_LINE);
		TYPES.put(e_VolumeType.POINT, VOL_POINT);
		TYPES.put(e_VolumeType.SPHERE, VOL_SPHERE);
	}

}
