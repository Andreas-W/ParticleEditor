package entities;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

import main.Engine;
import main.Main;
import entitytypes.FXListType;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.ParticleSystemType;
import util.MathUtil;

/**
 * Active FXList object
 */
public class FXList extends Entity{
	
	FXListType type;
	int[] spawnTimes; //times were particles are created
	
	public FXList(FXListType type) {
		this.type = type;
	}
	
	@Override
	public void init(Engine engine) {
		super.init(engine);
		spawnTimes = new int[type.ParticleSystems.size()];
		for(int i = 0; i < type.ParticleSystems.size(); i++) {
			ParticleSystemEntry entry = type.ParticleSystems.get(i);
			if (entry.isVisible()) {
				preLoadParticleSystem(entry);
			}
		}
	}
	
	@Override
	public void update() {
		boolean done = true;
		
		//FXList doesn't do anything right now
		//We might still need this later for LightPulse/Sound, etc.
		
//		for (int i = 0; i < spawnTimes.length; i++) {
//			if (spawnTimes[i] > -1) {
//				done = false;
//				if (spawnTimes[i] <= this.timer) {
//					spawnTimes[i] = -1;
//
//					//Create a Particle System
//					
//				}
//			}
//		}
		if (done) {
			this.dead = true;
		}
		super.update();
	}
	
	public void preLoadParticleSystem(ParticleSystemEntry entry) {
		ParticleSystemType p_type = Main.getParticleSystem(entry.Name);
		if (type != null) {
			for (int i = 0; i < entry.Count; i++) {
				int spawnDelay = 0;
				if (entry.InitialDelay != null) spawnDelay += (int) ((float)MathUtil.getRandomFloat(entry.InitialDelay.data[0], entry.InitialDelay.data[1], entry.InitialDelay.rtype) / 33.333f);
				ParticleSystem psys = new ParticleSystem(p_type, spawnDelay);
				Vector3d position = this.getPosition();
				position.add(new Vector3d(entry.Offset[0], entry.Offset[1], entry.Offset[2]));
				if (entry.Radius != null) {
					float r = MathUtil.getRandomFloat(entry.Radius.data, entry.Radius.rtype);
					Vector2f xyOffset = MathUtil.getPolarOffset((float) MathUtil.randomAngleRad(), r);
					position.x += xyOffset.x;
					position.y += xyOffset.y;
				}
				if (entry.Height != null) {
					float h = MathUtil.getRandomFloat(entry.Height.data, entry.Height.rtype);
					position.z += h;
				}
				psys.setPosition(position);
		
				engine.addEntity(psys);
				psys.init(engine);
			}
		}
	}

}
