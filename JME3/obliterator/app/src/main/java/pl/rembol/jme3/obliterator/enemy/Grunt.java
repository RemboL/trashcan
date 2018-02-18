package pl.rembol.jme3.obliterator.enemy;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;

public class Grunt extends Node {

	private static final ColorRGBA RED_TRANSPARENT = new ColorRGBA(1f, .5f,
			.5f, .5f);
	private static final ColorRGBA GREEN_TRANSPARENT = new ColorRGBA(.5f, 1f,
			.5f, .5f);
	private static final ColorRGBA RED_SOLID = new ColorRGBA(1f, .5f, .5f, 1f);
	private static final ColorRGBA GREEN_SOLID = new ColorRGBA(.5f, 1f, .5f, 1f);

	private Material material;
	private Material wireMaterial;
	private AssetManager assetManager;

	private Vector3f position;
	private Node rootNode;
	private Control control;

	private float hp = 1;

	boolean killed = false;
	boolean finished = false;

	public Grunt(AssetManager assetManager, Node rootNode) {
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	public Grunt position(Vector3f position) {
		this.position = position;
		return this;
	}

	public Grunt init() {
		initMaterial();
		initShape();

		this.setLocalTranslation(position);

		rootNode.attachChild(this);

		control = new Control();
		this.addControl(control);

		return this;
	}

	private void initShape() {
		Box box = new Box(.5f, .5f, .5f);

		Geometry boxGeo = new Geometry("node", box);
		boxGeo.setMaterial(material);
		boxGeo.setQueueBucket(Bucket.Transparent);
		boxGeo.setLocalTranslation(new Vector3f(0f, .5f, 0f));

		WireBox wireBox = new WireBox(.5f, .5f, .5f);
		Geometry wireGeo = new Geometry("wire", wireBox);
		wireGeo.setMaterial(wireMaterial);
		wireGeo.setLocalTranslation(new Vector3f(0f, .5f, 0f));

		this.attachChild(boxGeo);
		this.attachChild(wireGeo);

	}

	private void initMaterial() {
		material = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Diffuse", new ColorRGBA(.5f, 1f, .5f, .5f));
		material.setColor("Ambient", new ColorRGBA(.5f, 1f, .5f, .5f));
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		wireMaterial = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		wireMaterial.setColor("Color", new ColorRGBA(.5f, 1f, .5f, 1f));
		wireMaterial.getAdditionalRenderState().setWireframe(true);
	}

	public void hit() {
		hp -= .2f;

		ColorRGBA colorTransparent = new ColorRGBA();
		colorTransparent.interpolateLocal(RED_TRANSPARENT, GREEN_TRANSPARENT, hp);
		material.setColor("Diffuse", colorTransparent);
		material.setColor("Ambient", colorTransparent);

		ColorRGBA colorSolid = new ColorRGBA();
		colorSolid.interpolateLocal(RED_SOLID, GREEN_SOLID, hp);
		wireMaterial.setColor("Color", colorSolid);

	}

	public void destroy(boolean withParticles) {
		if (withParticles) {
			int numberOfParticles = FastMath.nextRandomInt(6, 9);
			for (int i = 0; i < numberOfParticles; ++i) {
				new GruntParticle(assetManager, rootNode)
						.position(this.getWorldTranslation().clone()).init();
			}
		}

		this.removeControl(control);
		rootNode.detachChild(this);

	}

	private class Control extends AbstractControl {

		private float timeToChangeVelocity = 0;

		private Vector3f velocity;

		@Override
		protected void controlUpdate(float tpf) {
			timeToChangeVelocity -= tpf;

			if (velocity == null || timeToChangeVelocity < 0) {
				velocity = new Vector3f(FastMath.nextRandomFloat() - .5f, 0f,
						2f);
				timeToChangeVelocity = FastMath.nextRandomFloat() * 3 + 2;
			}

			move(velocity.mult(tpf));
			if (getWorldTranslation().x < -4.5f) {
				getWorldTranslation().x = -4.5f;
			}
			if (getWorldTranslation().x > 4.5f) {
				getWorldTranslation().x = 4.5f;
			}

			if (getWorldTranslation().z > 0) {
				finished = true;
				destroy(false);
				return;
			}

			if (hp <= 0) {
				killed = true;
				destroy(true);
				return;
			}
		}

		@Override
		protected void controlRender(RenderManager paramRenderManager,
				ViewPort paramViewPort) {
		}
	}

}
