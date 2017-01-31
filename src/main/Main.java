package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import entities.FXList;
import entitytypes.FXListType;
import entitytypes.ParticleSystemType;
import entitytypes.FXListType.ParticleSystemEntry;
import parser.Parser;
import util.Util;

public class Main {
	
	public static final String TEXTURE_PATH = "./textures/";

	public static int FPS = 30; //30;
	public static HashMap<String, ParticleSystemType> ParticleSystemTypes = new HashMap<String, ParticleSystemType>();
	public static HashMap<String, FXListType> FXListTypes = new HashMap<String, FXListType>();
	
	public static void main(String[] args) {
		
		//boolean loaded = loadFiles("C:/Games/Command & Conquer Generalz GIT/Data/INI/ParticleSystem.ini", "C:/Games/Command & Conquer Generalz GIT/Data/INI/FXList.ini");
		boolean loaded = loadFiles("testparticle.txt", "testFX.txt");
		
		if (loaded) {
			final Renderer renderer = new Renderer();
			renderer.loadTextures();
			

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
								String FX = (String) renderer.browsePanel.getA_lst_FX().getSelectedValue();
								FXListType type = getFXList(FX);
								if (type != null) {
									engine.addEntity(new FXList(type));
									engine.start();
									engine.join();
									//System.out.println(Util.timerLog.toString());
									//Util.clearTimer();
									renderer.clearParticles();
									run = renderer.mainWindow.getTglbtnAutoplay().isSelected();
								}else {
									//run = false;
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
			Parser.parseParticleSystemINI(particleSystems, ParticleSystemTypes);
			Parser.parseFXListINI(FXLists, FXListTypes);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}		
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
