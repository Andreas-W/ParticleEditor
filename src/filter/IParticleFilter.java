package filter;

import java.util.List;

import entitytypes.FXListType;
import entitytypes.ParticleSystemType;

public interface IParticleFilter {
	
	public boolean applyFilter (FXListType ftype, float factor);
	public boolean applyFilter (List<ParticleSystemType> ptypes, float factor);

	public String getName();
	public String getDescription();
	public String getPreviewText(ParticleSystemType ptype, float factor);

		
}
