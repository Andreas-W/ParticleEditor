package entitytypes;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import util.Util;


/**
 * ParticleSystem attributes, corresponding to entries in ParticleSystem.ini
 */

public class ParticleSystemType {
	
	public ParticleSystemType() {
		
	}
	
	
	
	

	public ParticleSystemType(ParticleSystemType other) {
		super();
		Priority = other.Priority;
		IsOneShot = other.IsOneShot;
		Shader = other.Shader;
		Type = other.Type;
		ParticleName = other.ParticleName;
		AngleZ = other.AngleZ.clone();
		AngularRateZ = other.AngularRateZ.clone();
		AngularDamping = other.AngularDamping.clone();
		VelocityDamping = other.VelocityDamping.clone();
		Gravity = other.Gravity;
		Lifetime = other.Lifetime.clone();
		SystemLifetime = other.SystemLifetime;
		PerParticleAttachedSystem = other.PerParticleAttachedSystem;
		SlaveSystem = other.SlaveSystem;
		SlavePosOffset = other.SlavePosOffset.clone();
		Size = other.Size.clone();
		StartSizeRate = other.StartSizeRate.clone();
		SizeRate = other.SizeRate.clone();
		SizeRateDamping = other.SizeRateDamping.clone();
		if(other.Alpha1 != null) Alpha1 = other.Alpha1.clone();
		if(other.Alpha2 != null) Alpha2 = other.Alpha2.clone();
		if(other.Alpha3 != null) Alpha3 = other.Alpha3.clone();
		if(other.Alpha4 != null) Alpha4 = other.Alpha4.clone();
		if(other.Alpha5 != null) Alpha5 = other.Alpha5.clone();
		if(other.Alpha6 != null) Alpha6 = other.Alpha6.clone();
		if(other.Alpha7 != null) Alpha7 = other.Alpha7.clone();
		if(other.Alpha8 != null) Alpha8 = other.Alpha8.clone();
		if(other.Alpha9 != null) Alpha9 = other.Alpha9.clone();

		if(other.Color1 != null) Color1 = other.Color1.clone();
		if(other.Color2 != null) Color2 = other.Color2.clone();
		if(other.Color3 != null) Color3 = other.Color3.clone();
		if(other.Color4 != null) Color4 = other.Color4.clone();
		if(other.Color5 != null) Color5 = other.Color5.clone();
		if(other.Color6 != null) Color6 = other.Color6.clone();
		if(other.Color7 != null) Color7 = other.Color7.clone();
		if(other.Color8 != null) Color8 = other.Color8.clone();
		if(other.Color9 != null) Color9 = other.Color9.clone();

		ColorScale = other.ColorScale.clone();
		BurstDelay = other.BurstDelay.clone();
		BurstCount = other.BurstCount.clone();
		InitialDelay = other.InitialDelay.clone();
		DriftVelocity = other.DriftVelocity.clone();
		VelocityType = other.VelocityType;
		VelOrthoX = other.VelOrthoX.clone();
		VelOrthoY = other.VelOrthoY.clone();
		VelOrthoZ = other.VelOrthoZ.clone();
		VelOutward = other.VelOutward.clone();
		VelOutwardOther = other.VelOutwardOther.clone();
		VelCylindricalRadial = other.VelCylindricalRadial.clone();
		VelCylindricalNormal = other.VelCylindricalNormal.clone();
		VelSpherical = other.VelSpherical.clone();
		VelHemispherical = other.VelHemispherical.clone();
		VolumeType = other.VolumeType;
		VolLineStart = other.VolLineStart.clone();
		VolLineEnd = other.VolLineEnd.clone();
		VolCylinderRadius = other.VolCylinderRadius;
		VolCylinderLength = other.VolCylinderLength;
		VolSphereRadius = other.VolSphereRadius;
		VolBoxHalfSize = other.VolBoxHalfSize;
		IsHollow = other.IsHollow;
		IsGroundAligned = other.IsGroundAligned;
		IsEmitAboveGroundOnly = other.IsEmitAboveGroundOnly;
		IsParticleUpTowardsEmitter = other.IsParticleUpTowardsEmitter;
	}





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
	
