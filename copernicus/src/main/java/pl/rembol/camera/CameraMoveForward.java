package pl.rembol.camera;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.opengl.GL11;

public class CameraMoveForward implements CameraTransformation {

	private MutableDouble distance;
	
	CameraMoveForward(MutableDouble distance) {
		this.distance = distance;
	}

	CameraMoveForward(double distance) {
		this.distance = new MutableDouble(distance);
	}

	
	@Override
	public void transform() {
		GL11.glTranslated(0, distance.getValue(), 0);
	}

}
