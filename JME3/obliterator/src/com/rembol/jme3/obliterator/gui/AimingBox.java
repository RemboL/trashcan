package com.rembol.jme3.obliterator.gui;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.rembol.jme3.obliterator.util.Rectangle;

public class AimingBox {
	
	private Material material;
	
	private AssetManager assetManager;
	private Node guiNode;

	public AimingBox(AssetManager assetManager, Node guiNode) {
		this.assetManager = assetManager;
		this.guiNode = guiNode;
	}
	
	public void init(Rectangle aimingRectangle) {
		initMaterial();
		
		Quad quad = new Quad(aimingRectangle.width(), aimingRectangle.height());
		Geometry boxGeo = new Geometry("box", quad);
		boxGeo.setMaterial(material);
		boxGeo.move(new Vector3f(aimingRectangle.minX(), aimingRectangle.minY(), 0f));
		guiNode.attachChild(boxGeo);
		
	}
	
	private void initMaterial() {
		material = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", new ColorRGBA(1f, 1f, 1f, .1f));
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}

}
