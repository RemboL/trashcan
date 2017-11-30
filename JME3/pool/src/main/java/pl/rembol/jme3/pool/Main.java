package pl.rembol.jme3.pool;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication {

    public static void main(String args[]) {
        Main app = new Main();
        app.start();
    }

    /**
     * Prepare the Physics Application State (jBullet)
     */
    private BulletAppState bulletAppState;

    private Ball whiteBall;

    @Override
    public void simpleInitApp() {
        /** Set up Physics Game */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        initTable();
        createLights();
        initWhiteBall();
        initFlyCamera(whiteBall.getGeometry());
        initBalls();
        initInput();
    }

    private void initWhiteBall() {
        whiteBall = new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-14f, 0f, 0f), ColorRGBA.White);
    }

    private void initTable() {
        Spatial table = assetManager.loadModel("table.blend");
        table.setLocalTranslation(0, -.5f, 0);
        RigidBodyControl tableBodyControl = new RigidBodyControl(0.0f);
        table.addControl(tableBodyControl);
        bulletAppState.getPhysicsSpace().add(tableBodyControl);
        tableBodyControl.setRestitution(0f);
        rootNode.attachChild(table);
    }

    private void initInput() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(
                MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("shoot") && !keyPressed) {
                    hitWhiteBall();
                }
            }
        }, "shoot");
    }

    private void initFlyCamera(Spatial target) {
        cam.setLocation(new Vector3f());
        flyCam.setEnabled(false);
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
                new Vector3f(14f, 0f, 0f), ColorRGBA.Black);

        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(9f, 0f, 0f), ColorRGBA.Magenta);
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(0f, 0f, 0f), ColorRGBA.Blue);
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, 0f), ColorRGBA.Brown);
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, -3f), ColorRGBA.Yellow);
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, 3f), ColorRGBA.Green);
    }

    private void createLights() {
        createLight(-15, 10, -5);
        createLight(-15, 10, 5);
        createLight(15, 10, 5);
        createLight(15, 10, -5);
    }

    private Light createLight(int posX, int posY, int posZ) {
        PointLight light = new PointLight();
        light.setPosition(new Vector3f(posX, posY, posZ));
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);
        return light;
    }

    private void hitWhiteBall() {
        whiteBall.getGeometry().getControl(RigidBodyControl.class)
                .applyCentralForce(cam.getDirection().setY(0).normalize().mult(1000));
    }
}