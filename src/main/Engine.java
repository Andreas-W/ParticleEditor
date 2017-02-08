package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.media.j3d.BranchGroup;

import util.Util;
import entities.Entity;
import entities.FXList;
import entities.Particle;
import entities.ParticleEntity;
import entities.ParticleSystem;


public class Engine extends Thread {
	
	public Renderer renderer;
	public int maxTime = 1500; //150 frames = 5 Seconds
	public double maxFrametime = 1.0 / (double)Main.FPS;
	//public boolean repeat = false;
	//public long globalTimer = 0;
	boolean run;
	
	int maxParticles = 0;
	int minFPS = -1;
	
	public ArrayList<Entity> activeEntities = new ArrayList<Entity>();
	
	public Engine(Renderer renderer) {
		this.renderer = renderer;
	}
	
	@Override
	public void run() {
		try {
			mainLoop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mainLoop() throws InterruptedException {
		long lastTime = System.currentTimeMillis();
		run = true;
		int frameCounter = 0;
		maxParticles = 0;
		minFPS = -1;
		while (run && !renderer.isReset())
		{
			maxFrametime = 1.0 / (double)Main.FPS;
			
			boolean reset = false;
			while (renderer.pause() && ((reset = renderer.isReset()) == false)) {
				sleep((long)((maxFrametime)*1000.0));
				lastTime = System.currentTimeMillis();
			}
			if (reset) run = false;
			
			initObjects();
			
			
			long time = System.currentTimeMillis();

			double deltaT = (time - lastTime) * 0.001;	//in seconds
			lastTime = time;

			if (deltaT < maxFrametime) {
				sleep((long)((maxFrametime-deltaT)*1000.0));
				deltaT = maxFrametime;
				lastTime = lastTime+(long) (maxFrametime*1000.0);
			}
			frameCounter++;
			//globalTimer++;

			//Perform gamestep
			gameStep(frameCounter, deltaT);
			
			if (frameCounter >= maxTime || activeEntities.size() == 0) {
				run = false;
			}
		}
		renderer.updateStatus(0, -1, -1, -1, -1);
	}
	
	private void initObjects() {
		ArrayList<Entity> entities = new ArrayList<Entity>(activeEntities);
		//Iterate through copy of Entity list for concurrency reasons
		for (Entity entity : entities) {
			if (entity.engine == null) {
				entity.init(this);
			//	System.out.println(Util.timerLog.toString());
			//	Util.clearTimer();
			}
		}
	}

	//We actually don't need the deltaT, since all calculations are frame-wise
	private void gameStep(int frameCounter, double deltaT) {
		int c_fx = 0;
		int c_sys = 0;
		int c_part = 0;
		ArrayList<Entity> entities = new ArrayList<Entity>(activeEntities);
		//Iterate through copy of Entity list for concurrency reasons
		//Util.startTimer("GameStep"+frameCounter);
		//renderer.sceneGroup.removeChild(renderer.particleGroup);
		
		for (Entity entity : entities) {
			if (entity instanceof ParticleEntity) c_part++;
			else if (entity instanceof ParticleSystem) c_sys++;
			else if (entity instanceof FXList) c_fx++;

			if (c_part > maxParticles) maxParticles = c_part;
			int fps = (int) (1.0 / deltaT);
			if (minFPS == -1 || fps < minFPS) minFPS = fps;
				
			if (entity.engine == null) {
				entity.init(this);
			}
			entity.update();
			if (entity.dead) {
				activeEntities.remove(entity);
				entity.clear3D();
			}
			
			renderer.updateStatus(c_part, fps, minFPS, maxParticles, frameCounter);
		}
		
		renderer.particleGroup.addChild(renderer.newParticleGroup);
		renderer.newParticleGroup = new BranchGroup();
		renderer.newParticleGroup.setCapability(BranchGroup.ALLOW_DETACH);
		renderer.newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		renderer.newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		renderer.newParticleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
//		if (frameCounter % 1 == 0 && Util.timerLog.length() > 0) {
//			System.out.println(Util.timerLog.toString());
//			Util.clearTimer();
//		}
		
//		Util.stopTimer("GameStep"+frameCounter);
//		Util.timerLog.append(String.format("GameStep%d: Frametime= %.3f\n",frameCounter, deltaT*1000));
//		if (frameCounter % 10 == 0) {
//			System.out.println(Util.timerLog.toString());
//			Util.clearTimer();
//		}
//		if (frameCounter % 2 == 0) {
//			System.out.printf("--- Active Entities ---\nFXLists: %d\nParticleSystems: %d\nParticles: %d\n\n",c_fx, c_sys, c_part);
//			for (Entity entity : entities) {
//				if (entity instanceof Particle) {
//					Particle p = (Particle) entity;
//					//System.out.printf("Particle=%s, Position = %.2f/ %.2f/ %.2f, Velocity = %.2f/ %.2f/ %.2f\n", p.system.type.ParticleName, p.posX, p.posY, p.posZ, p.Velocity.x, p.Velocity.y, p.Velocity.z);
//					System.out.printf("Particle=%s, Color = %.2f/ %.2f/ %.2f, Velocity = %.2f/ %.2f/ %.2f\n", p.system.type.ParticleName, p.ParticleColor.x, p.ParticleColor.y, p.ParticleColor.z, p.Velocity.x, p.Velocity.y, p.Velocity.z);
//				}
//			}
//		}
	}
	
	public void addEntity(Entity entity) {
		activeEntities.add(entity);
	}
}
