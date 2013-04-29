package pl.rembol.copernicus.system;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import pl.rembol.camera.Camera;

/**
 *
 * @author RemboL
 */
public class AstralSystem {

    private Map<String, AstralObject> astralObjects = new HashMap<String, AstralObject>();
    private boolean isMovement = false;
    private boolean isGravity = false;
    double scaleSize = 1.0 / 20;
    double scaleDistance = 1.0 / Math.pow(10, 7);
    long scaleTime = 1;
    private AstralObject focus;

    public AstralSystem() {
    }

    public void add(String name) {
        AstralObject astralObject = new AstralObject(this);
        astralObject.setName(name);
        astralObjects.put(name, astralObject);

    }

    public void rename(String oldName, String newName) {
        AstralObject astralObject = astralObjects.get(oldName);
        if (astralObject == null) {
            return;
        }

        astralObject.setName(newName);

        astralObjects.put(newName, astralObject);
        astralObjects.remove(oldName);
    }

    public void remove(String name) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObjects.remove(name);
    }

    public void setColor(String name, int red, int green, int blue) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObject.setColor(new Color(red, green, blue));
    }

    public void setMass(String name, double mass) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObject.setMass(mass);
    }

    public void setRadius(String name, double radius) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObject.setRadius(radius);
    }

    public void setPosition(String name, double x, double y, double z) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObject.setPosition(new Vector3d(x, y, z));
    }

    public void setVelocity(String name, double x, double y, double z) {
        AstralObject astralObject = astralObjects.get(name);
        if (astralObject == null) {
            return;
        }

        astralObject.setVelocity(new Vector3d(x, y, z));
    }

    public void setScaleSize(double scaleSize) {
        this.scaleSize = scaleSize;
    }

    public void setScaleDistance(double scaleDistance) {
        this.scaleDistance = scaleDistance;
    }

    public void setScaleTime(long scaleTime) {
        this.scaleTime = scaleTime;
    }

    public void setFocus(String name) {
        AstralObject newFocus = astralObjects.get(name);
        if (newFocus != null) {
            focus = newFocus;
        }
    }

    public void tick() {
        for (AstralObject first : astralObjects.values()) {
            for (AstralObject second : astralObjects.values()) {
                if (first != second) {
                    first.addGravityInfluence(second);
                }
            }
        }

        for (AstralObject astralObject : astralObjects.values()) {
            astralObject.tick();
        }

    }

    public void draw(Camera camera) {
        if (focus != null) {
            Vector3d newCameraPosition = new Vector3d(focus.getPosition());
            newCameraPosition.scale(scaleDistance);
            camera.setPosition(newCameraPosition);
        }

        for (AstralObject astralObject : astralObjects.values()) {
            astralObject.draw(camera);
        }
    }
}
