package pl.rembol.camera;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.lwjgl.opengl.GL11;

public class CameraTilt implements CameraTransformation {

	private MutableDouble tilt;
	
	CameraTilt(MutableDouble tilt) {
		this.tilt = tilt;
	}
	
	CameraTilt(double tilt) {
		this.tilt = new MutableDouble(tilt);
	}
	
	@Override
	public void transform() {
		GL11.glRotated(tilt.getValue(), 1, 0, 0);
	}

}
