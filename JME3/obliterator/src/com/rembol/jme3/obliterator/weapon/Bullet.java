package com.rembol.jme3.obliterator.weapon;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Sphere;
import com.rembol.jme3.obliterator.app.MainApplication;
import com.rembol.jme3.obliterator.enemy.GruntService;

public class Bullet extends AbstractControl {

	private static Material material;
	private static Material shadowMaterial;
	private AssetManager assetManager;

	private Vector3f position = new Vector3f(0f, 0f, 0f);
	private Vector3f direction = new Vector3f(0f, 0f, -20f);
	private Node node;
	private Geometry shadowNode;
	private Node rootNode;
	private MainApplication mainApp;

	private boolean toBeDestroyed = false;

	public Bullet(MainApplication mainApp, AssetManager assetManager,
			Node rootNode) {
		this.mainApp = mainApp;
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	public Bullet position(Vector3f position) {
		this.position = position;
		return this;
	}

	public Bullet direction(Vector3f direction) {
		this.direction = direction;
		return this;
	}

	public void init() {
		initMaterial();
		node = initShape();

		node.setLocalTranslation(position);

		rootNode.attachChild(node);

		node.addControl(this);
	}

	private Node initShape() {
		Node node = new Node();

		Sphere sphere = new Sphere(5, 5, .1f);

		Geometry sphereGeo = new Geometry("node", sphere);
		sphereGeo.setMaterial(material);

		shadowNode = new Geometry("node", sphere);
		shadowNode.setMaterial(shadowMaterial);
		rootNode.attachChild(shadowNode);

		node.attachChild(sphereGeo);

		return node;
	}

	private void initMaterial() {
		if (material == null) {
			material = new Material(assetManager,
					"Common/MatDefs/Light/Lighting.j3md");
			material.setBoolean("UseMaterialColors", true);
			material.setColor("Diffuse", ColorRGBA.Red);
			material.setColor("Ambient", ColorRGBA.Red);
		}
		if (shadowMaterial == null) {
			shadowMaterial = new Material(assetManager,
					"Common/MatDefs/Light/Lighting.j3md");
			shadowMaterial.setBoolean("UseMaterialColors", true);
			shadowMaterial.setColor("Diffuse", ColorRGBA.Black);
			shadowMaterial.setColor("Ambient", ColorRGBA.Black);
		}

	}

	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
	}

	@Override
	protected void controlUpdate(float tpf) {
		node.move(direction.mult(tpf));
		
		direction.addLocal(Vector3f.UNIT_Y.mult(-tpf * 10f));
		
		Vector3f shadowPosition = node.getWorldTranslation().clone();
		shadowPosition.y = 0;
		shadowNode.setLocalTranslation(shadowPosition);

		if (mainApp.getStateManager().getState(GruntService.class)
				.checkForCollisions(this)) {
			toBeDestroyed = true;
		}

		if (position().y < 0) {
			toBeDestroyed = true;
		}
		
		if (position().x < -5f || position().x > 5f) {
			toBeDestroyed = true;
		}

		if (toBeDestroyed) {
			rootNode.detachChild(node);
			node.removeControl(this);
			rootNode.detachChild(shadowNode);
		}
	}

	public Vector3f position() {
		return node.getWorldTranslation();
	}

}
