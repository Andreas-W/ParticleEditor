package entities;

import javax.media.j3d.Appearance;
import javax.media.j3d.BadTransformException;
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

import com.sun.j3d.utils.universe.ViewingPlatform;

import entitytypes.ParticleSystemType.e_Shader;
import util.MathUtil;
import main.Engine;

public class StreakParticle extends ParticleEntity{

	public Shape3D shape;
	
	public StreakParticle next;
	public StreakParticle prev;
	
	public StreakParticle(ParticleSystem sys, Vector3d position,
			Vector3d offset, Vector3d velocity, float startSize) {
		super(sys, position, offset, velocity, startSize);
	}

	@Override
	public void update() {
		super.update();
		//---------------------
		//Update J3D stuff

		if (prev == null) return; //Don't draw if it's the first particle in a trail
			
//		Transform3D trans = new Transform3D();
//		Vector3d transVector = MathUtil.toJ3DVec(this.getPosition());
//		double scalefactor = Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001);
//		trans.set(transVector);
//		try {
//			tg.setTransform(trans);		
//		} catch (BadTransformException e) {
//			e.printStackTrace(System.err);
//		}
		if (prev != null) {
			setPlaneCoordinates((QuadArray) shape.getGeometry());
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
		this.ap = new Appearance();
		if (prev == null) return; //Don't draw if it's the first particle in a trail
		
//		double scalefactor = Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001);
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

		QuadArray plane = new QuadArray (4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		
		plane.setCapability(QuadArray.ALLOW_COORDINATE_WRITE);
		plane.setCapability(QuadArray.ALLOW_COORDINATE_READ);
		setPlaneCoordinates(plane);
		int t = 1; //texture repeats
		plane.setTextureCoordinate(0, 0, new TexCoord2f(0, 0));
		plane.setTextureCoordinate(0, 1, new TexCoord2f(t, 0));
		plane.setTextureCoordinate(0, 2, new TexCoord2f(t, t));
		plane.setTextureCoordinate(0, 3, new TexCoord2f(0, t));
	
		shape = new Shape3D(plane, ap);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

		 bg = new BranchGroup();
		 bg.addChild(shape);
		 bg.setCapability(BranchGroup.ALLOW_DETACH);
		 engine.renderer.newParticleGroup.addChild(bg);
	}

	private void setPlaneCoordinates(QuadArray plane) {
		
		Vector3d p2 = MathUtil.toJ3DVec(this.getPosition());
		Vector3d p1 = MathUtil.toJ3DVec(this.prev.getPosition());
		
		//double scalefactor = Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001);
		
		ViewingPlatform viewPlatform = engine.renderer.universe.getViewingPlatform();
	    TransformGroup viewTransform = viewPlatform.getViewPlatformTransform();
	    Transform3D t3d = new Transform3D();
	    viewTransform.getTransform(t3d);
	    Vector3d viewPos = new Vector3d(0,0,0);
	    t3d.get(viewPos);
	    
	    // compute vector from eye to point
	    Vector3d vec1 = new Vector3d();
	    vec1.sub(viewPos, p1);
	        
	    // compute vector along line length
	    Vector3d vec2 = new Vector3d();
	    vec2.sub(p1,p2);
	        
	    // cross product
	    Vector3d vec = new Vector3d();
	    vec.cross(vec1, vec2);
	    vec.normalize();
	    
	    Vector3d vec3 = new Vector3d(vec);
	    Vector3d vec4 = new Vector3d(vec);
	    
	    vec3.scale(Math.max((double)this.prev.Size * MathUtil.J3D_COORD_SCALE,0.001));
	    vec4.scale(Math.max((double)this.Size * MathUtil.J3D_COORD_SCALE,0.001));
	    
	    // compute vertices
	    Vector3d v1 = new Vector3d(p1);
	    Vector3d v2 = new Vector3d(p1);
	    v1.sub(vec3);
	    v2.add(vec3);
	    
	    Vector3d v3 = new Vector3d(p2);
	    Vector3d v4 = new Vector3d(p2);
	    v3.add(vec4);
	    v4.sub(vec4);
	    
	    if (prev.prev != null) {
		    Point3d v1_ = new Point3d();
		    Point3d v2_ = new Point3d();
	    	
		    QuadArray prevPlane = (QuadArray)prev.shape.getGeometry();
		   
		    prevPlane.getCoordinate(3, v1_);
		    prevPlane.getCoordinate(2, v2_);
		    
		    /* THIS IS ACTUALLY CORRECT, BUT IT'S WAY TOO SLOW SO WE DON'T USE IT
		    v1.interpolate(v1_, 0.5);
		    v2.interpolate(v2_, 0.5);
		    
		    prevPlane.setCoordinate(3, new Point3d(v1));
		    prevPlane.setCoordinate(2, new Point3d(v2));
		    
		     */
	    }
	    
	    plane.setCoordinate(0, new Point3d(v1));
	    plane.setCoordinate(1, new Point3d(v2));
	    plane.setCoordinate(2, new Point3d(v3));
	    plane.setCoordinate(3, new Point3d(v4)); 	    
	}
}
