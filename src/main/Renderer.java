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
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javax.media.j3d.LineArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
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
import util.Util;
import net.nikr.dds.DDSImageReader;
import net.nikr.dds.DDSImageReaderSpi;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
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
	
	public TransformGroup tgModel = new TransformGroup();
	
	private TransformGroup tgGround = new TransformGroup();
	private Shape3D ground;
	private Background background;
	
	public TexturePreviewFrame texturePreview;
	
	public SimpleUniverse universe;
	
	public HashMap<String, Scene> models = new HashMap<String, Scene>();

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
		//Load all currently used textures
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
		if (Config.TextureLoadPattern != null && !Config.TextureLoadPattern.equals("")) {
			loadTexturesFromDirMatching(new File(Config.TextureFolder1));
			loadTexturesFromDirMatching(new File(Config.TextureFolder2));
		}
		
		
		if (editPanel != null) editPanel.particleEditPanel.loadTextureNames();
		//System.out.println("Texture loading finished.");
	}
	
	private void loadTexturesFromDirMatching(File dir) {
		final String pattern = Config.TextureLoadPattern+"\\.((tga)|(TGA)|(dds)|(DDS))";		
	    File[] files = dir.listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return name.matches(pattern);
	        }
	    });
	    for (File file : files) {
	    	String fname = Util.getTrimmedFilename(file).toLowerCase();
	    	if (!TextureMap.containsKey(fname)) {
		    	try {
					BufferedImage image = ImageIO.read(file);
					TextureLoader loader = new TextureLoader(image);
					Texture texture = loader.getTexture();
					texture.setBoundaryModeS(Texture.WRAP);
					texture.setBoundaryModeT(Texture.WRAP);
					TextureMap.put(fname,  texture);
				} catch (IOException e) {
					Util.printToLog("Couldn't read texture file: "+fname);
				}
	    	}
	    }
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
				Util.printToLog("PS '"+pname+"': Couldn't read file: "+fname);
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
		background = new Background(new Color3f(new Color(Config.BGColor)));
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
		ground = new Shape3D();
		ground.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		ground.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		ground.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		Appearance ap = new Appearance();
		ap.setColoringAttributes(new ColoringAttributes(0.5f, 0.6f, 0.1f, ColoringAttributes.NICEST));
		ap.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
		RenderingAttributes ra = new RenderingAttributes();
		ra.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
		ap.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		ap.setRenderingAttributes(ra);
		ground.setAppearance(ap);
		
		setupGround();
		setupModels(); //TODO
		
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
	
	public void setupModels() {
		
		File dir = new File("models");
		File[] list = dir.listFiles(new FilenameFilter() { 
            public boolean accept(File dir, String filename)
                 { return filename.endsWith(".obj"); }
		});
		
		for (File f : list) {
		    try {
		    	URL url = Paths.get(f.getAbsolutePath()).toUri().toURL();
				ObjectFile loader = new ObjectFile(ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY );//ObjectFile.RESIZE);
				//Scene model = loader.load(new FileReader(modelPath));
		    	Scene model = loader.load(url);
		    	model.getSceneGroup().setCapability(BranchGroup.ALLOW_DETACH);
				String name = f.getName().substring(0, f.getName().length()-4);
				models.put(name, model);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IncorrectFormatException e) {
				e.printStackTrace();
			} catch (ParsingErrorException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 			
		}
		
		Transform3D tModel = new Transform3D();
		tModel.setScale(MathUtil.J3D_COORD_SCALE);		
		tgModel = new TransformGroup(tModel);
		tgModel.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		tgModel.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tgModel.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
	    
		this.tgGround.addChild(tgModel);

	}
	
	public void showModel(String model) {
		this.tgModel.removeAllChildren();
		if (models.containsKey(model)) {
			this.tgModel.addChild(models.get(model).getSceneGroup());
		}
	}

	public void setupGround() {
		
		if (Config.GroundType == 1) { //Textured		
			try {
				String tex = Config.GroundTexture;			
				ground.getAppearance().setTexture(new TextureLoader(ImageIO.read(new File(tex))).getTexture());
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			QuadArray plane = new QuadArray (4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
			float s = (float) (Config.GroundSize*MathUtil.J3D_COORD_SCALE*0.5f);
			float t = (Config.GroundSize/Config.GroundResolution)*0.5f; //texture repeats
			plane.setCoordinate(3, new Point3f(-s, 0f, -s));
			plane.setCoordinate(2, new Point3f(s, 0f, -s));
			plane.setCoordinate(1, new Point3f(s, 0f, s));
			plane.setCoordinate(0, new Point3f(-s, 0f, s));
			plane.setTextureCoordinate(0, 3, new TexCoord2f(0, 0));
			plane.setTextureCoordinate(0, 2, new TexCoord2f(t, 0));
			plane.setTextureCoordinate(0, 1, new TexCoord2f(t, t));
			plane.setTextureCoordinate(0, 0, new TexCoord2f(0, t));
			ground.removeAllGeometries();
			ground.addGeometry(plane);
		
		}else if (Config.GroundType == 0) {
			int num = (int)(((Config.GroundSize)/Config.GroundResolution)*0.5f);
			int count = 8*num+4;
			float maxDist = (float) (Config.GroundSize*MathUtil.J3D_COORD_SCALE*0.5);
			float distance = maxDist/(float)num;
			LineArray grid=new LineArray(count,LineArray.COORDINATES|LineArray.COLOR_3);
			Color3f c = new Color3f(new Color(Config.GridColor));
			
			int i = 0;
			for (int j = 0; j < num; j++) {
				float f = (j+1) * distance;
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(f, 0.0f, maxDist));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(f, 0.0f, -maxDist));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(maxDist, 0.0f, f));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(-maxDist, 0.0f, f));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(-f, 0.0f, maxDist));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(-f, 0.0f, -maxDist));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(maxDist, 0.0f, -f));
				grid.setColor(i, c);
				grid.setCoordinate(i++, new Point3f(-maxDist, 0.0f, -f));
			}
			grid.setColor(count-2, c);
			grid.setCoordinate(count-2, new Point3f(0, 0.0f, maxDist));
			grid.setColor(count-1, c);
			grid.setCoordinate(count-1, new Point3f(0, 0.0f, -maxDist));
			grid.setColor(count-3, c);
			grid.setCoordinate(count-3, new Point3f(maxDist, 0.0f, 0));
			grid.setColor(count-4, c);
			grid.setCoordinate(count-4, new Point3f(-maxDist, 0.0f, 0));
			ground.removeAllGeometries();
			ground.addGeometry(grid);
		}

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
		double z = r * Math.sin(pitch);
		double y = r * Math.cos(pitch);
		
	    Point3d eye = new Point3d(0, y, z);  
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
	
	public void addNewParticleToLists(ParticleSystemType type, String name) {
		editPanel.addToParticleSelection(name);
		
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) editPanel.particleEditPanel.getCbAttachedSystem().getModel();
		if (model.getIndexOf(name) == -1) editPanel.particleEditPanel.getCbAttachedSystem().addItem(name);
		
		DefaultComboBoxModel<String> model2 = (DefaultComboBoxModel<String>) editPanel.particleEditPanel.getCbSlavedSystem().getModel();
		if (model2.getIndexOf(name) == -1) editPanel.particleEditPanel.getCbSlavedSystem().addItem(name);
		
	}
}
