package entitytypes;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


/**
 * ParticleSystem attributes, corresponding to entries in ParticleSystem.ini
 */

public class ParticleSystemType {
	
	//ParticleSystem fields	(only include actual particleSystem field in this class)
	public String Priority = ""; //We don't really need this value, so it's just read as a string
	public boolean IsOneShot = false;
	public e_Shader Shader = e_Shader.ADDITIVE;
	public e_Type Type = e_Type.PARTICLE;
	public String ParticleName = "test.tga";
	
	public float[] AngleZ = new float[2];
	public float[] AngularRateZ = new float[2];
	public float[] AngularDamping = new float[2];
	
	public float[] VelocityDamping = new float[2];
	public float Gravity = 0.0f;
	public int[] Lifetime = new int[2];
	public int SystemLifetime = 0;
	
	public String PerParticleAttachedSystem = "";
	public String SlaveSystem = "";
	public posEntry SlavePosOffset = new posEntry(0.0f, 0.0f, 0.0f);
	
	public float[] Size = new float[] {1.0f, 1.0f};
	public float[] StartSizeRate = new float[2];
	public float[] SizeRate = new float[2];
	public float[] SizeRateDamping = new float[2];
	
	public alphaEntry Alpha1;
	public alphaEntry Alpha2;
	public alphaEntry Alpha3;
	public alphaEntry Alpha4;
	public alphaEntry Alpha5;
	public alphaEntry Alpha6;
	public alphaEntry Alpha7;
	public alphaEntry Alpha8;
	public alphaEntry Alpha9;
	
	public colorEntry Color1;
	public colorEntry Color2;
	public colorEntry Color3;
	public colorEntry Color4;
	public colorEntry Color5;
	public colorEntry Color6;
	public colorEntry Color7;
	public colorEntry Color8;
	public colorEntry Color9;
	
	public int[] ColorScale = new int[2];
	
	public int[] BurstDelay = new int[2];
	public int[] BurstCount = new int[2];
	public int[] InitialDelay = new int[2];
	public posEntry DriftVelocity = new posEntry(0.0f, 0.0f, 0.0f);

	public e_VelocityType VelocityType = e_VelocityType.NONE;
	
	public float[] VelOrthoX = new float[2];
	public float[] VelOrthoY = new float[2];
	public float[] VelOrthoZ = new float[2];
	
	public float[] VelOutward = new float[2];
	public float[] VelOutwardOther = new float[2];
	
	public float[] VelCylindricalRadial= new float[2];
	public float[] VelCylindricalNormal= new float[2];
	
	public float[] VelSpherical= new float[2];
	public float[] VelHemispherical= new float[2];
	
	public e_VolumeType VolumeType = e_VolumeType.POINT;
	
	public posEntry VolLineStart = new posEntry(0.0f, 0.0f, 0.0f);
	public posEntry VolLineEnd = new posEntry(0.0f, 0.0f, 0.0f);
	
	public float VolCylinderRadius = 0.0f;
	public float VolCylinderLength = 0.0f;
	
	public float VolSphereRadius = 0.0f;
	
	public posEntry VolBoxHalfSize = new posEntry(0.0f, 0.0f, 0.0f);
	
	public boolean IsHollow = false;
	public boolean IsGroundAligned = false;
	public boolean IsEmitAboveGroundOnly = false;
	public boolean IsParticleUpTowardsEmitter = false;
	
	//Ignore Wind Motion for now
	
	public class posEntry {
		public float x = 0.0f;
		public float y = 0.0f;
		public float z = 0.0f;
		public posEntry(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Vector3d toVec() {
			return new Vector3d(x, y, z);
		}
	}
	
	public class colorEntry {
		public int r = 0;
		public int g = 0;
		public int b = 0;
		public int frame = 0;
		public colorEntry(int r, int g, int b, int frame) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.frame = frame;
		}
		
		public Vector3f toVec() {
			return new Vector3f(r, g, b);
		}
	}
	
	public class alphaEntry {
		public float[] alpha;
		public int frame = 0;
		public alphaEntry(float alpha1, float alpha2, int frame) {
			this.alpha = new float[]{alpha1, alpha2};
			this.frame = frame;
		}
	}
	
	public enum e_Shader {
		ADDITIVE, ALPHA, ALPHA_TEST
	}
	
	public enum e_Type {
		PARTICLE, STREAK
	}
	
	public enum e_VelocityType {
		NONE, ORTHO, OUTWARD, CYLINDRICAL, SPHERICAL, HEMISPHERICAL
	}
	
	public enum e_VolumeType {
		POINT, LINE, CYLINDER, SPHERE, BOX
	}
}
