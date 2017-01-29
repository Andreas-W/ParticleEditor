package entitytypes;

import java.util.ArrayList;

//FXList entry
public class FXListType {
	
	public ArrayList<ParticleSystemEntry> ParticleSystems = new ArrayList<ParticleSystemEntry>();
	
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
	}
}
