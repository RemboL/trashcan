package com.rembol.jme3.obliterator.enemy;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.rembol.jme3.obliterator.weapon.Bullet;

public class GruntService extends AbstractAppState {

	private AssetManager assetManager;

	private Node rootNode;

	public GruntService(AssetManager assetManager, Node rootNode) {
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	private List<Grunt> grunts = new ArrayList<Grunt>();

	private int difficulty = 5;

	private float timeToReleaseNextGrunt = 0;

	@Override
	public void update(float tpf) {
		List<Grunt> gruntsToRemove = new ArrayList<Grunt>();

		for (Grunt grunt : grunts) {
			if (grunt.finished) {
				gruntsToRemove.add(grunt);
				difficulty--;
			}

			if (grunt.killed) {
				gruntsToRemove.add(grunt);
				difficulty++;
			}

		}
		grunts.removeAll(gruntsToRemove);

		if (difficulty < 5) {
			difficulty = 5;
		}

		timeToReleaseNextGrunt -= tpf;

		if (timeToReleaseNextGrunt <= 0 || grunts.isEmpty()) {
			Grunt grunt = new Grunt(assetManager, rootNode)
					.position(
							new Vector3f(FastMath.nextRandomFloat() * 4f - 2f,
									0, -30)).init();
			grunts.add(grunt);

			timeToReleaseNextGrunt = 15f / difficulty;
		}

	}

	public boolean checkForCollisions(Bullet bullet) {
		for (Grunt grunt : grunts) {
			if (checkForCollisions(grunt, bullet)) {
				grunt.hit();
				return true;
			}
		}

		return false;
	}

	private boolean checkForCollisions(Grunt grunt, Bullet bullet) {
		if (Math.abs(bullet.position().x - grunt.getWorldTranslation().x) > .55f) {
			return false;
		}
		if (Math.abs(bullet.position().y - (grunt.getWorldTranslation().y + .5f)) > .55f) {
			return false;
		}
		if (Math.abs(bullet.position().z - grunt.getWorldTranslation().z) > .55f) {
			return false;
		}

		return true;
	}

}
