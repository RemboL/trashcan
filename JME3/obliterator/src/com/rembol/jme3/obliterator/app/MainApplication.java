package com.rembol.jme3.obliterator.app;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.controls.TouchTrigger;
import com.jme3.math.Vector3f;
import com.rembol.jme3.obliterator.enemy.GruntService;
import com.rembol.jme3.obliterator.ground.Ground;
import com.rembol.jme3.obliterator.gui.AimingBox;
import com.rembol.jme3.obliterator.util.Rectangle;
import com.rembol.jme3.obliterator.weapon.Gun;

public class MainApplication extends SimpleApplication {

	public MainApplication() {
		super(new StatsAppState());
	}

	public static void main(String[] args) {
		MainApplication app = new MainApplication();
		app.start();
	}

	private Ground ground;

	@Override
	public void simpleInitApp() {
		
		cam.setLocation(new Vector3f(0f, 10f, 10f));
		cam.lookAt(new Vector3f(0f, 0f, -10f), Vector3f.UNIT_Y);

		new LightManager()
				.initLightAndShadows(viewPort, rootNode, assetManager);
		
		GruntService gruntService = new GruntService(assetManager, rootNode);
		stateManager.attach(gruntService);
		
		ground = new Ground(assetManager, rootNode).position(new Vector3f(0, 0, 0))
		.init();

		Gun gun = new Gun(this, assetManager, rootNode);
		stateManager.attach(gun);

		inputManager.addMapping("Touch", new TouchTrigger(0));

		inputManager.addListener(
				new ShootListener(stateManager, getAimingBoundaries()), new String[] { "Touch" });
		
		new AimingBox(assetManager, guiNode).init(getAimingBoundaries());
	}

	@Override
	public void simpleUpdate(float tpf) {
	}
	
	public Ground ground() {
		return ground;
	}
	
	public Rectangle getAimingBoundaries() {
		return new Rectangle(settings.getWidth() * 2 / 3 + 50, 50, settings.getWidth() - 50, settings.getHeight() / 2 - 50);
	}

}
