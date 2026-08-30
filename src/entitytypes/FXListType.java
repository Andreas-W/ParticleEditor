package entitytypes;

import java.util.ArrayList;

import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType.e_Priority;
import main.Main;
import util.Util;

//FXList entry
public class FXListType {
	
	public ArrayList<ParticleSystemEntry> ParticleSystems = new ArrayList<ParticleSystemEntry>();
	
	private boolean temporary = false;
	
	public String additionalEntries = "";
	
	//ParticleSystem entry in FXList
	public class ParticleSystemEntry {
		//Field names must match the INI tokens exactly: Parser.parseToken() looks them
		//up with getField(token), and anything it can't find is dropped on load.
		public String Name = "";
		public boolean OrientToObject = false; // [Yes/No]
		public float[] Offset = new float[]{ 0.0f, 0.0f, 0.0f };
		public RandomIntEntry InitialDelay;
		
		public int Count = 1;
		public RandomFloatEntry Radius;
		
		public RandomFloatEntry Height;
		
		//Additional Stuff (Ignored in this editor, but read and written back)
		public boolean UseCallersRadius = false;
		public boolean CreateAtGroundHeight = false;
		public boolean Ricochet = false;
		public boolean AttachToObject = false;
		public float RotateX = 0;
		public float RotateY = 0;
		public float RotateZ = 0;
		
		//Only present in the modding fork of the engine, not in the base game
		public boolean OrientOffset = false;
		public boolean OrientXY = false;
		public boolean UseCachedSurfaceInfo = false;
		//"no limit" in the engine, so anything non-infinite means the modder set it
		public float MinAllowedHeight = Float.NEGATIVE_INFINITY;
		public float MaxAllowedHeight = Float.POSITIVE_INFINITY;
		public e_AllowedSurface AllowedSurface = e_AllowedSurface.ALL;
		
		public ParticleSystemEntry(ParticleSystemEntry other) {
			this.Name = other.Name;
			this.OrientToObject = other.OrientToObject;
			this.Offset = other.Offset.clone();
			if (other.InitialDelay != null) this.InitialDelay = new RandomIntEntry(other.InitialDelay);
			this.Count = other.Count;
			if (other.Radius != null) this.Radius = new RandomFloatEntry(other.Radius);
			if (other.Height != null) this.Height = new RandomFloatEntry(other.Height);
			this.UseCallersRadius = other.UseCallersRadius;
			this.CreateAtGroundHeight = other.CreateAtGroundHeight;
			this.Ricochet = other.Ricochet;
			this.AttachToObject = other.AttachToObject;
			this.RotateX = other.RotateX;
			this.RotateY = other.RotateY;
			this.RotateZ = other.RotateZ;
			this.OrientOffset = other.OrientOffset;
			this.OrientXY = other.OrientXY;
			this.UseCachedSurfaceInfo = other.UseCachedSurfaceInfo;
			this.MinAllowedHeight = other.MinAllowedHeight;
			this.MaxAllowedHeight = other.MaxAllowedHeight;
			this.AllowedSurface = other.AllowedSurface;
		}

		public ParticleSystemEntry() {
		}

		public String createCode() {
			StringBuilder sb = new StringBuilder();
			sb.append("ParticleSystem\n");
			sb.append("  Name = ").append(Name).append("\n");
			if (Count > 1)
				sb.append("  Count = "+Count+"\n");
			if (Offset[0] != 0 || Offset[1] != 0 || Offset[2] != 0)
				sb.append(String.format("  Offset = X:%s Y:%s Z:%s\n", Util.fmt(Offset[0]),  Util.fmt(Offset[1]),  Util.fmt(Offset[2])));
			if (InitialDelay != null && (InitialDelay.data[0] != 0 || InitialDelay.data[1] != 0))
				sb.append("  InitialDelay"+InitialDelay.getCode());
			if (Radius != null &&( Radius.data[0] != 0 || Radius.data[1] != 0))
				sb.append("  Radius"+Radius.getCode());
			if (Height != null && (Height.data[0] != 0 || Height.data[1] != 0))
				sb.append("  Height"+Height.getCode());
			if (OrientToObject)
				sb.append("  OrientToObject = Yes\n");
			if (UseCallersRadius)
				sb.append("  UseCallersRadius = Yes\n");
			if (CreateAtGroundHeight)
				sb.append("  CreateAtGroundHeight = Yes\n");
			if (Ricochet)
				sb.append("  Ricochet = Yes\n");
			if (AttachToObject)
				sb.append("  AttachToObject = Yes\n");
			if (RotateX != 0)
				sb.append("  RotateX = "+Util.fmt(RotateX)+"\n");
			if (RotateY != 0)
				sb.append("  RotateY = "+Util.fmt(RotateY)+"\n");
			if (RotateZ != 0)
				sb.append("  RotateZ = "+Util.fmt(RotateZ)+"\n");
			if (OrientOffset)
				sb.append("  OrientOffset = Yes\n");
			if (OrientXY)
				sb.append("  OrientXY = Yes\n");
			if (UseCachedSurfaceInfo)
				sb.append("  UseCachedSurfaceInfo = Yes\n");
			//Util.fmt would write the literal "Infinity", which is not valid INI,
			//so only write these once the modder has given them a real limit.
			if (!Float.isInfinite(MinAllowedHeight))
				sb.append("  MinAllowedHeight = "+Util.fmt(MinAllowedHeight)+"\n");
			if (!Float.isInfinite(MaxAllowedHeight))
				sb.append("  MaxAllowedHeight = "+Util.fmt(MaxAllowedHeight)+"\n");
			if (AllowedSurface != e_AllowedSurface.ALL)
				sb.append("  AllowedSurface = "+AllowedSurface.toString()+"\n");
			sb.append("End\n");
			return sb.toString();
		}
		
