package entities;

import java.awt.Color;
import java.awt.event.TextEvent;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.media.j3d.Appearance;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;

import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.alphaEntry;
import entitytypes.ParticleSystemType.e_Shader;
import util.MathUtil;
import main.Engine;
import main.Main;
import main.Renderer;

public class Particle extends Entity{
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
	
	public float Size;
	public float SizeRate;
	public float SizeRateDamping;
	
	//public ArrayList<Float> alphaValues;
	//public ArrayList<Integer> alphaFrames;
	
	public ArrayList<SimpleEntry<Integer, Float>> alphaValues;
	
	public float ParticleAlpha;
	public Vector3f ParticleColor = new Vector3f(0,0,0);
	
	public int ColorScale;
	
	//Java3D stuff
	public BranchGroup bg;
	public TransformGroup tg;
	public Shape3D shape;
	//public Billboard billboard;
	public Appearance ap;
	TransformGroup billboardGroup;
	
	public Particle(ParticleSystem sys, Vector3d position, Vector3d velocity) {
		this.system = sys;
		this.setPosition(position);
		this.Velocity = velocity;
	}
	
	@Override
	public void update() {
		//-- Lifetime
		super.update();
		if (this.timer >= this.Lifetime) {
			this.dead = true;
		}
		if (this.dead && this.AttachedSystem != null) {
			AttachedSystem.dead = true;
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
		Size += SizeRate;
		SizeRate *= SizeRateDamping;
		
		//-- AngleZ
		AngleZ += AngularRateZ;
		AngularRateZ *= AngularDamping;
		
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
		float p = (float)(timer-c1.getKey()) / (float)(c2.getKey()-c1.getKey());		
		//RGB to HSB
		float[] hsb1 = Color.RGBtoHSB((int)(c1.getValue().x), (int)(c1.getValue().y), (int)(c1.getValue().z), null);
		float[] hsb2 = Color.RGBtoHSB((int)(c2.getValue().x), (int)(c2.getValue().y), (int)(c2.getValue().z), null);	
		//HSB to RGB;
		Color color = new Color(Color.HSBtoRGB(hsb1[0]*(1f-p) + hsb2[0]*(p), hsb1[1]*(1f-p) + hsb2[1]*(p), hsb1[2]*(1f-p) + hsb2[2]*(p)));
		int csVal = this.ColorScale * this.timer;
		this.ParticleColor.x = color.getRed() + csVal;
		this.ParticleColor.y = color.getGreen() + csVal;
		this.ParticleColor.z = color.getBlue() + csVal;

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
		if (a1.getKey() == a2.getKey()) p = 1.0f;
		else p = (float)(timer-a1.getKey()) / (float)(a2.getKey()-a1.getKey());		
		//interpolate
		this.ParticleAlpha = a1.getValue()*(1f-p) + a2.getValue() * (p);
		
		//-- Attached System --
		if (AttachedSystem != null) {
			AttachedSystem.setPosition(this.getPosition());
		}
		
		//---------------------
		//Update J3D stuff
//	    Transform3D trans2 = new Transform3D();
//		trans2.setRotation(new AxisAngle4f(1.0f, 1.0f, 0.0f, this.AngleZ));
//		billboardGroup.setTransform(trans2);
		
		//billboard.setRotationPoint(new Point3f(MathUtil.toJ3DVec(this.getPosition())));
		if (system.type.IsGroundAligned == false) {
			if (this.AngleZ != 0) {
				setPlaneCoordinates((QuadArray) shape.getGeometry());
			}
			((OrientedShape3D)shape).setRotationPoint(new Point3f(MathUtil.toJ3DVec(this.getPosition())));
		}else {
			if (this.AngleZ != 0) {
				Transform3D trans2 = new Transform3D();
				trans2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, this.AngleZ));
				billboardGroup.setTransform(trans2);
			}
		}
		
		
		Transform3D trans = new Transform3D();
		//Transform3D scale = new Transform3D();
		trans.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
		//trans.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, this.AngleZ));
		trans.setScale((double)this.Size * MathUtil.J3D_COORD_SCALE);
		trans.setTranslation(MathUtil.toJ3DVec(this.getPosition()));
		//trans.mul(scale);
		tg.setTransform(trans);
		//--
		if (system.type.Shader == e_Shader.ALPHA || system.type.Shader == e_Shader.ALPHA_TEST) {
			ap.getTransparencyAttributes().setTransparency(1.0f-this.ParticleAlpha);
		}
		//System.out.printf("Alpha = %.4f -- Transparency = %.4f\n",this.ParticleAlpha, ap.getTransparencyAttributes().getTransparency());
		
