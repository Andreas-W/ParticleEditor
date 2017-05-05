package entities;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.alphaEntry;
import entitytypes.ParticleSystemType.e_VolumeType;
import main.Engine;
import main.Main;
import util.MathUtil;

public abstract class ParticleEntity extends Entity {
	public static Color3f BLACK = new Color3f(0.0f, 0.0f, 0.0f);
	public static Color3f WHITE = new Color3f(1.0f, 1.0f, 1.0f);
	
	
	//This particle's emitter
	public ParticleSystem system;
	//---
	//Particle Attributes
	public float AngleZ;
	public float AngularRateZ;
	public float AngularDamping;
	
	public Vector3d Velocity;
	
	public float VelocityDamping;
	public int Lifetime;
	
	public ParticleSystem AttachedSystem = null;
	
	public float SlaveScale = 1.0f; //Used for Particles from Slaved Systems
	public float Size;
	public float SizeRate;
	public float SizeRateDamping;
	
	private float startSize;
	
	//public ArrayList<Float> alphaValues;
	//public ArrayList<Integer> alphaFrames;
	
	public ArrayList<SimpleEntry<Integer, Float>> alphaValues;
	
	public float ParticleAlpha;
	public Vector3f ParticleColor = new Vector3f(0,0,0);
	
	public float ColorScale;
	
	private Vector3d offset;
	private final Vector3d spawnPos;
	
	public BranchGroup bg;
	public Appearance ap;
	
	
	public ParticleEntity(ParticleSystem sys, Vector3d position, Vector3d offset, Vector3d velocity, float startSize) {
		this.system = sys;
		this.setPosition(position);
		this.Velocity = velocity;
		this.offset = offset;
		this.spawnPos = new Vector3d(position);
		this.startSize = startSize;
		if (Double.isNaN(Velocity.x))  {
			System.out.println("Velocity NaN");
		}
	}
	
