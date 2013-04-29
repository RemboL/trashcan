package pl.rembol.camera;

import javax.vecmath.Vector3d;

public class Object3d {
	protected Vector3d position = new Vector3d(0, 0, 0);

	protected double direction = 0;

	protected double velocity = 0;

	protected double maxVelocity = 3;

	protected double acceleration = 0.01;

	protected double rotSpeed = 0.04;

	protected int tickRotation = 0;

	protected int tickAcceleration = -1;

	public Object3d() {
	}

	public Object3d(Vector3d position) {
		this.position = position;
	}

	public void rotateLeft() {
		tickRotation = 1;
	}

	public void rotateRight() {
		tickRotation = -1;
	}

	public void accelerate() {
		tickAcceleration = 1;
	}

	public void tick() {
		updateMovement();
		checkMovementBoundaries();
		updatePosition();
		resetMovement();
	}

	public Vector3d getPosition() {
		return position;
	}

	public void updateMovement() {
		velocity += tickAcceleration * acceleration;
		direction += tickRotation * rotSpeed;
	}

	protected void checkMovementBoundaries() {
		if (velocity < 0) {
			velocity = 0;
		}
		if (velocity > maxVelocity) {
			velocity = maxVelocity;
		}

		if (direction < 0) {
			direction += 2 * Math.PI;
		}
		if (direction > 2 * Math.PI) {
			direction -= 2 * Math.PI;
		}
	}

	protected void updatePosition() {
		double xVel = Math.cos(-direction - Math.PI / 2) * velocity;
		double zVel = Math.sin(-direction - Math.PI / 2) * velocity;

                position.add(new Vector3d(xVel, 0, zVel));
                
	}

	protected void resetMovement() {
		tickAcceleration = -1; // deceleration
		tickRotation = 0;
	}

	@Override
	public String toString() {
		return "dir:" + direction + " vel: " + velocity + " "
				+ position.toString();
	}

}
