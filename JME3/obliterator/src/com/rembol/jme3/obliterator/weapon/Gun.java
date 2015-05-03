package com.rembol.jme3.obliterator.weapon;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.rembol.jme3.obliterator.app.MainApplication;

public class Gun extends AbstractAppState {

	private MainApplication mainApp;
	private AssetManager assetManager;
	private Node rootNode;

	float timeToLoad = 0;

	public Gun(MainApplication mainApplication, AssetManager assetManager, Node rootNode) {
		this.mainApp = mainApplication;
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	public void fire(float x, float y) {
		if (timeToLoad == 0) {
			new Bullet(mainApp, assetManager, rootNode).position(
					new Vector3f(0, 1f, 5)).direction(new Vector3f(x * 5f, y * 5f, -10f).normalize().mult(20f)).init();
			timeToLoad += .1;
		}

	}

	@Override
	public void update(float tpf) {
		if (timeToLoad > 0) {
			timeToLoad -= tpf;
		}

		if (timeToLoad < 0) {
			timeToLoad = 0;
		}
	}

}