		Color3f col = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f c = this.ParticleColor;
		col.x = c.x/255.0f;
		col.y = c.y/255.0f;
		col.z = c.z/255.0f;
//		ap.getMaterial().setAmbientColor(col);
//		ap.getMaterial().setEmissiveColor(col);
		ap.getColoringAttributes().setColor(col);
	}
	
	@Override
	public void init(Engine engine) {
		super.init(engine);
		if (Math.abs(system.type.AngleZ[0]-system.type.AngleZ[1]) >= 2*Math.PI) {
			this.AngleZ = MathUtil.getRandomFloat(0.0f, (float) (Math.PI*2.0f));
		}else {
			this.AngleZ = MathUtil.getRandomFloat(system.type.AngleZ);
		}
		
		this.AngularRateZ= MathUtil.getRandomFloat(system.type.AngularRateZ);
		this.AngularDamping = MathUtil.getRandomFloat(system.type.AngularDamping);
		this.VelocityDamping = MathUtil.getRandomFloat(system.type.VelocityDamping);
		this.Lifetime = MathUtil.getRandomInt(system.type.Lifetime);
		this.Size = MathUtil.getRandomFloat(system.type.Size);
		this.SizeRate = MathUtil.getRandomFloat(system.type.SizeRate);
		this.SizeRateDamping = MathUtil.getRandomFloat(system.type.SizeRateDamping);
		this.ColorScale = MathUtil.getRandomInt(system.type.ColorScale);
		
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
		
		if (system.type.PerParticleAttachedSystem != null && !system.type.PerParticleAttachedSystem.equals("")) {
			ParticleSystemType p_type = Main.getParticleSystem(system.type.PerParticleAttachedSystem);
			ParticleSystem psys = new ParticleSystem(p_type);
			psys.setPosition(this.getPosition());
			engine.addEntity(psys);
			psys.init(engine);
		}
		
		//----------------------------
		//Java3D stuff.
		this.tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D trans = new Transform3D();
		//Transform3D scale = new Transform3D();
		double d_scale = (double)this.Size * MathUtil.J3D_COORD_SCALE;
		trans.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
		//trans.setRotation(new AxisAngle4f(0.0f, 0.0f, 1.0f, this.AngleZ));
		trans.setScale(d_scale);
		trans.setTranslation(MathUtil.toJ3DVec(this.getPosition()));
		//System.out.println("Scale:"+d_scale);
		//trans.mul(scale);
		tg.setTransform(trans);
		//--
		this.ap = new Appearance();
		ap.setColoringAttributes(new ColoringAttributes(1.0f, 1.0f, 1.0f, ColoringAttributes.NICEST));
		//-- SET TRANSPARENCY
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
		//---
		ap.setTexture(system.texture);
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		ap.setTextureAttributes(texAttr);
		Color3f col = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f black = new Color3f(0,0,0);
		if (this.system.colors.size() >= 1 ) {
			Vector3f c = system.colors.get(0).getValue();
			col.x = c.x/255.0f;
			col.y = c.y/255.0f;
			col.z = c.z/255.0f;
		}

		Material mat = new Material(col, col, col, col, 0);
		mat.setLightingEnable(false);
		mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
		mat.setCapability(Material.ALLOW_COMPONENT_READ);
		ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
		ap.setMaterial(mat);
		ColoringAttributes colAttr = new ColoringAttributes(col, ColoringAttributes.NICEST);
		colAttr.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
		ap.setColoringAttributes(colAttr);
		ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);

		QuadArray plane = new QuadArray (4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		if (system.type.IsGroundAligned == false) {
			plane.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
			setPlaneCoordinates(plane);
			int t = 1; //texture repeats
			plane.setTextureCoordinate(0, 0, new TexCoord2f(0, 0));
			plane.setTextureCoordinate(0, 1, new TexCoord2f(t, 0));
			plane.setTextureCoordinate(0, 2, new TexCoord2f(t, t));
			plane.setTextureCoordinate(0, 3, new TexCoord2f(0, t));
		
			shape = new OrientedShape3D(plane, ap, OrientedShape3D.ROTATE_ABOUT_POINT, new Point3f(MathUtil.toJ3DVec(this.getPosition())));
			shape.setCapability(OrientedShape3D.ALLOW_POINT_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		}else {
			setPlaneCoordinatesGroundAligned(plane);
			int t = 1;
			plane.setTextureCoordinate(0, 3, new TexCoord2f(0, 0));
			plane.setTextureCoordinate(0, 2, new TexCoord2f(t, 0));
			plane.setTextureCoordinate(0, 1, new TexCoord2f(t, t));
			plane.setTextureCoordinate(0, 0, new TexCoord2f(0, t));
			shape = new Shape3D(plane, ap);
		}
		
		//ColorCube cc = new ColorCube(1.0f);
		//tg.addChild(cc);
		
		billboardGroup = new TransformGroup(); 
	    billboardGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    billboardGroup.setCapability(BranchGroup.ALLOW_DETACH);
      
	    if (system.type.IsGroundAligned) {
		    Transform3D trans2 = new Transform3D();
		    trans2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, this.AngleZ));
		    billboardGroup.setTransform(trans2);
	    }
	    
