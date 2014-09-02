package pl.rembol.jme3.pool;

import java.io.ObjectInputStream.GetField;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.SpotLightShadowFilter;
import com.jme3.shadow.SpotLightShadowRenderer;

/**
 * Example 12 - how to give objects physical properties so they bounce and fall.
 * 
 * @author base code by double1984, updated by zathras
 */
public class Main extends SimpleApplication {
	
	static FilterPostProcessor filterPostProcessor;

	public static void main(String args[]) {
		Main app = new Main();
		app.start();
	}

	/** Prepare the Physics Application State (jBullet) */
	private BulletAppState bulletAppState;
	private CameraNode camNode;

	private FilterPostProcessor getFilterPostProcessor() {
		if (filterPostProcessor == null) {
			filterPostProcessor = new FilterPostProcessor(assetManager);
		}
		
		return filterPostProcessor;
	}
	
	@Override
	public void simpleInitApp() {
		/** Set up Physics Game */
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		/** Add InputManager action: Left click triggers shooting. */
		inputManager.addMapping("shoot", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListener, "shoot");
		/** Initialize the scene, materials, and physics space */
		Table.initFloor(assetManager, rootNode, bulletAppState, 20f, 10f);
		initCrossHairs();

		initBalls();
		whiteBall = new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(-14f, 1f, 0f), ColorRGBA.White);
		
		initFlyCamera(whiteBall.getGeometry());

		createLights();
		
		rootNode.setShadowMode(ShadowMode.CastAndReceive);
	}

	private void initFlyCamera(Spatial target) {
		cam.setLocation(new Vector3f());
		// Disable the default flyby cam
		flyCam.setEnabled(false);
		// Enable a chase cam for this target (typically the player).
		ChaseCamera chaseCam = new ChaseCamera(cam, target, inputManager);
		chaseCam.setSmoothMotion(true);
	}

	private void initBalls() {
		for (int i = 0; i <= 3; ++i) {
			for (int j = 0; j <= i; ++j) {
				new Ball(assetManager, rootNode, bulletAppState,
						new Vector3f(10f + i * .87f, 1f, -i * .5f + j), ColorRGBA.Red);
			}
		}
		
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(14f, 1f, 0f), ColorRGBA.Black);
		
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(9f, 1f, 0f), ColorRGBA.Magenta);
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(0f, 1f, 0f), ColorRGBA.Blue);
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(-10f, 1f, 0f), ColorRGBA.Brown);
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(-10f, 1f, -3f), ColorRGBA.Yellow);
		new Ball(assetManager, rootNode, bulletAppState,
				new Vector3f(-10f, 1f, 3f), ColorRGBA.Green);
	}

	private void createLights() {
		createLight(-15, 10, -5);
		createLight(-15, 10, 5);
		createLight(15, 10, 5);
		createLight(15, 10, -5);
		
//		createShadow(createLight(0,-1,0));
	}

	private void createShadow(PointLight light) {
        final int SHADOWMAP_SIZE=1024;
        PointLightShadowFilter filter = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        filter.setLight(light);
        filter.setEnabled(true);
        getFilterPostProcessor().addFilter(filter);
        viewPort.addProcessor(filterPostProcessor);
	}

	private Light createLight(int posX, int posY, int posZ) {
		PointLight light = new PointLight();
		light.setPosition(new Vector3f(posX, posY, posZ));
		light.setColor(ColorRGBA.LightGray);
//		DirectionalLight light = new DirectionalLight();
//		light.setDirection(new Vector3f(posX, posY, posZ)
//				.normalizeLocal());
//		light.setColor(ColorRGBA.LightGray);
		rootNode.addLight(light);
		createShadow(light);
		return light;
	}
	

	/**
	 * Every time the shoot action is triggered, a new cannon ball is produced.
	 * The ball is set up to fly from the camera position in the camera
	 * direction.
	 */
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			if (name.equals("shoot") && !keyPressed) {
				makeCannonBall();
			}
		}
	};
	private Ball whiteBall;

	/**
	 * This method creates one individual physical cannon ball. By defaul, the
	 * ball is accelerated and flies from the camera position in the camera
	 * direction.
	 */
	public void makeCannonBall() {
//		Ball ball = new Ball(assetManager, rootNode, bulletAppState,
//				cam.getLocation(), ColorRGBA.White);

		whiteBall.setLinearVelocity(cam.getDirection().mult(25));
	}

	/** A plus sign used as crosshairs to help the player with aiming. */
	protected void initCrossHairs() {
		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // fake crosshairs :)
		ch.setLocalTranslation(
				// center
				settings.getWidth() / 2
						- guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}
}