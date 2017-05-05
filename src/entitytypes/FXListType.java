package entitytypes;

import java.util.ArrayList;

import entitytypes.FXListType.ParticleSystemEntry;
import main.Main;
import util.Util;

//FXList entry
public class FXListType {
	
	public ArrayList<ParticleSystemEntry> ParticleSystems = new ArrayList<ParticleSystemEntry>();
	
	private boolean temporary = false;
	
	public String additionalEntries = "";
	
	//ParticleSystem entry in FXList
	public class ParticleSystemEntry {
		/*
			//We don't need these in the editor for now
			public boolean UseCallersRadius = false; // [Yes/No] ; matches particle system radius with weapon damage radius
			public boolean CreateAtGroundHeight = false; //[Yes/No]
			public boolean AttachToObject = false; // [Yes/No];
			public boolean Ricochet = false; //[Yes/No]
			
			public float RotateX = 0; //[integer]
			public float RotateY = 0; //[integer]
			public float RotateZ = 0; //[integer]
			
			public float[] Height = new float[];
			
		*/
		public String Name = "";
		public boolean OrientToObject = false; // [Yes/No]
		public float[] Offset = new float[]{ 0.0f, 0.0f, 0.0f };
		public RandomIntEntry InitialDelay;
		
		public int Count = 1;
		public RandomFloatEntry Radius;
		
		public RandomFloatEntry Height;
		
		//Additional Stuff (Ignored in this editor)
		public boolean UseCallersRadius = false;
		public boolean CreateAtGroundHeight = false;
		public boolean Ricochet = false;		
		
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
		}

		public ParticleSystemEntry() {
		}

		public String createCode() {
			StringBuilder sb = new StringBuilder();
			sb.append("ParticleSystem\n");
			sb.append("  Name = ").append(Name).append("\n");
			if (Count > 1)
				sb.append("Count = "+Count+"\n");
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