//	    billboard = new Billboard(billboardGroup, Billboard.ROTATE_ABOUT_POINT, new Point3f(MathUtil.toJ3DVec(this.getPosition())));
//	    BoundingSphere bSphere = new BoundingSphere(new Point3d(0,0,0), Double.MAX_VALUE);
//	    billboard.setSchedulingBounds(bSphere);
//	    billboard.setCapability(BranchGroup.ALLOW_DETACH);
//	    billboard.setAlignmentAxis(0.0f, 0.0f, 1.0f);
	    
	    billboardGroup.addChild(shape);
	    tg.addChild(billboardGroup);
	    
	    bg = new BranchGroup();
	    bg.addChild(tg);
	    //bg.addChild(billboard);
	    bg.setCapability(BranchGroup.ALLOW_DETACH);

	    
	    engine.renderer.particleGroup.addChild(bg);
	    //engine.renderer.particleGroup.addChild(billboard);
	}
	
	private void setPlaneCoordinatesGroundAligned(QuadArray plane) {
		float s = 0.5f; // this.Size * 0.5f * (float)MathUtil.J3D_COORD_SCALE;
		plane.setCoordinate(3, new Point3f(-s, 0, -s));
		plane.setCoordinate(2, new Point3f(s, 0, -s));
		plane.setCoordinate(1, new Point3f(s, 0, s));
		plane.setCoordinate(0, new Point3f(-s, 0, s));
	}

	private void setPlaneCoordinates(QuadArray plane) {		
		
		double angle = this.AngleZ;
		
		if (Math.abs(angle) <= 0.001) {
			float s = 0.5f; // this.Size * 0.5f * (float)MathUtil.J3D_COORD_SCALE;
			plane.setCoordinate(0, new Point3f(-s, -s, 0));
			plane.setCoordinate(1, new Point3f(s, -s, 0));
			plane.setCoordinate(2, new Point3f(s, s,  0));
			plane.setCoordinate(3, new Point3f(-s, s, 0));
		}else {
			angle += Math.PI*0.25;
			
			double r = 0.70710678118654752440084436210485; //sqrt(0.5)			
			double cos = Math.cos(angle);
			double sin = Math.sin(angle);		
			double x = r * cos;
			double y = r * sin;	
			
			plane.setCoordinate(0, new Point3d(-x, -y, 0));
			plane.setCoordinate(1, new Point3d(y, -x, 0));
			plane.setCoordinate(2, new Point3d(x, y,  0));
			plane.setCoordinate(3, new Point3d(-y, x, 0));
		}
	}

	private void addAlphaValue(alphaEntry a) {
		if (a != null) {
			if (alphaValues.size() == 0 || alphaValues.get(alphaValues.size()-1).getKey() < a.frame) {
				alphaValues.add(new SimpleEntry<Integer, Float>(a.frame, MathUtil.getRandomFloat(a.alpha)));
			}
		}
	}
	
	@Override
	public void clear3D() {
		engine.renderer.particleGroup.removeChild(bg);
		//engine.renderer.particleGroup.removeChild(billboard);
	}
}