	public alphaEntry Alpha1 = new alphaEntry(1.0f, 1.0f, 0);
	public alphaEntry Alpha2;
	public alphaEntry Alpha3;
	public alphaEntry Alpha4;
	public alphaEntry Alpha5;
	public alphaEntry Alpha6;
	public alphaEntry Alpha7;
	public alphaEntry Alpha8;
	public alphaEntry Alpha9;
	
	public colorEntry Color1 = new colorEntry(255, 255, 255, 0);
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
	
	public class posEntry implements Cloneable{
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
		
		@Override
		protected posEntry clone(){
			return new posEntry(x,y,z);
		}
		
		public String getCode() {
			return String.format(" = X:%s Y:%s Z:%s\n", Util.fmt(x), Util.fmt(y), Util.fmt(z));
		}

		public boolean isZero() {
			return x == 0 && y == 0 && z == 0;
		}
	}
	
	public class colorEntry implements Cloneable {
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
		@Override
		protected colorEntry clone(){
			return new colorEntry(this.r, this.g, this.b, this.frame);
		}
		
		public String getCode(int n) {
			return String.format("Color%d = R:%d G:%d B:%d %d\n", n, r,g,b, frame);
		}
	}
	
	public class alphaEntry implements Cloneable{
		public float[] alpha;
		public int frame = 0;
		public alphaEntry(float alpha1, float alpha2, int frame) {
			this.alpha = new float[]{alpha1, alpha2};
			this.frame = frame;
		}
		@Override
		protected alphaEntry clone(){
			alphaEntry a = new alphaEntry(this.alpha[0], this.alpha[1], this.frame);
			return a;
		}
		
