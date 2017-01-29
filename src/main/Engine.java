package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import entities.Entity;
import entities.FXList;
import entities.Particle;
import entities.ParticleSystem;


public class Engine extends Thread {
	
	public Renderer renderer;
	public int maxTime = 1500; //150 frames = 5 Seconds
	double maxFrametime = 1.0 / (double)Main.FPS;
	//public boolean repeat = false;
	//public long globalTimer = 0;
	boolean run;
	
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
		long frameCounter = 0;
		while (run)
		{
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
			//	if (repeat) {
			//		frameCounter = 0;
			//	}else {
					run = false;
					renderer.updateStatus(0, (int) (1.0 / deltaT));
			//	}
			}
		}
	}
	
	//We actually don't need the deltaT, since all calculations are frame-wise
	private void gameStep(long frameCounter, double deltaT) {
		int c_fx = 0;
		int c_sys = 0;
		int c_part = 0;
		ArrayList<Entity> entities = new ArrayList<Entity>(activeEntities);
		//Iterate through copy of Entity list for concurrency reasons
		for (Entity entity : entities) {
			if (entity instanceof Particle) c_part++;
			else if (entity instanceof ParticleSystem) c_sys++;
			else if (entity instanceof FXList) c_fx++;

			if (entity.engine == null) entity.init(this);
			entity.update();
			if (entity.dead) {
				activeEntities.remove(entity);
				entity.clear3D();
			}
			
			renderer.updateStatus(c_part, (int) (1.0 / deltaT));
		}
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
