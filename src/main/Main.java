package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JWindow;

import entities.FXList;
import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import entitytypes.FXListType.ParticleSystemEntry;
import gui.MainWindow;
import parser.Parser;
import util.Util;

public class Main {

	public static final String configFilePath = "./config.properties";

	public static int FPS = 30; //30;
	public static HashMap<String, ParticleSystemType> ParticleSystemTypes = new HashMap<String, ParticleSystemType>();
	public static HashMap<String, FXListType> FXListTypes = new HashMap<String, FXListType>();
	
	public static HashMap<String, ParticleSystemType> work_ParticleSystemTypes = new HashMap<String, ParticleSystemType>();
	public static HashMap<String, FXListType> work_FXListTypes = new HashMap<String, FXListType>();
	
	public static ArrayList<String> FXListNames = new ArrayList<String>(); //Keep update with above hashset;
	public static ArrayList<String> ParticleSystemNames = new ArrayList<String>(); //Keep update with above hashset;
	
	public static FXListType activeFXListType; //Currently active FX Type
	public static ParticleSystemType activeParticleSystemType;
	
	public static void main(String[] args) {
		
		Config.readConfigFile(configFilePath);
		
		Locale.setDefault(Locale.US);
		
		 JWindow w = Util.showLoadingWindow(null);
		
		//boolean loaded = loadFiles("C:/Games/Command & Conquer Generalz GIT/Data/INI/ParticleSystem.ini", "C:/Games/Command & Conquer Generalz GIT/Data/INI/FXList.ini");
		boolean loaded = loadFiles(Config.ParticleSystemFile, Config.FXListFile);
		
		if (loaded) {

			final Renderer renderer = new Renderer();
			//renderer.loadTextures();
			
			w.setVisible(false);
			w.dispose();
			renderer.mainWindow.toFront();

			//engine.repeat = false;
			Thread looper = new Thread(new Runnable() {			
				@Override
				public void run() {
					boolean active = true; //Always true
					boolean run = true;
					while(active) {
						if (run || renderer.isReset()) {
							try {
								renderer.mainWindow.running = true;
								Engine engine = new Engine(renderer);
								//String FX = "FX_TerrorMortarSiteWeaponExplosion";
								FXListType type = Main.activeFXListType;
//								boolean particleMode = renderer.isInParticleMode();
//								if (particleMode) {
//									//String part = (String) renderer.browsePanel.getActiveTab().getA_lst_Particles().getSelectedValue();
//									//if (part == null) run = false;
//									type = FXListType.getFXTypeFromParticle(part);
//								}else {
//									String FX = (String) renderer.browsePanel.getActiveTab().getA_lst_FX().getSelectedValue();
//									if (FX == null) run = false;
//									else type = getFXList(FX);
//								}
								if (type != null) {
									engine.addEntity(new FXList(type));
									engine.start();
									engine.join();
									//System.out.println(Util.timerLog.toString());
									//Util.clearTimer();
									renderer.clearParticles();
									run = renderer.mainWindow.getTglbtnAutoplay().isSelected();
								}else {
									run = false;
								}
							} catch (InterruptedException e) {
								active = false;
							}
						} else {
							run = renderer.mainWindow.getTglbtnAutoplay().isSelected();
						}
					}
				}
			});
			looper.start();
			
			
		}
		
		
//		for (String key : ParticleSystemTypes.keySet()) {
//			ParticleSystemType type = ParticleSystemTypes.get(key);
//			System.out.println(key + " "+ type.toString());
//		}		
//		for (String key : FXListTypes.keySet()) {
//			FXListType type = FXListTypes.get(key);
//			System.out.println(key + " "+ type.toString());
//			for (ParticleSystemEntry entry : type.ParticleSystems) {
//				System.out.println(" -- "+ entry.Name);
//			}
//		}
		
	}
	
	public static boolean loadFiles(String particleSystems, String FXLists) {
		try {
			ParticleSystemTypes = new HashMap<String, ParticleSystemType>();
			FXListTypes = new HashMap<String, FXListType>();
			
			Parser.parseParticleSystemINI(particleSystems, ParticleSystemTypes);
			Parser.parseFXListINI(FXLists, FXListTypes);
			
			updateParticleSystemNames();
			updateFXListNames();
			
			activeParticleSystemType = ParticleSystemTypes.get(ParticleSystemNames.get(0));
			activeFXListType = FXListTypes.get(FXListNames.get(0));
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	
	public static void updateFXListNames() {
		FXListNames = new ArrayList<String>();
		FXListNames.addAll(FXListTypes.keySet());
		Collections.sort(FXListNames);
	}

	public static void updateParticleSystemNames() {
		ParticleSystemNames = new ArrayList<String>();
		ParticleSystemNames.addAll(ParticleSystemTypes.keySet());
		Collections.sort(ParticleSystemNames);
	}

	public static ParticleSystemType getParticleSystem(String name) {
		if (ParticleSystemTypes.containsKey(name)) {
			return ParticleSystemTypes.get(name);
		}else {
			System.out.println("Could not find ParticleSystem "+name);
			return null;
		}
	}
	
	public static FXListType getFXList(String name) {
		if (FXListTypes.containsKey(name)) {
		return FXListTypes.get(name);
		}else {
			System.out.println("Could not find FXList "+name);
			return null;
		}
		
	}

}
