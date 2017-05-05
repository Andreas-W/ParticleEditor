package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	//Config file properties
	public static int WindowWidth=1280;
	public static int WindowHeigth=720;
	public static boolean Fullscreen=true;
	public static String ParticleSystemFile = "ParticleSystem.txt";
	public static String FXListFile = "FXList.txt";
	public static String DefaultFolder = "./";
	public static String DefaultFolderWorkingSet = "./";
	
	//Texture loading
	public static String TextureFolder1 = "./";
	public static String TextureFolder2 = "./";
	public static String TextureLoadPattern = null;
	
	//Local config
	public static String currentFXListFile;
	public static String currentParticleSystemFile;
	
	//View Config
	public static String GroundTexture = "ground.jpg";
	public static float GroundSize = 500.00f;
	public static float GroundResolution = 20.f;
	public static byte GroundType = 1; //0 = Grid, 1 = Textured
	public static int GridColor = 0;

	
	public static int BGColor = 0;

	
	public static void readConfigFile(String filepath) {
		File configFile = new File(filepath);
		 
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		 
		    WindowWidth = Integer.parseInt(props.getProperty("WindowWidth", "1280"));
		    WindowHeigth = Integer.parseInt(props.getProperty("WindowHeight", "720"));
		    Fullscreen = Boolean.parseBoolean(props.getProperty("Fullscreen", "true" ));
		    ParticleSystemFile=props.getProperty("ParticleSystemFile", "ParticleSystem.txt");
		    FXListFile=props.getProperty("FXListFile","FXList.txt");
		    DefaultFolder=props.getProperty("DefaultFolder", ".");
		    DefaultFolderWorkingSet = props.getProperty("DefaultFolderWorkingSet", ".");
		    TextureFolder1=props.getProperty("TextureFolder1", ".");
		    TextureFolder2=props.getProperty("TextureFolder2", ".");
		    TextureLoadPattern = props.getProperty("TextureLoadPattern", null);
		    
		    GroundTexture = props.getProperty("GroundTexture", "ground.jpg");
		    GroundSize = Float.parseFloat(props.getProperty("GroundSize", "500"));
		    GroundResolution = Float.parseFloat(props.getProperty("GroundResolution", "20"));
		    GroundType = Byte.parseByte(props.getProperty("GroundType", "1"));

		    GridColor = Integer.parseInt(props.getProperty("GridColor", "-8355712"));
		    
		    BGColor = Integer.parseInt(props.getProperty("BGColor", "-4144960"));
		    
		    reader.close();
		} catch (IOException ex) {
		    writeConfigFile(filepath);
		}
	}

	public static void writeConfigFile(String filepath) {
		File configFile = new File(filepath);
		 
		try {
		    Properties props = new Properties();
		    
		    props.setProperty("WindowWidth", ""+WindowWidth);
		    props.setProperty("WindowHeight", ""+WindowWidth);
		    props.setProperty("Fullscreen", ""+Fullscreen);
		    props.setProperty("ParticleSystemFile", ParticleSystemFile);
		    props.setProperty("FXListFile", FXListFile);
		    props.setProperty("DefaultFolder", DefaultFolder);
		    props.setProperty("DefaultFolderWorkingSet", DefaultFolderWorkingSet);
		    props.setProperty("TextureFolder1", TextureFolder1);
		    props.setProperty("TextureFolder2", TextureFolder2);
		    if (TextureLoadPattern != null) props.setProperty("TextureLoadPattern", TextureLoadPattern);
		    
		    props.setProperty("GroundTexture", GroundTexture);
		    props.setProperty("GroundSize", ""+GroundSize);
		    props.setProperty("GroundResolution", ""+GroundResolution);
		    props.setProperty("GroundType", ""+GroundType);
		    
		    props.setProperty("GridColor", ""+GridColor);
		    
		    props.setProperty("BGColor", ""+BGColor);
		    
		    
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "Particle Editor Config");
		    writer.close();
		    
		} catch (IOException ex) {
		    // I/O error
		}
	}
}
