package entities;

import java.awt.Color;
import java.awt.event.TextEvent;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.media.j3d.Appearance;
import javax.media.j3d.BadTransformException;
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
import javax.vecmath.Matrix3d;
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
import entitytypes.ParticleSystemType.e_VolumeType;
import util.MathUtil;
import util.Util;
import util.VolumeTypes;
import main.Engine;
import main.Main;
import main.Renderer;

public class Particle extends ParticleEntity{
	
	//Java3D stuff
	public TransformGroup tg;
	public Shape3D shape;
	//public Billboard billboard;
	TransformGroup billboardGroup;
	
	public Particle(ParticleSystem sys, Vector3d position, Vector3d offset, Vector3d velocity, float startSize) {
		super(sys, position, offset, velocity, startSize);
	}
	
	@Override
	public void update() {
		super.update();
		//---------------------
		//Update J3D stuff
		if (system.type.IsGroundAligned == false) {
			if (this.AngleZ != 0) {
				setPlaneCoordinates((QuadArray) shape.getGeometry());
			}
		}else {
			if (this.AngleZ != 0) {
				Transform3D trans2 = new Transform3D();
				trans2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, this.AngleZ));
				billboardGroup.setTransform(trans2);
			}
		}
			
		Transform3D trans = new Transform3D();
		Transform3D scale = new Transform3D();
		Vector3d transVector = MathUtil.toJ3DVec(this.getPosition());
		double scalefactor = Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001);
		trans.set(transVector);
		scale.set(scalefactor);
		trans.mul(scale);
		try {
			tg.setTransform(trans);		
		} catch (BadTransformException e) {
			e.printStackTrace(System.err);
		}

		if (system.type.Shader == e_Shader.ALPHA || system.type.Shader == e_Shader.ALPHA_TEST) {
			ap.getTransparencyAttributes().setTransparency(1.0f-this.ParticleAlpha);
		}

		Color3f col = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f c = this.ParticleColor;
		col.x = c.x/255.0f;
		col.y = c.y/255.0f;
		col.z = c.z/255.0f;

		ap.getColoringAttributes().setColor(col);

	}
	
	@Override
	public void init(Engine engine) {
		super.init(engine);
		//----------------------------
		//Java3D stuff.
		this.tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D trans = new Transform3D();
		Transform3D scale = new Transform3D();
		Vector3d transVector = MathUtil.toJ3DVec(this.getPosition());
		double scalefactor = Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001);
		trans.set(transVector);
		scale.set(scalefactor);
		trans.mul(scale);
		try {
			tg.setTransform(trans);		
		} catch (BadTransformException e) {
			e.printStackTrace(System.err);
		}
		
		this.ap = new Appearance();
		ap.setColoringAttributes(new ColoringAttributes(1.0f, 1.0f, 1.0f, ColoringAttributes.NICEST));
		//-- SET TRANSPARENCY
		setUpTransparency();
		//---
		ap.setTexture(system.texture);
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		//texAttr.setCombineRgbMode(TextureAttributes.COMBINE_ADD_SIGNED);
		ap.setTextureAttributes(texAttr);
		Color3f col = new Color3f(1.0f, 1.0f, 1.0f);
		if (this.system.colors.size() >= 1 ) {
			Vector3f c = system.colors.get(0).getValue();
			col.x = c.x/255.0f;
			col.y = c.y/255.0f;
			col.z = c.z/255.0f;
		}
		//col = new Color3f(1.0f, 1.0f, 1.0f);
		Material mat = new Material(col, col, col, col, 0);
		mat.setLightingEnable(false);
		mat.setCapability(Material.ALLOW_COMPONENT_WRITE);
		mat.setCapability(Material.ALLOW_COMPONENT_READ);
		ap.setCapability(Appearance.ALLOW_MATERIAL_READ);
		ap.setMaterial(mat);
		ColoringAttributes colAttr = new ColoringAttributes(col, ColoringAttributes.FASTEST);
		colAttr.setCapability(ColoringAttributes.ALLOW_COLOR_WRITE);
		ap.setColoringAttributes(colAttr);
		ap.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_READ);

	//	Util.stopTimer("ParticleInit_Appearance");
		
	//	Util.startTimer("ParticleInit_Quad");
		
		QuadArray plane = new QuadArray (4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		if (system.type.IsGroundAligned == false) {
			plane.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
			setPlaneCoordinates(plane);
			int t = 1; //texture repeats
			plane.setTextureCoordinate(0, 0, new TexCoord2f(0, 0));
			plane.setTextureCoordinate(0, 1, new TexCoord2f(t, 0));
			plane.setTextureCoordinate(0, 2, new TexCoord2f(t, t));
			plane.setTextureCoordinate(0, 3, new TexCoord2f(0, t));
		
			shape = new OrientedShape3D(plane, ap, OrientedShape3D.ROTATE_ABOUT_POINT, new Point3f(0.0f, 0.0f, 0.0f));// new Point3f(MathUtil.toJ3DVec(this.getPosition())));
			shape.setCapability(OrientedShape3D.ALLOW_POINT_WRITE);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
			shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		}else {
			plane.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
			setPlaneCoordinatesGroundAligned(plane);
			int t = 1;
			plane.setTextureCoordinate(0, 3, new TexCoord2f(0, 0));
			plane.setTextureCoordinate(0, 2, new TexCoord2f(t, 0));
			plane.setTextureCoordinate(0, 1, new TexCoord2f(t, t));
			plane.setTextureCoordinate(0, 0, new TexCoord2f(0, t));
			shape = new Shape3D(plane, ap);
		}
	
		billboardGroup = new TransformGroup(); 
	    billboardGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    billboardGroup.setCapability(BranchGroup.ALLOW_DETACH);
      
	    if (system.type.IsGroundAligned) {
		    Transform3D trans2 = new Transform3D();
		    trans2.setRotation(new AxisAngle4f(0.0f, 1.0f, 0.0f, this.AngleZ));
		    billboardGroup.setTransform(trans2);
	    }

	    billboardGroup.addChild(shape);
	    tg.addChild(billboardGroup);
	    
	    bg.addChild(tg);
	    bg.setCapability(BranchGroup.ALLOW_DETACH);

	    engine.renderer.newParticleGroup.addChild(bg);

	}
	
	private void setPlaneCoordinatesGroundAligned(QuadArray plane) {
		float s = 1f; // this.Size * 0.5f * (float)MathUtil.J3D_COORD_SCALE;
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
}
