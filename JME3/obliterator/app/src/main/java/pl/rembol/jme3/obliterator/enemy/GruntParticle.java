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

public class GruntParticle extends Node {

	private Material material;
	private Material wireMaterial;
	private AssetManager assetManager;
	private Node rootNode;
	private Vector3f position;
	private Control control;

	public GruntParticle(AssetManager assetManager, Node rootNode) {
		this.assetManager = assetManager;
		this.rootNode = rootNode;

	}

	public GruntParticle position(Vector3f position) {
		this.position = position;
		return this;
	}
	
	public void init() {
		initMaterial();
		initShape();

		this.setLocalTranslation(position);

		rootNode.attachChild(this);

		control = new Control();
		this.addControl(control);

}
	
	private void initShape() {
		Box box = new Box(.05f, .05f, .05f);

		Geometry boxGeo = new Geometry("node", box);
		boxGeo.setMaterial(material);
		boxGeo.setQueueBucket(Bucket.Transparent);
		boxGeo.setLocalTranslation(new Vector3f(0f, .1f, 0f));

		WireBox wireBox = new WireBox(.05f, .05f, .05f);
		Geometry wireGeo = new Geometry("wire", wireBox);
		wireGeo.setMaterial(wireMaterial);
		wireGeo.setLocalTranslation(new Vector3f(0f, .1f, 0f));

		this.attachChild(boxGeo);
		this.attachChild(wireGeo);
	}
	
	public void destroy() {
		this.removeControl(control);
		rootNode.detachChild(this);
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
	
	private class Control extends AbstractControl {
		
		private Vector3f velocity = new Vector3f(FastMath.nextRandomFloat() * 2 - 1, 5f, FastMath.nextRandomFloat() * 2 - 1);

		private float timeToLive = FastMath.nextRandomFloat() * .5f + .5f;

		@Override
		protected void controlUpdate(float tpf) {
			move(velocity.clone().mult(tpf));
			velocity.addLocal(Vector3f.UNIT_Y.mult(-tpf * 10f));
			timeToLive -= tpf;

			if (timeToLive <= 0) {
				destroy();
			}
		}

		@Override
		protected void controlRender(RenderManager paramRenderManager,
				ViewPort paramViewPort) {
		}
	}

}