		public String getCode(int n) {
			return String.format("Alpha%d = %s %s %d\n", n, Util.fmt(alpha[0]), Util.fmt(alpha[1]), frame);
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

	public String createInnerCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("Priority = "+Priority+"\n");
		if(IsOneShot) sb.append("IsOneShot = Yes\n");
		sb.append("Shader = "+Shader.toString()+"\n");
		sb.append("ParticleName = "+ParticleName+"\n");
		if (Util.hasValues(AngleZ)) sb.append(String.format("AngleZ = %s %s\n", Util.fmt(AngleZ[0]), Util.fmt(AngleZ[1])));
		if (Util.hasValues(AngularRateZ)) sb.append(String.format("AngularRateZ = %s %s\n", Util.fmt(AngularRateZ[0]), Util.fmt(AngularRateZ[1])));
		if (Util.hasValues(AngularDamping)) sb.append(String.format("AngularDamping = %s %s\n", Util.fmt(AngularDamping[0]), Util.fmt(AngularDamping[1])));
		if (Util.hasValues(VelocityDamping)) sb.append(String.format("VelocityDamping = %s %s\n", Util.fmt(VelocityDamping[0]), Util.fmt(VelocityDamping[1])));
		if (Gravity != 0) sb.append("Gravity = "+Util.fmt(Gravity)+"\n");
		sb.append(String.format("Lifetime = %d %d\n", Lifetime[0], Lifetime[1]));
		sb.append("SystemLifetime = "+SystemLifetime+"\n");
		sb.append(String.format("Size = %s %s\n", Size[0], Size[1]));
		if (Util.hasValues(StartSizeRate)) sb.append(String.format("StartSizeRate = %s %s\n", StartSizeRate[0], StartSizeRate[1]));
		if (Util.hasValues(SizeRate)) sb.append(String.format("SizeRate = %s %s\n", SizeRate[0], SizeRate[1]));
		if (Util.hasValues(SizeRateDamping)) sb.append(String.format("SizeRateDamping = %s %s\n", SizeRateDamping[0], SizeRateDamping[1]));
		if (Alpha1!=null) sb.append(Alpha1.getCode(1));
		if (Alpha2!=null) sb.append(Alpha2.getCode(2));
		if (Alpha3!=null) sb.append(Alpha3.getCode(3));
		if (Alpha4!=null) sb.append(Alpha4.getCode(4));
		if (Alpha5!=null) sb.append(Alpha5.getCode(5));
		if (Alpha6!=null) sb.append(Alpha6.getCode(6));
		if (Alpha7!=null) sb.append(Alpha7.getCode(7));
		if (Alpha8!=null) sb.append(Alpha8.getCode(8));
		if (Alpha9!=null) sb.append(Alpha9.getCode(9));
		if (Color1!=null) sb.append(Color1.getCode(1));
		if (Color2!=null) sb.append(Color2.getCode(2));
		if (Color3!=null) sb.append(Color3.getCode(3));
		if (Color4!=null) sb.append(Color4.getCode(4));
		if (Color5!=null) sb.append(Color5.getCode(5));
		if (Color6!=null) sb.append(Color6.getCode(6));
		if (Color7!=null) sb.append(Color7.getCode(7));
		if (Color8!=null) sb.append(Color8.getCode(8));
		if (Color9!=null) sb.append(Color9.getCode(9));
		if (Util.hasValues(ColorScale)) sb.append(String.format("ColorScale = %d %d\n", ColorScale[0], ColorScale[1]));
		sb.append(String.format("BurstDelay = %d %d\n", BurstDelay[0], BurstDelay[1]));
		sb.append(String.format("BurstCount = %d %d\n", BurstCount[0], BurstCount[1]));
		if (Util.hasValues(InitialDelay)) sb.append(String.format("InitialDelay = %d %d\n", InitialDelay[0], InitialDelay[1]));
		if (DriftVelocity!= null && !DriftVelocity.isZero()) sb.append("DriftVelocty").append(DriftVelocity.getCode());
		sb.append("VelocityType = "+VelocityType.toString()+"\n");
		switch(VelocityType) {
		case CYLINDRICAL:
			sb.append(String.format("VelCylindricalRadial = %s %s\n", Util.fmt(VelCylindricalRadial[0]), Util.fmt(VelCylindricalRadial[1])));
			sb.append(String.format("VelCylindricalNormal = %s %s\n", Util.fmt(VelCylindricalNormal[0]), Util.fmt(VelCylindricalNormal[1])));
			break;
		case HEMISPHERICAL:
			sb.append(String.format("VelHemispherical = %s %s\n", Util.fmt(VelHemispherical[0]), Util.fmt(VelHemispherical[1])));
			break;
		case ORTHO:
			sb.append(String.format("VelOrthoX = %s %s\n", Util.fmt(VelOrthoX[0]), Util.fmt(VelOrthoX[1])));
			sb.append(String.format("VelOrthoY = %s %s\n", Util.fmt(VelOrthoY[0]), Util.fmt(VelOrthoY[1])));
			sb.append(String.format("VelOrthoZ = %s %s\n", Util.fmt(VelOrthoZ[0]), Util.fmt(VelOrthoZ[1])));
			break;
		case OUTWARD:
			sb.append(String.format("VelOutward = %s %s\n", Util.fmt(VelOutward[0]), Util.fmt(VelOutward[1])));
			sb.append(String.format("VelOutwardOther = %s %s\n", Util.fmt(VelOutwardOther[0]), Util.fmt(VelOutwardOther[1])));
			break;
		case SPHERICAL:
			sb.append(String.format("VelSpherical = %s %s\n", Util.fmt(VelSpherical[0]), Util.fmt(VelSpherical[1])));
			break;
		}
		sb.append("VolumeType = "+VolumeType.toString()+"\n");
		switch(VolumeType) {
		case BOX:
			sb.append("VolBoxHalfSize" + VolBoxHalfSize.getCode());
			break;
		case CYLINDER:
			sb.append("VolCylinderRadius = "+Util.fmt(VolCylinderRadius)+"\n");
			sb.append("VolCylinderLength = "+Util.fmt(VolCylinderLength)+"\n");
			break;
		case LINE:
			sb.append("VolLineStart" + VolLineStart.getCode());
			sb.append("VolLineEnd" + VolLineEnd.getCode());
			break;
		case SPHERE:
			sb.append("VolSphereRadius"+ Util.fmt(VolSphereRadius)+"\n");
			break;	
		}
		if(IsHollow) sb.append("IsHollow = Yes\n");
		if(IsGroundAligned) sb.append("IsGroundAligned = Yes\n");
		if(IsEmitAboveGroundOnly) sb.append("IsEmitAboveGroundOnly = Yes\n");
		if(IsParticleUpTowardsEmitter) sb.append("IsParticleUpTowardsEmitter = Yes\n");
		return sb.toString();
	}
}
