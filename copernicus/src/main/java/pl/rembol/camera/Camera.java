package pl.rembol.camera;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Vector3d;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.opengl.GL11;


public class Camera extends Object3d {

    private List<CameraTransformation> transformations = new ArrayList<CameraTransformation>();
    private MutableDouble cameraTilt = new MutableDouble(0);
    private MutableDouble cameraRotation = new MutableDouble(0);
    private MutableDouble zoomOut = new MutableDouble(5);

    public void resetTransformations() {
        transformations.add(CameraTransformations.moveBackward(zoomOut));
        transformations.add(CameraTransformations.tilt(cameraTilt));
        transformations.add(CameraTransformations.rotate(cameraRotation));
    }

    public Camera(Vector3d position) {
        super(position);
        resetTransformations();
    }

    public Camera() {
        super();
        resetTransformations();
    }

    public void addTransformation(CameraTransformation transformation) {
        transformations.add(transformation);
    }

    public void translateCameraPosition() {
        GL11.glLoadIdentity();

        for (CameraTransformation transformation : transformations) {
            transformation.transform();
        }

        GL11.glTranslated(-getPosition().x, -getPosition().y,
                -getPosition().z);
    }

    public void setCameraTilt(double cameraTilt) {
        if (cameraTilt > 90) {
            cameraTilt = 90;
        }

        if (cameraTilt < -90) {
            cameraTilt = -90;
        }

        this.cameraTilt.setValue(cameraTilt);
    }

    public double getCameraTilt() {
        return this.cameraTilt.getValue();
    }

    public void setCameraRotation(double cameraRotation) {
        if (cameraRotation > 360) {
            cameraRotation -= 360;
        }

        if (cameraRotation < 0) {
            cameraRotation += 360;
        }

        this.cameraRotation.setValue(cameraRotation);
    }

    public double getCameraRotation() {
        return this.cameraRotation.getValue();
    }

    public void setZoomOut(double zoomOut) {
        Logger.getLogger(Camera.class.getName()).log(Level.INFO, "setZoomOut("+zoomOut+")");
        
        this.zoomOut.setValue(zoomOut);
    }

    public double getZoomOut() {
        return this.zoomOut.getValue();
    }
    
    public void setPosition(Vector3d position) {
        this.position = new Vector3d(position);
    }
}
