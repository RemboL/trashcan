package pl.rembol.camera;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.opengl.GL11;

public class CameraMoveBackward implements CameraTransformation {

	private MutableDouble distance;
	
	CameraMoveBackward(MutableDouble distance) {
		this.distance = distance;
	}

	CameraMoveBackward(double distance) {
		this.distance = new MutableDouble(distance);
	}

	
	@Override
	public void transform() {
		GL11.glTranslated(0, 0, -distance.getValue());
	}

}
