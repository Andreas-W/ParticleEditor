package entities;

import java.awt.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.media.j3d.Texture;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import util.MathUtil;
import util.VelocityTypes;
import util.VelocityTypes.IVelocityType;
import util.VolumeTypes;
import main.Engine;
import main.Main;
import main.Renderer;
import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.e_Type;


/**
 * This is the actual particle system entity.
 * It needs all the min-max attributes that are rolled per-system. We get everything else from the Type
 * 
 */
public class ParticleSystem extends Entity{
	public ParticleSystemType type;
	
	public int InitialDelay;
	public float StartSizeRate; //I'm not entirely sure if this is per-system and not rolled each frame
	public float StartSize = 0;
	public ArrayList<SimpleEntry<Integer, Vector3f>> colors;
	
	public ParticleSystem slave = null;
	public ParticleSystem master = null;
	
	private int nextBurst = 0;
	
	public Texture texture;
	
	public Entity parent; //Attached to an Object or a particle

	private int spawnDelay = 0;
	
	private StreakParticle prevParticle = null;

	
	public ParticleSystem(ParticleSystemType type, int spawnDelay) {
		this.type = type;
		this.spawnDelay = spawnDelay;
	}
	
	@Override
	public void init(Engine engine) {
		super.init(engine);
		if (this.type == null) return;
		if (this.master == null)  InitialDelay = MathUtil.getRandomInt(type.InitialDelay) + spawnDelay;
		else this.InitialDelay = this.master.InitialDelay;
		
		StartSizeRate = MathUtil.getRandomFloat(type.StartSizeRate);
		colors = new ArrayList<SimpleEntry<Integer, Vector3f>>();
		if (type.Color1 != null){ colors.add(new SimpleEntry<Integer, Vector3f>(type.Color1.frame, type.Color1.toVec()));
		if (type.Color2 != null && type.Color2.frame > type.Color1.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color2.frame, type.Color2.toVec()));
		if (type.Color3 != null && type.Color3.frame > type.Color2.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color3.frame, type.Color3.toVec()));
		if (type.Color4 != null && type.Color4.frame > type.Color3.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color4.frame, type.Color4.toVec()));
		if (type.Color5 != null && type.Color5.frame > type.Color4.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color5.frame, type.Color5.toVec()));
		if (type.Color6 != null && type.Color6.frame > type.Color5.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color6.frame, type.Color6.toVec()));
		if (type.Color7 != null && type.Color7.frame > type.Color6.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color7.frame, type.Color7.toVec()));
		if (type.Color8 != null && type.Color8.frame > type.Color7.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color8.frame, type.Color8.toVec()));
		if (type.Color9 != null && type.Color9.frame > type.Color8.frame) {colors.add(new SimpleEntry<Integer, Vector3f>(type.Color9.frame, type.Color9.toVec()));	}}}}}}}}}
		this.texture = engine.renderer.TextureMap.get(this.type.ParticleName.substring(0, this.type.ParticleName.length()-4));
		if (this.master == null) { //Can only have slave if this is not already a slave system
			if (type.SlaveSystem != null && !type.SlaveSystem.equals("") && Main.ParticleSystemTypes.containsKey(type.SlaveSystem)) {
				ParticleSystemType ptype = Main.getParticleSystem(type.SlaveSystem);
				ParticleSystem psys = new ParticleSystem(ptype, spawnDelay);
				this.slave = psys;
				psys.master = this;
				Vector3d position = this.getPosition();
				if (type.SlavePosOffset != null)position.add(type.SlavePosOffset.toVec());
				psys.setPosition(position);
				engine.addEntity(psys);
				psys.init(engine);
			}
		}
	}
	
	@Override
	public void update() {
		if (type == null){
			this.dead = true;
			return;
		}
		
		//Slaved systems' particles are spawned by the master. We still tick it's timer
		if (this.master != null) {
			super.update();
			if (type.SystemLifetime != 0 && this.timer > type.SystemLifetime) {
				this.dead = true;
			}
			return;
		}
		
		if (InitialDelay > 0) {
			InitialDelay--;
		}else {
			super.update();
			if (type.SystemLifetime != 0 && this.timer > type.SystemLifetime) {
				this.dead = true;
				//System.out.println("ParticleSystemLifetime is over.");
				return;
			}
			//Actual Particle System logic
			if (nextBurst-- <= 0) {
				nextBurst = MathUtil.getRandomInt(type.BurstDelay);
				int burstCount = MathUtil.getRandomInt(type.BurstCount);
				for (int i = 0; i < burstCount; i++) {
					Vector3d dir = new Vector3d(0.0, 0.0, 0.0);
					
					Vector3d offset = getSpawnPosition(dir);
					Vector3d velocity = getSpawnVelocity(dir);			
					
					Vector3d position = new Vector3d(offset);
					position.add(this.getPosition());
					//TODO: Add parent rotation and offset
					

					if (this.type.Type == e_Type.PARTICLE) {
						Particle part = new Particle(this, position, offset, velocity, StartSize);
						engine.addEntity(part);
					}else if (this.type.Type == e_Type.STREAK) {
						StreakParticle part = new StreakParticle(this, position, offset, velocity, StartSize);
						if (prevParticle != null && !prevParticle.dead) part.prev = prevParticle;
						prevParticle = part;
						engine.addEntity(part);
					}
					
					if (this.slave != null) { //Spawn another particle for the slave						
						dir = new Vector3d(0.0, 0.0, 0.0);						
						offset = getSpawnPosition(dir);
						velocity = getSpawnVelocity(dir);			
						
						position = new Vector3d(offset);
						position.add(this.getPosition());
						position.add(this.type.SlavePosOffset.toVec());

						if (this.type.Type == e_Type.PARTICLE) {
							Particle part = new Particle(this.slave, position, offset, velocity, StartSize);
							engine.addEntity(part);
						}else if (this.type.Type == e_Type.STREAK) {
							StreakParticle part = new StreakParticle(this.slave, position, offset, velocity, StartSize);
							if (slave.prevParticle != null && !slave.prevParticle.dead) part.prev = slave.prevParticle;
							slave.prevParticle = part;
							engine.addEntity(part);
						}
					}
					
					StartSize = Math.min(StartSize + StartSizeRate, 50.0f);
				}
			}
		}
	}

	private Vector3d getSpawnVelocity(Vector3d dir) {
		return VelocityTypes.TYPES.get(this.type.VelocityType).getVelocity(this, dir);
	}

	private Vector3d getSpawnPosition(Vector3d dir) {
		return VolumeTypes.TYPES.get(this.type.VolumeType).getPosition(this, dir);
	}
}
