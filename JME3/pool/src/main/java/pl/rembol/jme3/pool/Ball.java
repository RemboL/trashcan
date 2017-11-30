package pl.rembol.jme3.pool;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

public class Ball {

	private static final Sphere sphere;

	static {
		/** Initialize the cannon ball geometry */
		sphere = new Sphere(32, 32, 0.5f, true, false);
	}

	private Material ballMaterial;
	private RigidBodyControl ballPhysicsControl;
	private Geometry ballGeometry;

	public Ball(AssetManager assetManager, Node rootNode,
			BulletAppState bulletAppState, Vector3f startLocation, ColorRGBA color) {

		initMaterial(assetManager, color);
		ballGeometry = new Geometry("cannon ball", sphere);
		ballGeometry.setMaterial(ballMaterial);
		rootNode.attachChild(ballGeometry);
		
		/** Position the cannon ball */
		ballGeometry.setLocalTranslation(startLocation);
		
		/** Add physical ball to physics space. */
		ballPhysicsControl = new RigidBodyControl(1f);
		ballGeometry.addControl(ballPhysicsControl);
		bulletAppState.getPhysicsSpace().add(ballPhysicsControl);
		ballPhysicsControl.setDamping(0.2f, 0.1f);
		ballPhysicsControl.setRestitution(1f);
	}
	
	public Spatial getGeometry() {
		return ballGeometry;
	}

	private void initMaterial(AssetManager assetManager, ColorRGBA color) {
		ballMaterial = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		ballMaterial.setBoolean("UseMaterialColors", true);
		ballMaterial.setColor("Diffuse", color);
		ballMaterial.setColor("Specular", ColorRGBA.White);
		ballMaterial.setFloat("Shininess", 64f); // [0,128]
		
	}

	public void setLinearVelocity(Vector3f velocity) {
		ballPhysicsControl.setLinearVelocity(velocity);
	}

}