		private boolean visible = true;

		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
		
		public class RandomIntEntry {
			public int[] data = new int[2];
			public e_RandomType rtype = e_RandomType.UNIFORM;		
			public RandomIntEntry(int i1, int i2, e_RandomType rtype) {
				data[0] = i1;
				data[1] = i2;
				this.rtype = rtype;
			}
			public RandomIntEntry(RandomIntEntry other) {
				data = other.data.clone();
				this.rtype = other.rtype;
			}
			public String getCode() {
				return String.format(" = %d %d %s\n", data[0], data[1], rtype.toString());
			}
		}
		
		public class RandomFloatEntry {
			public float[] data = new float[2];
			public e_RandomType rtype = e_RandomType.UNIFORM;		
			public RandomFloatEntry(float f1, float f2, e_RandomType rtype) {
				data[0] = f1;
				data[1] = f2;
				this.rtype = rtype;
			}
			public RandomFloatEntry(RandomFloatEntry other) {
				data = other.data.clone();
				this.rtype = other.rtype;
			}
			public String getCode() {
				return String.format(" = %s %s %s\n", Util.fmt(data[0]), Util.fmt(data[1]), rtype.toString());
			}
		}

		/**
		 * Mirrors isValidSurface() in the engine's FXList.cpp: an entry limited to one
		 * surface is skipped on the other one, and ALL always plays. "selected" is the
		 * surface the FX is being played on, so ALL there means "do not filter".
		 */
		public boolean matchesSurface(e_AllowedSurface selected) {
			if (selected == e_AllowedSurface.ALL) return true;
			if (selected == e_AllowedSurface.LAND) return AllowedSurface != e_AllowedSurface.WATER;
			return AllowedSurface != e_AllowedSurface.LAND;
		}

		public void setValues(ParticleSystemEntry other) {
			this.Name = other.Name;
			this.OrientToObject = other.OrientToObject;
			this.Offset = other.Offset.clone();
			if (other.InitialDelay != null) this.InitialDelay = new RandomIntEntry(other.InitialDelay);
			this.Count = other.Count;
			if (other.Radius != null) this.Radius = new RandomFloatEntry(other.Radius);
			if (other.Height != null) this.Height = new RandomFloatEntry(other.Height);
			this.UseCallersRadius = other.UseCallersRadius;
			this.CreateAtGroundHeight = other.CreateAtGroundHeight;
			this.Ricochet = other.Ricochet;
			this.AttachToObject = other.AttachToObject;
			this.RotateX = other.RotateX;
			this.RotateY = other.RotateY;
			this.RotateZ = other.RotateZ;
			this.OrientOffset = other.OrientOffset;
			this.OrientXY = other.OrientXY;
			this.UseCachedSurfaceInfo = other.UseCachedSurfaceInfo;
			this.MinAllowedHeight = other.MinAllowedHeight;
			this.MaxAllowedHeight = other.MaxAllowedHeight;
			this.AllowedSurface = other.AllowedSurface;
		}
				
	}
	
	public FXListType(FXListType other) {
		this.additionalEntries = other.additionalEntries;
		this.temporary = false;
		this.ParticleSystems = new ArrayList<FXListType.ParticleSystemEntry>();
		for (ParticleSystemEntry entry : other.ParticleSystems) {
			this.ParticleSystems.add(new ParticleSystemEntry(entry));
		}
	}

	

	public FXListType() {}



	public String createInnerCode() {
		StringBuilder sb = new StringBuilder();
		for (ParticleSystemEntry entry : ParticleSystems) {
			sb.append(entry.createCode());
		}
		sb.append(additionalEntries);
		return sb.toString();
	}

	
	public static FXListType getFXTypeFromParticle(String particleName) {
		FXListType type = new FXListType();
		ParticleSystemEntry entry = type.new ParticleSystemEntry();
		entry.Name = particleName;
		type.ParticleSystems.add(entry);
		type.setTemporary(true);
		return type;
	}


	public void setTemporary(boolean b) {
		this.temporary = b;
	}
	public boolean isTemporary() {
		return temporary;
	}
	
	public enum e_RandomType {
		CONSTANT, UNIFORM, GAUSSIAN, TRIANGULAR, LOW_BIAS, HIGH_BIAS
	}
	
	//Matches AllowedSurfaceNames in the engine's FXList.cpp
	public enum e_AllowedSurface {
		ALL, LAND, WATER
	}

	public String getFormattedCode(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append("FXList "+name);
		for (String line : this.createInnerCode().split("\n")) {
			sb.append("\n  ").append(line);
		}
		sb.append("\nEnd");
		return sb.toString();
	}
}
