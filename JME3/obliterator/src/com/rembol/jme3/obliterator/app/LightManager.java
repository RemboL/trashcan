package com.rembol.jme3.obliterator.app;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;

public class LightManager {

	private DirectionalLight directional;

	public void initLightAndShadows(ViewPort viewPort, Node rootNode,
			AssetManager assetManager) {
		directional = new DirectionalLight();
		directional.setDirection(new Vector3f(0f, 0f, -1f).normalize());
		directional.setColor(ColorRGBA.White.mult(.7f));
		rootNode.addLight(directional);
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White.mult(.3f));
		rootNode.addLight(ambient);

		final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(
				assetManager, SHADOWMAP_SIZE, 3);
		dlsr.setLight(directional);
		viewPort.addProcessor(dlsr);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

		viewPort.addProcessor(fpp);
	}

}
