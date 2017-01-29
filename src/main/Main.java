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

public class Main {
	
	public static final String TEXTURE_PATH = "./textures/";

	public static int FPS = 30; //30;
	public static HashMap<String, ParticleSystemType> ParticleSystemTypes = new HashMap<String, ParticleSystemType>();
	public static HashMap<String, FXListType> FXListTypes = new HashMap<String, FXListType>();
	
	public static void main(String[] args) {
		
		
		boolean loaded = loadFiles("testparticle.txt", "testFX.txt");
		
		if (loaded) {
			final Renderer renderer = new Renderer();
			renderer.loadTextures();


			//engine.repeat = false;
			Thread looper = new Thread(new Runnable() {			
				@Override
				public void run() {
					boolean run = true;
					while(run) {
						try {
							Engine engine = new Engine(renderer);
							String FX = "MeteorImpactExplosionLarge";
							if (FXListTypes.containsKey(FX)) {
								engine.addEntity(new FXList(FXListTypes.get(FX)));
								engine.start();
								engine.join();
								Thread.sleep(1000);
							}else {
								System.out.println("Could not find FXList "+FX);
								run = false;
							}
						} catch (InterruptedException e) {
							run = false;
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
		return ParticleSystemTypes.get(name);
	}
	
	public static FXListType getFXList(String name) {
		return FXListTypes.get(name);
	}

}
