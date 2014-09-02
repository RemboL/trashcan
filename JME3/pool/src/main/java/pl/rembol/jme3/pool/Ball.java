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
import com.jme3.scene.shape.Sphere.TextureMode;

public class Ball {

	private static final Sphere sphere;

	static {
		/** Initialize the cannon ball geometry */
		sphere = new Sphere(32, 32, 0.48f, true, false);
		sphere.setTextureMode(TextureMode.Projected);

	}

	private Material stone_mat;
	private RigidBodyControl ball_phy;
	private Geometry ball_geo;

	public Ball(AssetManager assetManager, Node rootNode,
			BulletAppState bulletAppState, Vector3f startLocation, ColorRGBA color) {

		initMaterial(assetManager, color);
		ball_geo = new Geometry("cannon ball", sphere);
		ball_geo.setMaterial(stone_mat);
		rootNode.attachChild(ball_geo);
		/** Position the cannon ball */
		ball_geo.setLocalTranslation(startLocation);
		ball_phy = new RigidBodyControl(1f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(ball_phy);
		bulletAppState.getPhysicsSpace().add(ball_phy);
		/** Accelerate the physcial ball to shoot it. */
		ball_phy.setDamping(0.2f, 0.1f);
		ball_phy.setRestitution(1f);
	}
	
	public Spatial getGeometry() {
		return ball_geo;
	}

	private void initMaterial(AssetManager assetManager, ColorRGBA color) {
		stone_mat = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		stone_mat.setBoolean("UseMaterialColors", true);
		stone_mat.setColor("Diffuse", color);
		stone_mat.setColor("Specular", ColorRGBA.White);
		stone_mat.setFloat("Shininess", 64f); // [0,128]
		
	}

	public void setLinearVelocity(Vector3f velocity) {
		ball_phy.setLinearVelocity(velocity);
	}

}
