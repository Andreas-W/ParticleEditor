package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.FileImageInputStream;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import util.MathUtil;
import net.nikr.dds.DDSImageReader;
import net.nikr.dds.DDSImageReaderSpi;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import entities.Entity;
import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import gui.BrowsePanel;
import gui.EditPanel;
import gui.MainWindow;
import gui.StatusPanel;
import gui.TexturePreviewFrame;

public class Renderer {
	
	public HashMap<String, Texture> TextureMap = new HashMap<String, Texture>();
	
	public Canvas3D canvas;
	public BranchGroup sceneGroup = new BranchGroup();
	public BranchGroup particleGroup = new BranchGroup();
	//public BranchGroup newParticleGroup = new BranchGroup();
	public BranchGroup newParticleGroup = new BranchGroup();
	
	private TransformGroup tgGround = new TransformGroup();
	private Shape3D ground;
	private Background background;
	
	public TexturePreviewFrame texturePreview;
	
	public SimpleUniverse universe;

	//GUI ELEMENTS
	//---------------
	public MainWindow mainWindow;
	public StatusPanel statusPanel;
	public BrowsePanel browsePanel;
	public EditPanel editPanel;

	private float groundOffset;
	
	static {
		IIORegistry registry = IIORegistry.getDefaultInstance();
		registry.registerServiceProvider( new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
		registry.registerServiceProvider( new net.nikr.dds.DDSImageReaderSpi());
	}
	
	public Renderer() {
		init();
	}
	
	public void init()
    {
        ///////////////////////////
        System.setProperty("sun.awt.noerasebackground", "true");

        loadTextures();
        setupScene();
        setupFrame();
        
        this.texturePreview = new TexturePreviewFrame(this);
        texturePreview.setVisible(false);
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run()
//            {
//            	show();
//            }
//        });
    } 
	
	/**
	 * Load all textures used in currently loaded ParticleSystems
	 */
	public void loadTextures() {
		for (String pname : Main.ParticleSystemTypes.keySet()) {
			ParticleSystemType type = Main.ParticleSystemTypes.get(pname);
			String filename = type.ParticleName;
			if (!TextureMap.containsKey(filename.substring(0, filename.length()-4))) {
				Texture texture = loadTexture(filename, pname);			
				if (texture != null) {
					texture.setCapability(Texture.ALLOW_IMAGE_READ);
					texture.getImage(0).setCapability(ImageComponent.ALLOW_IMAGE_READ);
					TextureMap.put(filename.substring(0, filename.length()-4), texture);
					//System.out.println("Loaded Texture "+filename);
				}
			}
		}
		if (editPanel != null) editPanel.particleEditPanel.loadTextureNames();
		//System.out.println("Texture loading finished.");
	}
	
	public Texture loadTexture(String fname, String pname) {
		if (fname.length() >= 5) {
			String filename = fname.substring(0, fname.length()-4);
			File file = new File(Config.TextureFolder1+"/"+filename+".dds");
			if (!file.exists()) file = new File(Config.TextureFolder1+"/"+filename+".tga");
			if (!file.exists()) file = new File(Config.TextureFolder2+"/"+filename+".dds");
			if (!file.exists()) file = new File(Config.TextureFolder2+"/"+filename+".tga");
			try {
				BufferedImage image = ImageIO.read(file);
				TextureLoader loader = new TextureLoader(image);
				Texture texture = loader.getTexture();
				texture.setBoundaryModeS(Texture.WRAP);
				texture.setBoundaryModeT(Texture.WRAP);
				return texture;
			} catch (IOException e) {
				System.out.println("PS '"+pname+"': Couldn't read file: "+fname);
			}
		}
		return null;
	}
	
	public void setupScene() {
		particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		particleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		newParticleGroup.setCapability(BranchGroup.ALLOW_DETACH);
		newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
		canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration()); 		
		universe = new SimpleUniverse(canvas); 
		//SimpleUniverse universe = new SimpleUniverse();
		canvas.getView().setMinimumFrameCycleTime(1000/60);
		canvas.getView().setBackClipDistance(100.0);
		background = new Background(new Color3f(Color.BLACK));
		background.setApplicationBounds(new BoundingSphere(new Point3d(0,0,0), Double.MAX_VALUE));
		background.setCapability(Background.ALLOW_COLOR_WRITE);

		sceneGroup.addChild(background);

		
		//universe.getViewingPlatform().setNominalViewingTransform();
		OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ROTATE);
		orbit.setSchedulingBounds(new BoundingSphere());