	@Override
	public void update() {
		//-- Lifetime
		//Util.startTimer("ParticleUpdate_Total");
		super.update();
		if (this.timer >= this.Lifetime) {
			this.dead = true;
			this.bg.detach();
		}
		
		if (this.dead) {
			if (this.AttachedSystem != null) {
				AttachedSystem.dead = true;
			}
			return;
		}
		//-- Motion
		Velocity.z += system.type.Gravity;
		Velocity.scale(VelocityDamping);
			

		//Velocity.add(system.type.DriftVelocity.toVec());
		Vector3d vel = system.type.DriftVelocity.toVec();
		vel.add(Velocity);
		this.posX += vel.x;
		this.posY += vel.y;
		this.posZ += vel.z;

		//-- Size
		Size = Math.max(Size + SizeRate, MathUtil.TINY_FLOAT);
		SizeRate *= SizeRateDamping;
		
		//-- AngleZ
		AngleZ += AngularRateZ;
		AngularRateZ *= AngularDamping;
		
		if (system.type.IsParticleUpTowardsEmitter && system.type.IsGroundAligned) {
			Vector3d current_offset = this.getPosition(); //new vector
			current_offset.sub(current_offset, spawnPos);
			current_offset.add(offset);
			this.AngleZ = (float) (-Math.atan2(current_offset.y, current_offset.x) - Math.PI*0.5);
		}
		
		//-- Color
		/* ------------------------
		 * INTERPOLATE COLOR VALUES
		 */
		SimpleEntry<Integer, Vector3f> c1 = null;
		SimpleEntry<Integer, Vector3f> c2 = null;
		SimpleEntry<Integer, Vector3f> prevC = null;
		for (SimpleEntry<Integer, Vector3f> c : this.system.colors) {
			if (timer < c.getKey()) {
				c1 = (prevC != null) ? prevC : c;
				c2 = c;
				break;
			}
			prevC = c;
		}
		if (c1 == null) c1 = this.system.colors.get(this.system.colors.size()-1);
		if (c2 == null) c2 = c1;
		float csVal = this.ColorScale * this.timer;
		if (c1 != c2) {
			float p = (float)(timer-c1.getKey()) / (float)(c2.getKey()-c1.getKey());

			//TODO: NUMERICAL ERROR DETECTION!
//			double mr = (c2.getValue().x/255.0 - c1.getValue().x/255.0)/(c2.getKey() - c1.getKey());
//			if ((Math.abs(mr) <= 0.02)) {
//				System.out.println("mr = "+mr);
//			}
			
			
//			//RGB to HSB
//			float[] hsb1 = Color.RGBtoHSB((int)(c1.getValue().x), (int)(c1.getValue().y), (int)(c1.getValue().z), null);
//			float[] hsb2 = Color.RGBtoHSB((int)(c2.getValue().x), (int)(c2.getValue().y), (int)(c2.getValue().z), null);	
//			//HSB to RGB;
//			Color color = new Color(Color.HSBtoRGB(hsb1[0]*(1f-p) + hsb2[0]*(p), hsb1[1]*(1f-p) + hsb2[1]*(p), hsb1[2]*(1f-p) + hsb2[2]*(p)));
//			this.ParticleColor.x = color.getRed() + csVal;
//			this.ParticleColor.y = color.getGreen() + csVal;
//			this.ParticleColor.z = color.getBlue() + csVal;
			//ACTUALLY IS JUST RGB INTERPOLATION
			this.ParticleColor.x = c1.getValue().x * (1f-p) + c2.getValue().x * p + csVal;
			this.ParticleColor.y = c1.getValue().y * (1f-p) + c2.getValue().y * p + csVal;
			this.ParticleColor.z = c1.getValue().z * (1f-p) + c2.getValue().z * p + csVal;
		}else {
			Vector3f col = c1.getValue();
			this.ParticleColor.x = col.x + csVal;
			this.ParticleColor.y = col.y + csVal;
			this.ParticleColor.z = col.z + csVal;
		}

		/*-------------------------
		 * INTERPOLATE ALPHA VALUES
		 */
		SimpleEntry<Integer, Float> a1 = null;
		SimpleEntry<Integer, Float> a2 = null;
		SimpleEntry<Integer, Float> prevA = null;
		for (SimpleEntry<Integer, Float> a : this.alphaValues) {
			if (timer < a.getKey()) {
				a1 = (prevA != null) ? prevA : a;
				a2 = a;
				break;
			}
			prevA = a;
		}
		if (a1 == null) a1 = this.alphaValues.get(this.alphaValues.size()-1);
		if (a2 == null) a2 = a1;
		float p;
		if (a1.getKey() == a2.getKey()) p = 1.0f;
		else p = (float)(timer-a1.getKey()) / (float)(a2.getKey()-a1.getKey());		
		//interpolate
		this.ParticleAlpha = a1.getValue()*(1f-p) + a2.getValue() * (p);
		
		//Util.stopTimer("ParticleUpdate_Colors");
		
		//-- Attached System --
		if (AttachedSystem != null) {
			AttachedSystem.setPosition(this.getPosition());
		}
	}

	
	@Override
	public void init(Engine engine) {
		super.init(engine);
		
	    bg = new BranchGroup();
		
		if (Math.abs(system.type.AngleZ[0]-system.type.AngleZ[1]) >= 2*Math.PI) {
			this.AngleZ = MathUtil.getRandomFloat(0.0f, (float) (Math.PI*2.0f));
		}else {
			this.AngleZ = MathUtil.getRandomFloat(system.type.AngleZ);
		}
		
		this.AngularRateZ= MathUtil.getRandomFloat(system.type.AngularRateZ);
		this.AngularDamping = MathUtil.getRandomFloat(system.type.AngularDamping);		
		this.Lifetime = MathUtil.getRandomInt(system.type.Lifetime);
		
		this.ColorScale = MathUtil.getRandomFloat(system.type.ColorScale);
		
		if (this.system.master != null) {
			this.Size = MathUtil.getRandomFloat(system.master.type.Size) + startSize;		
			this.SlaveScale = MathUtil.getRandomFloat(system.type.Size);
			this.VelocityDamping = MathUtil.getRandomFloat(system.master.type.VelocityDamping);
			this.SizeRate = MathUtil.getRandomFloat(system.master.type.SizeRate);
			this.SizeRateDamping = MathUtil.getRandomFloat(system.master.type.SizeRateDamping);
		}else {
			this.Size = MathUtil.getRandomFloat(system.type.Size) + startSize;		
			this.VelocityDamping = MathUtil.getRandomFloat(system.type.VelocityDamping);
			this.SizeRate = MathUtil.getRandomFloat(system.type.SizeRate);
			this.SizeRateDamping = MathUtil.getRandomFloat(system.type.SizeRateDamping);
		}
		
		//Corrections based on in-game behaviour of IsParticleUpTowardsEmitter. It's weird
		if (system.type.IsParticleUpTowardsEmitter) {
			this.AngularDamping = 1.0f;
			this.AngularRateZ = 0.0f;
			if (!system.type.IsGroundAligned) {
				if (Math.abs(this.Velocity.length()) <= 0.001 && system.type.VolumeType == e_VolumeType.POINT) {
					this.AngleZ = (float) Math.PI;
				}else {
					this.AngleZ = MathUtil.getRandomFloat(0.0f, (float) (Math.PI*2.0f));
				}
			}else {
				this.AngleZ = (float) (-Math.atan2(this.offset.y, this.offset.x)- Math.PI*0.5);
			}
		}
		
		alphaValues = new ArrayList<SimpleEntry<Integer,Float>>();

		addAlphaValue(system.type.Alpha1);
		addAlphaValue(system.type.Alpha2);
		addAlphaValue(system.type.Alpha3);
		addAlphaValue(system.type.Alpha4);
		addAlphaValue(system.type.Alpha5);
		addAlphaValue(system.type.Alpha6);
		addAlphaValue(system.type.Alpha7);
		addAlphaValue(system.type.Alpha8);
		addAlphaValue(system.type.Alpha9);
		
//		Util.stopTimer("ParticleInit_Randoms");
		
		if (system.type.PerParticleAttachedSystem != null && !system.type.PerParticleAttachedSystem.equals("") && Main.ParticleSystemTypes.containsKey(system.type.PerParticleAttachedSystem)) {
			ParticleSystemType p_type = Main.getParticleSystem(system.type.PerParticleAttachedSystem);
			ParticleSystem psys = new ParticleSystem(p_type, 0);
			psys.setPosition(this.getPosition());
			this.AttachedSystem = psys;
			psys.parent = this;
			engine.addEntity(psys);
			psys.init(engine);
		}
		
	}
	
	private void addAlphaValue(alphaEntry a) {
		if (a != null) {
			if (alphaValues.size() == 0 || alphaValues.get(alphaValues.size()-1).getKey() < a.frame) {
				alphaValues.add(new SimpleEntry<Integer, Float>(a.frame, MathUtil.getRandomFloat(a.alpha)));
			}
		}
	}
	
	protected void setUpTransparency() {
		float a;
		TransparencyAttributes t_attr;		
		switch (system.type.Shader) {
		case ALPHA:
		case ALPHA_TEST: //???
			a = 0.0f;
			if (this.alphaValues.size() >= 1) a = 1f-this.alphaValues.get(0).getValue(); 
			t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, a, TransparencyAttributes.BLEND_SRC_ALPHA, TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA);
			t_attr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap.setTransparencyAttributes( t_attr);
			break;
		case ADDITIVE:
		default:
			t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 1.0f, TransparencyAttributes.BLEND_ONE, TransparencyAttributes.BLEND_ONE);
			t_attr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
			ap.setTransparencyAttributes( t_attr);
			break;
		}
	}	
	
	@Override
	public void clear3D() {
		engine.renderer.newParticleGroup.removeChild(bg);
		//engine.renderer.particleGroup.removeChild(billboard);
	}
}
