package filter;

import java.util.List;

import entitytypes.FXListType;
import entitytypes.ParticleSystemType;

public class ScaleSizeFilter implements IParticleFilter{

	@Override
	public boolean applyFilter(FXListType ftype, float factor) {
		return false;
	}

	@Override
	public boolean applyFilter(List<ParticleSystemType> ptypes, float factor) {
	
		return true;
		
	}

	@Override
	public String getName() {
		return "Scale size";
	}

	@Override
	public String getDescription() {
		return "Scale the total size of a particlesystem by a factor";
	}

	@Override
	public String getPreviewText(ParticleSystemType ptype, float factor) {
		// TODO Auto-generated method stub
		return null;
	}

}