		universe.getViewingPlatform().setViewPlatformBehavior(orbit);
		universe.getViewer().getView().setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
		
		resetCamera();
		//-------
		//Set up Objects
		Appearance ap = new Appearance();
		ap.setColoringAttributes(new ColoringAttributes(0.5f, 0.6f, 0.1f, ColoringAttributes.NICEST));
		//ap.setTexture(loadTexture("TLGras01a.tga"));
		try {
			ap.setTexture(new TextureLoader(ImageIO.read(new File("ground.jpg"))).getTexture());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		ap.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
		RenderingAttributes ra = new RenderingAttributes();
		ra.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
		ap.setRenderingAttributes(ra);
		
		
		//int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS; 
		//Box groundBox = new Box(10.0f, 0.1f, 10.0f, primflags, ap);
		QuadArray plane = new QuadArray (4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
		float s = 2.5f;
		int t = 4; //texture repeats
		plane.setCoordinate(3, new Point3f(-s, 0f, -s));
		plane.setCoordinate(2, new Point3f(s, 0f, -s));
		plane.setCoordinate(1, new Point3f(s, 0f, s));
		plane.setCoordinate(0, new Point3f(-s, 0f, s));
		plane.setTextureCoordinate(0, 3, new TexCoord2f(0, 0));
		plane.setTextureCoordinate(0, 2, new TexCoord2f(t, 0));
		plane.setTextureCoordinate(0, 1, new TexCoord2f(t, t));
		plane.setTextureCoordinate(0, 0, new TexCoord2f(0, t));
		
//		Transform3D trans = new Transform3D();
//		trans.setTranslation(new Vector3f(0.0f, -0.5f, 0.0f));
//		TransformGroup tg = new TransformGroup(trans);
//		tg.addChild(groundBox);
		//Sphere sphere = new Sphere(0.5f);
		//group.addChild(sphere);
		ground = new Shape3D(plane, ap);
		ground.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		tgGround.addChild(ground);
		tgGround.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sceneGroup.addChild(tgGround);
		
		//-- Light
//		Color3f light1Color = new Color3f(1.8f, 0.1f, 0.1f);
//		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
//		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0 ,0.0), 100.0);
//		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
//		light1.setInfluencingBounds(bounds);
//		sceneGroup.addChild(light1);
		//-----

		universe.addBranchGraph(sceneGroup);
		universe.addBranchGraph(particleGroup);
	}
	
	public void setupFrame() {
		mainWindow = new MainWindow(this);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//frame.setLayout(new BorderLayout());
        mainWindow.setSize(Config.WindowWidth, Config.WindowHeigth);
        if (Config.Fullscreen) mainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
        
        canvas.setMinimumSize(new Dimension(800, 600));

		// create the status bar panel and shove it down the bottom of the frame
		
		JPanel contentPanel = mainWindow.getContentPane();
		statusPanel = new StatusPanel();
		statusPanel.setPreferredSize(new Dimension(mainWindow.getWidth(), 24));

		editPanel = new EditPanel(this);
		editPanel.setPreferredSize(new Dimension(315, 0));
		browsePanel = new BrowsePanel(this);
		browsePanel.browse_All.fillList(Main.FXListTypes.keySet(), Main.ParticleSystemTypes.keySet());
    
		
		//mainWindow.getMainSplitPane().add(browsePanel);
		//mainWindow.getMainSplitPane().add(canvas);
		//mainWindow.getMainSplitPane().add(editPanel);
		//JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, browsePanel, canvas);
		//JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split1, editPanel);
		//contentPanel.add(split2, BorderLayout.CENTER);
		
		contentPanel.add(browsePanel, BorderLayout.WEST);
		contentPanel.add(canvas, BorderLayout.CENTER);
		contentPanel.add(editPanel, BorderLayout.EAST);
		
		//split1.setDividerSize(4);
//		split1.setDividerLocation(250);
//		split1.setResizeWeight(0);
//		split1.setOneTouchExpandable(true);
//		//split2.setDividerSize(4);
//		split2.setDividerLocation(750);
//		split2.setResizeWeight(1);
//		split2.setOneTouchExpandable(true);
		contentPanel.add(statusPanel, BorderLayout.SOUTH);
		//contentPanel.add(browsePanel, BorderLayout.WEST);
		//contentPanel.add(editPanel, BorderLayout.EAST);
        //contentPanel.add("Center", canvas);

        mainWindow.setVisible(true); 

	}
	
	public void updateStatus(int particleCount, int fps, int minFPS, int maxParticles, int totalFrames) {
		statusPanel.updateStatus(particleCount, fps, minFPS, maxParticles, totalFrames);
		//this.statusLabel.setText(String.format("Particle Count: %4d / FPS: %d", particleCount, fps));
	}
	
	public void resetCamera() {
		TransformGroup viewingTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
		Transform3D viewingTransform = new Transform3D();

		double pitch = 0.9163; // for 37.5° angle
		double r = 3.50; //350.0
		double x = r * Math.sin(pitch);
		double y = r * Math.cos(pitch);
		
	    Point3d eye = new Point3d(x, y, 0);  
	    Point3d center = new Point3d(0,0,0);
	    Vector3d up = new Vector3d(0,1,0);
	    viewingTransform.lookAt(eye, center, up);
	    viewingTransform.invert();
	    viewingTransformGroup.setTransform(viewingTransform);
	}
	

	public void clearParticles() {
		this.particleGroup.removeAllChildren();
	}

	@Deprecated
	public boolean isRunning() {
		return mainWindow.isRunning();
	}
	
	public boolean isReset() {
		if(mainWindow.reset) {
			mainWindow.reset = false;
			return true;
		}else {
			return false;
		}
	}

	public boolean pause() {
		return mainWindow.getTglbtnPlay().isSelected(); //Actually is buttonPause
	}

	public void setGroundOffset(float value) {
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3f(0.0f, (float) -(value * MathUtil.J3D_COORD_SCALE), 0.0f));
		tgGround.setTransform(trans);
	}

