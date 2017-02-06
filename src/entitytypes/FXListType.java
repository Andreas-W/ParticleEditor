package entitytypes;

import java.util.ArrayList;

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
		public int[] InitialDelay = new int[]{0, 0};
		public int Count = 1;
		public float[] Radius = new float[]{0, 0};
		public float[] Height = new float[]{0, 0};
		//Additional Stuff (Ignored in this editor)
		public boolean UseCallersRadius = false;
		public boolean CreateAtGroundHeight = false;
		public boolean Ricochet = false;		
		
		public String createCode() {
			StringBuilder sb = new StringBuilder();
			sb.append("ParticleSystem\n");
			sb.append("  Name = ").append(Name).append("\n");
			if (Offset[0] != 0 || Offset[1] != 0 || Offset[2] != 0)
				sb.append(String.format("  Offset = X:%s Y:%s Z:%s\n", Util.fmt(Offset[0]),  Util.fmt(Offset[1]),  Util.fmt(Offset[2])));
			if (InitialDelay[0] != 0 || InitialDelay[1] != 0)
				sb.append(String.format("  InitialDelay = %d %d UNIFORM\n", InitialDelay[0], InitialDelay[1]));
			if (Radius[0] != 0 || Radius[1] != 0)
				sb.append(String.format("  Radius = %s %s UNIFORM\n",  Util.fmt(Radius[0]),  Util.fmt(Radius[1])));
			if (Height[0] != 0 || Height[1] != 0)
				sb.append(String.format("  Height = %s %s UNIFORM\n",  Util.fmt(Height[0]),  Util.fmt(Height[1])));
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
				
	}
	
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
}
