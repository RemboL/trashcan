package pl.rembol.jme3.obliterator.ground;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class Ground extends Node {

	private static Material groundMaterial;
	private static Material wallMaterial;
	private AssetManager assetManager;

	private Vector3f position = new Vector3f(0f, 0f, 0f);
	private Node rootNode;

	public Ground(AssetManager assetManager, Node rootNode) {
		this.assetManager = assetManager;
		this.rootNode = rootNode;

	}

	public Ground position(Vector3f position) {
		this.position = position;
		return this;
	}

	public Ground init() {
		initMaterial();
		initShape();

		this.setLocalTranslation(position);

		rootNode.attachChild(this);

		return this;
	}

	private void initShape() {
		Quad quad = new Quad(10f, 50f);

		Geometry bottomGeo = new Geometry("node", quad);
		bottomGeo.setMaterial(groundMaterial);
		bottomGeo.setLocalTranslation(new Vector3f(-5f, 0f, 10f));
		bottomGeo.setLocalRotation(new Quaternion().fromAngleAxis(
				-FastMath.HALF_PI, Vector3f.UNIT_X));
		this.attachChild(bottomGeo);

		quad = new Quad(5f, 50f);
		Geometry leftGeo = new Geometry("node", quad);
		leftGeo.setMaterial(wallMaterial);
		leftGeo.setLocalTranslation(new Vector3f(-5f, 5f, 10f));
		leftGeo.setLocalRotation(new Quaternion().fromAngleAxis(
				-FastMath.HALF_PI, Vector3f.UNIT_X).mult(
				new Quaternion().fromAngleAxis(FastMath.HALF_PI,
						Vector3f.UNIT_Y)));
		this.attachChild(leftGeo);

		Geometry rightGeo = new Geometry("node", quad);
		rightGeo.setMaterial(wallMaterial);
		rightGeo.setLocalTranslation(new Vector3f(5f, 0f, 10f));
		rightGeo.setLocalRotation(new Quaternion().fromAngleAxis(
				-FastMath.HALF_PI, Vector3f.UNIT_X).mult(
				new Quaternion().fromAngleAxis(-FastMath.HALF_PI,
						Vector3f.UNIT_Y)));
		this.attachChild(rightGeo);
	}

	private void initMaterial() {
		if (groundMaterial == null) {
			groundMaterial = new Material(assetManager,
					"Common/MatDefs/Light/Lighting.j3md");
			groundMaterial.setBoolean("UseMaterialColors", true);
			groundMaterial.setColor("Diffuse", new ColorRGBA(0.9f, 0.4f, 0.2f,
					1F));
			groundMaterial.setColor("Ambient", new ColorRGBA(0.9f, 0.4f, 0.2f,
					1F));
		}

		if (wallMaterial == null) {

			wallMaterial = new Material(assetManager,
					"Common/MatDefs/Light/Lighting.j3md");
			wallMaterial.setBoolean("UseMaterialColors", true);
			wallMaterial.setColor("Diffuse", new ColorRGBA(1f, 0.6f, 0.3f, 1F));
			wallMaterial.setColor("Ambient", new ColorRGBA(1f, 0.6f, 0.3f, 1F));
		}
	}
	
}
