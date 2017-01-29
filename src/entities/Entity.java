package entities;

import main.Engine;
import javax.vecmath.Vector3d;

public abstract class Entity {
	public Engine engine; //The active engine this is connected to
	
	public double posX;
	public double posY;
	public double posZ;
	
	public double rotPitch;
	public double rotYaw;
	public double rotRoll;
	
	int timer = 0;
	public boolean dead = false;
	
	public void init(Engine engine) {
		this.engine = engine;
	}
	
	public void update() {
		timer++;
	}
	
	public void setPosition(Vector3d pos) {
		this.posX = pos.x;
		this.posY = pos.y;
		this.posZ = pos.z;
	}
	
	public Vector3d getPosition() {
		return new Vector3d(posX, posY, posZ);
	}
	
	public void clear3D() {}
}
