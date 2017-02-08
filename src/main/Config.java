package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	public static int WindowWidth=1280;
	public static int WindowHeigth=720;
	public static boolean Fullscreen=true;
	public static String ParticleSystemFile = "ParticleSystem.txt";
	public static String FXListFile = "FXList.txt";
	public static String DefaultFolder = "./";
	public static String TextureFolder1 = "./";
	public static String TextureFolder2 = "./";
	
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
		    TextureFolder1=props.getProperty("TextureFolder1", ".");
		    TextureFolder2=props.getProperty("TextureFolder2", ".");
		 
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
		    props.setProperty("TextureFolder1", TextureFolder1);
		    props.setProperty("TextureFolder2", TextureFolder2);  
		    
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "Particle Editor Config");
		    writer.close();
		    
		} catch (IOException ex) {
		    // I/O error
		}
	}
}