	public void setGroundVisible(boolean visible) {
		if (ground != null) {
			ground.getAppearance().getRenderingAttributes().setVisible(visible);
		}
	}

	public void setBackgroundColor(Color color) {
		background.setColor(new Color3f(color));
	}

	public boolean isInParticleMode() {
		return this.browsePanel.getActiveTab().getTpane_browse().getSelectedIndex()==1;
	}

	/**
	 * This is called when a list item in the browse tab is selected
	 * This changes the active FX type and updates all necessary elements
	 */
	public void updateActiveFX(FXListType type, String name) {
		Main.activeFXListType = type;
		if (type.isTemporary()) {
			editPanel.setFXTextAuto("<"+name+">");
			editPanel.setFXEditsEnabled(false);
		}else {
			editPanel.setFXTextAuto(name);
			editPanel.setFXEditsEnabled(true);
		}
		editPanel.updateFXGUI();
		if (type.isTemporary()) {
			editPanel.cb_ParticleSystems.setSelectedItem(name);
		}
		editPanel.updateFXCode();
	}

	/**
	 * This changes the active ParticleSystem type and updates all necessary elements
	 */
	public void updateActiveParticle(ParticleSystemType type,
			String name) {
		Main.activeParticleSystemType = type;
		editPanel.updateParticleGUI();
		editPanel.updateParticleCode();
	}
}
