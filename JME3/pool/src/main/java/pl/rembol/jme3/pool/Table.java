package pl.rembol.jme3.pool;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class Table {

	private static final float HOLE_SIZE = 2f;

	private static Material floor_mat;

	private static Material side_mat;

	/** Make a solid floor and add it to the scene. */
	public static void initFloor(AssetManager assetManager, Node rootNode,
			BulletAppState bulletAppState, float sizeX, float sizeY) {
		initMaterial(assetManager);

		Box floor = new Box(sizeX, 0.1f, sizeY);
		floor.scaleTextureCoordinates(new Vector2f(3, 6));

		Geometry floor_geo = new Geometry("Floor", floor);
		floor_geo.setMaterial(floor_mat);
		floor_geo.setShadowMode(ShadowMode.Receive);
		floor_geo.setLocalTranslation(0, -0.1f, 0);
		attach(rootNode, bulletAppState, floor_geo, 0f);

		prepareWall(rootNode, bulletAppState, "left wall", new Vector2f(1f,
				sizeY - HOLE_SIZE), new Vector2f(-sizeX - 1f, 0f));
		prepareWall(rootNode, bulletAppState, "right wall", new Vector2f(1f,
				sizeY - HOLE_SIZE), new Vector2f(sizeX + 1f, 0f));

		prepareWall(rootNode, bulletAppState, "upper left wall", new Vector2f(
				sizeX / 2f - (0.75f * HOLE_SIZE), 1f), new Vector2f(
				-sizeX / 2f, -sizeY - 1f));
		prepareWall(rootNode, bulletAppState, "upper right wall", new Vector2f(
				sizeX / 2f - (0.75f * HOLE_SIZE), 1f), new Vector2f(sizeX / 2f,
				-sizeY - 1f));

		prepareWall(rootNode, bulletAppState, "lower left wall", new Vector2f(
				sizeX / 2f - (0.75f * HOLE_SIZE), 1f), new Vector2f(
				-sizeX / 2f, sizeY + 1f));
		prepareWall(rootNode, bulletAppState, "lower right wall", new Vector2f(
				sizeX / 2f - (0.75f * HOLE_SIZE), 1f), new Vector2f(sizeX / 2f,
				sizeY + 1f));

		prepareWall(rootNode, bulletAppState, "outer left wall", new Vector2f(
				1f, sizeY + 3f), new Vector2f(-sizeX - 3f, .75f));
		prepareWall(rootNode, bulletAppState, "outer right wall", new Vector2f(
				1f, sizeY + 3f), new Vector2f(sizeX + 3f, -.75f));
		prepareWall(rootNode, bulletAppState, "outer upper wall", new Vector2f(
				sizeX + 3f, 1f), new Vector2f(-.75f, -sizeY - 3f));
		prepareWall(rootNode, bulletAppState, "outer lower wall", new Vector2f(
				sizeX + 3f, 1f), new Vector2f(.75f, sizeY + 3f));
	}

	private static void prepareWall(Node rootNode,
			BulletAppState bulletAppState, String name, Vector2f size,
			Vector2f location) {
		Box wall = new Box(size.getX(), 0.5f, size.getY());
		wall.scaleTextureCoordinates(new Vector2f(0.75f * size.getY(),
				0.75f * size.getX()));
		Geometry wallGeo = new Geometry(name, wall);
		wallGeo.setMaterial(side_mat);
		wallGeo.setLocalTranslation(location.getX(), 0.5f, location.getY());
		attach(rootNode, bulletAppState, wallGeo, 1f);
	}

	private static void attach(Node rootNode, BulletAppState bulletAppState,
			Geometry geo, float restitution) {
		rootNode.attachChild(geo);
		/* Make the floor physical with mass 0.0f! */
		RigidBodyControl phy = new RigidBodyControl(0.0f);
		geo.addControl(phy);
		bulletAppState.getPhysicsSpace().add(phy);
		phy.setRestitution(restitution);
	}

	private static void initMaterial(AssetManager assetManager) {
		floor_mat = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		TextureKey key3 = new TextureKey("Pool-Table.bmp");
		key3.setGenerateMips(true);
		Texture tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		floor_mat.setTexture("DiffuseMap", tex3);

		side_mat = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		key3 = new TextureKey("Wood.jpg");
		key3.setGenerateMips(true);
		tex3 = assetManager.loadTexture(key3);
		tex3.setWrap(WrapMode.Repeat);
		side_mat.setTexture("DiffuseMap", tex3);
	}

}
